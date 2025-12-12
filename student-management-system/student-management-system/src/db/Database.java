package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // Your real MySQL database
    private static final String URL = 
        "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";     // change if your user is different
    private static final String PASS = "";         // put your MySQL password here

    static {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
