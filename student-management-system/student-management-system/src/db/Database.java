package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASS = ""; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
