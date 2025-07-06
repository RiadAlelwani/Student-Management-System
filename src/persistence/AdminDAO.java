package persistence;

import domain.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات المسؤولين في قاعدة البيانات
 */
public class AdminDAO {
    private final Connection conn;

    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    // إضافة مسؤول جديد
    public void insert(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin (name, email, gender, age, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getGender());
            stmt.setInt(4, admin.getAge());
            stmt.setString(5, admin.getUsername());
            stmt.setString(6, admin.getPassword());
            stmt.executeUpdate();
        }
    }

    // التحقق من وجود اسم مستخدم
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // مصادقة المسؤول
    public Admin authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        }
        return null;
    }

    // تحديث بيانات المسؤول
    public void update(Admin admin) throws SQLException {
        String sql = "UPDATE admin SET name = ?, email = ?, gender = ?, age = ?, password = ? WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getGender());
            stmt.setInt(4, admin.getAge());
            stmt.setString(5, admin.getPassword());
            stmt.setString(6, admin.getUsername());
            stmt.executeUpdate();
        }
    }

    // حذف مسؤول
    public void delete(String username) throws SQLException {
        String sql = "DELETE FROM admin WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    // الحصول على جميع المسؤولين
    public List<Admin> getAll() throws SQLException {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM admin";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Admin(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        }
        return list;
    }

    // الحصول على مسؤول بواسطة اسم المستخدم
    public Admin getByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        }
        return null;
    }
}