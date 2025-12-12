package dao;

import model.Student;
import db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // REMOVE table creation — you already have table "students" in MySQL
    public void createTableIfNotExists() {
        // Empty on purpose or delete this method completely
    }

    public void insert(Student st) throws SQLException {
        String sql = "INSERT INTO students(first_name,last_name,email,age) VALUES(?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, st.getFirstName());
            ps.setString(2, st.getLastName());
            ps.setString(3, st.getEmail());
            ps.setInt(4, st.getAge());
            ps.executeUpdate();
        }
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT id, first_name, last_name, email, age FROM students";
        List<Student> list = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("age")
                ));
            }
        }
        return list;
    }

    public void update(Student st) throws SQLException {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, age=? WHERE id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, st.getFirstName());
            ps.setString(2, st.getLastName());
            ps.setString(3, st.getEmail());
            ps.setInt(4, st.getAge());
            ps.setInt(5, st.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
