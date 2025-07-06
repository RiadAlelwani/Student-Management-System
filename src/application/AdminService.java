package application;

import domain.Admin;
import persistence.AdminDAO;
import java.sql.Connection;

public class AdminService {
    private final AdminDAO dao;

    // إنشاء خدمة المسؤول مع اتصال قاعدة البيانات
    public AdminService(Connection conn) {
        this.dao = new AdminDAO(conn);
    }

    // تسجيل دخول المسؤول
    public Admin login(String username, String password) throws Exception {
        // التحقق من إدخال اسم المستخدم وكلمة المرور
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username is required");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password is required");

        // مصادقة المسؤول عبر DAO
        return dao.authenticate(username, password);
    }
}