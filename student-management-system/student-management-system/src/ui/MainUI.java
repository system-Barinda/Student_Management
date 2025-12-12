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

    private final JTextField txtFirst = new JTextField(12);
    private final JTextField txtLast = new JTextField(12);
    private final JTextField txtEmail = new JTextField(18);
    private final JTextField txtAge = new JTextField(4);

    private final JButton btnAdd = new JButton("Add");
    private final JButton btnLoad = new JButton("Load");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnDelete = new JButton("Delete");
    private final JButton btnClear = new JButton("Clear");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "First Name", "Last Name", "Email", "Age"}, 0
    );

    private final JTable table = new JTable(tableModel);

    public MainUI() {
        super("Student Management (Swing + JDBC)");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // =========================
        // HEADER
        // =========================
        JLabel header = new JLabel("Student Management System", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // =========================
        // FORM PANEL (TOP)
        // =========================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtFirst, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtLast, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtAge, gbc);

        // Wrap header + form into one top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // =========================
        // TABLE (CENTER)
        // =========================
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // =========================
        // BUTTONS (BOTTOM)
        // =========================
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.add(btnAdd);
        buttons.add(btnLoad);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        add(buttons, BorderLayout.SOUTH);

        // =========================
        // BUTTON ACTIONS
        // =========================
        btnAdd.addActionListener(e -> onAdd());
        btnLoad.addActionListener(e -> loadData());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtFirst.setText(table.getValueAt(row, 1).toString());
                txtLast.setText(table.getValueAt(row, 2).toString());
                txtEmail.setText(table.getValueAt(row, 3).toString());
                txtAge.setText(table.getValueAt(row, 4).toString());
            }
        });

        loadData();
    }

    // =========================
    // ADD STUDENT
    // =========================
    private void onAdd() {
        try {
            service.addStudent(
                    txtFirst.getText(),
                    txtLast.getText(),
                    txtEmail.getText(),
                    txtAge.getText()
            );

            JOptionPane.showMessageDialog(this, "Student added successfully!");
            loadData();
            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // UPDATE STUDENT
    // =========================
    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update.");
            return;
        }

        try {
            Integer id = Integer.parseInt(table.getValueAt(row, 0).toString());
            service.updateStudent(id,
                    txtFirst.getText(),
                    txtLast.getText(),
                    txtEmail.getText(),
                    txtAge.getText()
            );

            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            loadData();
            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // DELETE STUDENT
    // =========================
    private void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }

        try {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            service.deleteStudent(id);

            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            loadData();
            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================
    // LOAD TABLE
    // =========================
    private void loadData() {
        try {
            List<Student> list = service.listAll();
            tableModel.setRowCount(0);

            for (Student s : list) {
                tableModel.addRow(new Object[]{
                        s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getAge()
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    // =========================
    // CLEAR FORM
    // =========================
    private void clearForm() {
        txtFirst.setText("");
        txtLast.setText("");
        txtEmail.setText("");
        txtAge.setText("");
        table.clearSelection();
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}
