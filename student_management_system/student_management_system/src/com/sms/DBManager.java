package com.sms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: Student Management System
 * My names : Aime Thierry Byiringiro
 * Reg No: 24rp07221
 * Description of the page: Manages database connection and all CRUD operations for the Student table.
 */
public class DBManager {
	//the following is connecting to the db so that all the operations can work
    private static final String URL = "jdbc:mariadb://localhost:3306/StudentManagementDB";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    //here I am providing all the queries needed to apply CRUD operations
    private static final String INSERT_STUDENT = "INSERT INTO Students (reg_number, name, math_marks, java_marks, php_marks) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_STUDENT = "UPDATE Students SET name = ?, math_marks = ?, java_marks = ?, php_marks = ? WHERE reg_number = ?";
    private static final String DELETE_STUDENT = "DELETE FROM Students WHERE reg_number = ?";
    private static final String SELECT_ALL_STUDENTS = "SELECT * FROM Students";

    /**
     * Establishes a connection to the MariaDB database.
     * @return A valid Connection object.
     * @throws SQLException if a database access error occurs.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Adds a new student record to the database.
     * @param student The Student object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addStudent(Student student) throws SQLException {
        // using try and catch to insert new records .
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_STUDENT)) {
            ps.setString(1, student.getRegNumber());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getMathMarks());
            ps.setInt(4, student.getJavaMarks());
            ps.setInt(5, student.getPhpMarks());
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing student record in the database.
     * @param student The Student object with updated details.
     * @throws SQLException if a database access error occurs.
     */
    public void updateStudent(Student student) throws SQLException {
        // Updating the existing data but also following the validations.
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STUDENT)) {
            ps.setString(1, student.getName());
            ps.setInt(2, student.getMathMarks());
            ps.setInt(3, student.getJavaMarks());
            ps.setInt(4, student.getPhpMarks());
            ps.setString(5, student.getRegNumber());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a student record from the database.
     * @param regNumber The registration number of the student to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteStudent(String regNumber) throws SQLException {
        // Deleting a student record from the database but using reg_no as it is the primary key because it identifies a student unique to another student;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_STUDENT)) {
            ps.setString(1, regNumber);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all student records from the database.
     * @return A list of Student objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<Student> getAllStudents() throws SQLException {
       // Get all students from the database.
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_STUDENTS)) {

            while (rs.next()) {
                String regNumber = rs.getString("reg_number");
                String name = rs.getString("name");
                int mathMarks = rs.getInt("math_marks");
                int javaMarks = rs.getInt("java_marks");
                int phpMarks = rs.getInt("php_marks");

                students.add(new Student(regNumber, name, mathMarks, javaMarks, phpMarks));
            }
        }
        return students;
    }
}
