package com.sms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: Student Management System
 * Author: Aime Thierry Byiringiro
 * Reg No: 24rp07221
 * Description: Main class for the Swing GUI application, handling user interaction, validation, and data display.
 */
public class StudentManagementGUI extends JFrame {
    // Simple, short, and clear explanation: Database manager instance for CRUD operations.
    private final DBManager dbManager = new DBManager();

    // GUI Components
    private JTextField txtRegNumber, txtName, txtMath, txtJava, txtPHP;
    private JButton btnAdd, btnEdit, btnDelete, btnClear;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel lblOverallAvg, lblHighestAvgStudent;

    // Simple, short, and clear explanation: Regex pattern for marks validation (0-100).
    private static final String MARK_REGEX = "^(100|[1-9]?[0-9])$";
    private static final Pattern MARK_PATTERN = Pattern.compile(MARK_REGEX);

    public StudentManagementGUI() {
        // Simple, short, and clear explanation: Setup the main window.
        setTitle("Student Management System - Aime Thierry Byiringiro (24rp07221)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize components
        initComponents();
        
        // Load initial data
        loadStudentsData();
        
        // Center the window
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // --- Input Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Reg Number
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Reg Number:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtRegNumber = new JTextField(15); formPanel.add(txtRegNumber, gbc);

        // Row 2: Name
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtName = new JTextField(15); formPanel.add(txtName, gbc);

        // Row 3: Math Marks
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Math Marks (0-100):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtMath = new JTextField(15); formPanel.add(txtMath, gbc);

        // Row 4: Java Marks
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(new JLabel("Java Marks (0-100):"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; txtJava = new JTextField(15); formPanel.add(txtJava, gbc);

        // Row 5: PHP Marks
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(new JLabel("PHP Marks (0-100):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; txtPHP = new JTextField(15); formPanel.add(txtPHP, gbc);

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("Add Student");
        btnEdit = new JButton("Edit Student");
        btnDelete = new JButton("Delete Student");
        btnClear = new JButton("Clear Form");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Add form and buttons to a top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        String[] columnNames = {"Reg Number", "Name", "Math", "Java", "PHP", "Average"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Summary Panel ---
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        lblOverallAvg = new JLabel("Overall Average: N/A");
        lblHighestAvgStudent = new JLabel("Highest Average Student: N/A");
        
        lblOverallAvg.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblHighestAvgStudent.setFont(new Font("SansSerif", Font.BOLD, 14));

        summaryPanel.add(lblOverallAvg);
        summaryPanel.add(lblHighestAvgStudent);
        add(summaryPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());
        
        // Simple, short, and clear explanation: Listener for table row selection to enable editing.
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                fillFormFromTable();
            }
        });
    }

    /**
     * Validates the marks input using a regular expression to ensure they are numbers between 0 and 100.
     * @param mark The mark string to validate.
     * @return true if valid, false otherwise.
     */
    private boolean validateMark(String mark) {
        // Simple, short, and clear explanation: Check if mark is a number between 0 and 100.
        if (mark == null || mark.trim().isEmpty()) {
            return false; // Required field validation
        }
        Matcher matcher = MARK_PATTERN.matcher(mark.trim());
        return matcher.matches();
    }

    /**
     * Validates all form inputs.
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean validateInputs() {
        // Simple, short, and clear explanation: Check all required fields and mark ranges.
        if (txtRegNumber.getText().trim().isEmpty() || txtName.getText().trim().isEmpty() ||
            txtMath.getText().trim().isEmpty() || txtJava.getText().trim().isEmpty() ||
            txtPHP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validateMark(txtMath.getText())) {
            JOptionPane.showMessageDialog(this, "Math Marks must be a number between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtMath.requestFocus();
            return false;
        }
        if (!validateMark(txtJava.getText())) {
            JOptionPane.showMessageDialog(this, "Java Marks must be a number between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtJava.requestFocus();
            return false;
        }
        if (!validateMark(txtPHP.getText())) {
            JOptionPane.showMessageDialog(this, "PHP Marks must be a number between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtPHP.requestFocus();
            return false;
        }
        
        return true;
    }

    /**
     * Loads all student data from the database and updates the JTable and summary labels.
     */
    private void loadStudentsData() {
        // Simple, short, and clear explanation: Refresh the table data from the database.
        tableModel.setRowCount(0); // Clear existing data
        try {
            List<Student> students = dbManager.getAllStudents();
            double totalAverage = 0;
            Student highestAvgStudent = null;
            
            for (Student student : students) {
                tableModel.addRow(student.toObjectArray());
                totalAverage += student.getAverageMarks();
                
                // Simple, short, and clear explanation: Find the student with the highest average.
                if (highestAvgStudent == null || student.getAverageMarks() > highestAvgStudent.getAverageMarks()) {
                    highestAvgStudent = student;
                }
            }

            // Update summary labels
            if (!students.isEmpty()) {
                double overallAverage = totalAverage / students.size();
                lblOverallAvg.setText(String.format("Overall Average: %.2f", overallAverage));
                lblHighestAvgStudent.setText(String.format("Highest Average Student: %s (%.2f)", 
                    highestAvgStudent.getName(), highestAvgStudent.getAverageMarks()));
            } else {
                lblOverallAvg.setText("Overall Average: N/A");
                lblHighestAvgStudent.setText("Highest Average Student: N/A");
            }

        } catch (SQLException ex) {
            // Simple, short, and clear explanation: Handle database errors.
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the Add Student button action.
     */
    private void addStudent() {
        // Simple, short, and clear explanation: Add a new student record.
        if (!validateInputs()) return;

        try {
            String regNumber = txtRegNumber.getText().trim();
            String name = txtName.getText().trim();
            int math = Integer.parseInt(txtMath.getText().trim());
            int java = Integer.parseInt(txtJava.getText().trim());
            int php = Integer.parseInt(txtPHP.getText().trim());

            Student newStudent = new Student(regNumber, name, math, java, php);
            dbManager.addStudent(newStudent);
            
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadStudentsData();

        } catch (NumberFormatException e) {
            // Simple, short, and clear explanation: Catch if marks are not valid numbers (though RegEx should prevent this).
            JOptionPane.showMessageDialog(this, "Marks must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            // Simple, short, and clear explanation: Catch database errors, e.g., duplicate primary key.
            if (e.getErrorCode() == 1062) { // MariaDB/MySQL error code for duplicate entry
                 JOptionPane.showMessageDialog(this, "Error: Student with this Registration Number already exists.", "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the Edit Student button action.
     */
    private void editStudent() {
        // Simple, short, and clear explanation: Update an existing student record.
        if (!validateInputs()) return;
        
        try {
            String regNumber = txtRegNumber.getText().trim();
            String name = txtName.getText().trim();
            int math = Integer.parseInt(txtMath.getText().trim());
            int java = Integer.parseInt(txtJava.getText().trim());
            int php = Integer.parseInt(txtPHP.getText().trim());

            Student updatedStudent = new Student(regNumber, name, math, java, php);
            dbManager.updateStudent(updatedStudent);
            
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadStudentsData();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Marks must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the Delete Student button action.
     */
    private void deleteStudent() {
        // Simple, short, and clear explanation: Delete the selected student record.
        String regNumber = txtRegNumber.getText().trim();
        if (regNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student " + regNumber + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbManager.deleteStudent(regNumber);
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadStudentsData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Fills the form fields with data from the selected row in the JTable.
     */
    private void fillFormFromTable() {
        // Simple, short, and clear explanation: Populate form fields from the selected table row.
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            txtRegNumber.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtMath.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtJava.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtPHP.setText(tableModel.getValueAt(selectedRow, 4).toString());
            
            // Disable Reg Number field during edit to prevent changing the primary key
            txtRegNumber.setEnabled(false);
        }
    }

    /**
     * Clears all form fields and re-enables the Reg Number field.
     */
    private void clearForm() {
        // Simple, short, and clear explanation: Reset all input fields.
        txtRegNumber.setText("");
        txtName.setText("");
        txtMath.setText("");
        txtJava.setText("");
        txtPHP.setText("");
        txtRegNumber.setEnabled(true);
        studentTable.clearSelection();
    }

    public static void main(String[] args) {
        // Simple, short, and clear explanation: Start the application on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            new StudentManagementGUI().setVisible(true);
        });
    }
}
