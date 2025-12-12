package service;

import dao.StudentDAO;
import model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    // Basic regex for email validation
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public StudentService() {
        dao.createTableIfNotExists();
    }

    public void addStudent(String firstName, String lastName, String email, String ageStr) throws IllegalArgumentException, SQLException {
        validate(firstName, lastName, email, ageStr);
        int age = Integer.parseInt(ageStr);
        Student s = new Student(firstName.trim(), lastName.trim(), email.trim(), age);
        dao.insert(s);
    }

    public List<Student> listAll() throws SQLException {
        return dao.findAll();
    }

    public void updateStudent(Integer id, String firstName, String lastName, String email, String ageStr) throws IllegalArgumentException, SQLException {
        if (id == null) throw new IllegalArgumentException("Select a student to update.");
        validate(firstName, lastName, email, ageStr);
        int age = Integer.parseInt(ageStr);
        Student s = new Student(id, firstName.trim(), lastName.trim(), email.trim(), age);
        dao.update(s);
    }

    public void deleteStudent(Integer id) throws SQLException {
        if (id == null) throw new IllegalArgumentException("Select a student to delete.");
        dao.delete(id);
    }

    private void validate(String firstName, String lastName, String email, String ageStr) throws IllegalArgumentException {
        if (firstName == null || firstName.trim().isEmpty()) throw new IllegalArgumentException("First name required.");
        if (lastName == null || lastName.trim().isEmpty()) throw new IllegalArgumentException("Last name required.");
        if (email == null || !EMAIL_REGEX.matcher(email.trim()).matches()) throw new IllegalArgumentException("Invalid email.");
        if (ageStr == null || !ageStr.matches("\\d+")) throw new IllegalArgumentException("Age must be a number.");
        int age = Integer.parseInt(ageStr);
        if (age < 1 || age > 120) throw new IllegalArgumentException("Age must be between 1 and 120.");
    }
}
