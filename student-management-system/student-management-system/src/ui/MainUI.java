package ui;

import model.Student;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainUI extends JFrame {
    private final StudentService service = new StudentService();

    private final JTextField txtFirst = new JTextField(15);
    private final JTextField txtLast = new JTextField(15);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtAge = new JTextField(4);

    private final JButton btnAdd = new JButton("Add");
    private final JButton btnLoad = new JButton("Load");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID","First Name","Last Name","Email","Age"}, 0
    );
    private final JTable table = new JTable(tableModel);

    public MainUI() {
        super("Student Management (Swing + JDBC)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Top form
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("First:")); form.add(txtFirst);
        form.add(new JLabel("Last:")); form.add(txtLast);
        form.add(new JLabel("Email:")); form.add(txtEmail);
        form.add(new JLabel("Age:")); form.add(txtAge);

        // Buttons panel
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(btnAdd);
        buttons.add(btnLoad);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        // Table inside scroll
        JScrollPane scroll = new JScrollPane(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(form, BorderLayout.NORTH);
        cp.add(buttons, BorderLayout.CENTER);
        cp.add(scroll, BorderLayout.SOUTH);

        // Wire events
        btnAdd.addActionListener(e -> onAdd());
        btnLoad.addActionListener(e -> loadData());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtFirst.setText(String.valueOf(table.getValueAt(row,1)));
                txtLast.setText(String.valueOf(table.getValueAt(row,2)));
                txtEmail.setText(String.valueOf(table.getValueAt(row,3)));
                txtAge.setText(String.valueOf(table.getValueAt(row,4)));
            }
        });

        loadData();
    }

    private void onAdd() {
        try {
            service.addStudent(txtFirst.getText(), txtLast.getText(), txtEmail.getText(), txtAge.getText());
            JOptionPane.showMessageDialog(this, "Added successfully.");
            loadData();
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            List<Student> list = service.listAll();
            tableModel.setRowCount(0);
            for (Student s : list) {
                tableModel.addRow(new Object[]{s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getAge()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to update.");
            return;
        }
        Integer id = (Integer) table.getValueAt(row, 0);
        try {
            service.updateStudent(id, txtFirst.getText(), txtLast.getText(), txtEmail.getText(), txtAge.getText());
            JOptionPane.showMessageDialog(this, "Updated successfully.");
            loadData();
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }
        int id = (Integer) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.deleteStudent(id);
            JOptionPane.showMessageDialog(this, "Deleted successfully.");
            loadData();
            clearForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        txtFirst.setText("");
        txtLast.setText("");
        txtEmail.setText("");
        txtAge.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainUI().setVisible(true);
        });
    }
}
