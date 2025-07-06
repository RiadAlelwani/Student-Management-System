package application;

import domain.Admin;
import persistence.AdminDAO;
import java.sql.Connection;

/**
 * خدمة إدارة عمليات المسؤول (Admin).
 */
public class AdminService {
    private final AdminDAO dao;

    /**
     * منشئ الخدمة مع تمرير اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات
     */
    public AdminService(Connection conn) {
        this.dao = new AdminDAO(conn);
    }

    /**
     * محاولة تسجيل دخول المسؤول بالمستخدم وكلمة المرور.
     * @param username اسم المستخدم
     * @param password كلمة المرور
     * @return كائن Admin عند نجاح المصادقة
     * @throws Exception إذا فشل التحقق أو حدث خطأ أثناء الوصول إلى البيانات
     */
    public Admin login(String username, String password) throws Exception {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // تفويض المصادقة إلى DAO
        return dao.authenticate(username, password);
    }
}
