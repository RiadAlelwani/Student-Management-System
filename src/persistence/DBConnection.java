package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * فئة لإدارة اتصالات قاعدة البيانات
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student_mgmt";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // الحصول على اتصال بقاعدة البيانات
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}