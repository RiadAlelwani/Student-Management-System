package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * فئة لإدارة اتصالات قاعدة البيانات.
 * تحتوي على معلومات الاتصال وتوفير اتصال جديد عند الطلب.
 */
public class DBConnection {
    // عنوان URL لقاعدة البيانات (مع اسم قاعدة البيانات student_mgmt)
    private static final String URL = "jdbc:mysql://localhost:3306/student_mgmt";

    // اسم المستخدم للاتصال بقاعدة البيانات
    private static final String USER = "root";

    // كلمة المرور للاتصال بقاعدة البيانات
    private static final String PASSWORD = "";

    /**
     * إنشاء وفتح اتصال جديد بقاعدة البيانات.
     * @return كائن Connection يمثل الاتصال المفتوح
     * @throws SQLException في حال فشل الاتصال بقاعدة البيانات
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
