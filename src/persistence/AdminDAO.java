package persistence;

import domain.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة DAO (Data Access Object) للتعامل مع بيانات المسؤولين (Admin)
 * في قاعدة البيانات عبر استعلامات SQL.
 */
public class AdminDAO {
    private final Connection conn;

    /**
     * إنشاء كائن AdminDAO مع تمرير اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات النشط
     */
    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * إضافة مسؤول جديد إلى قاعدة البيانات.
     * @param admin كائن Admin يحتوي بيانات المسؤول الجديد
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
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

    /**
     * التحقق مما إذا كان اسم المستخدم موجودًا في قاعدة البيانات.
     * @param username اسم المستخدم للتحقق منه
     * @return true إذا كان اسم المستخدم موجودًا، false خلاف ذلك
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    /**
     * مصادقة المسؤول عبر اسم المستخدم وكلمة المرور.
     * @param username اسم المستخدم
     * @param password كلمة المرور
     * @return كائن Admin إذا تم التحقق بنجاح، أو null إذا فشل التحقق
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
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

    /**
     * تحديث بيانات مسؤول موجود في قاعدة البيانات.
     * @param admin كائن Admin يحتوي البيانات المحدثة (يجب أن يكون الاسم المستخدم موجودًا)
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
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

    /**
     * حذف مسؤول من قاعدة البيانات بواسطة اسم المستخدم.
     * @param username اسم المستخدم الخاص بالمسؤول المراد حذفه
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
    public void delete(String username) throws SQLException {
        String sql = "DELETE FROM admin WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }

    /**
     * جلب جميع المسؤولين من قاعدة البيانات.
     * @return قائمة تحتوي جميع المسؤولين
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
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

    /**
     * جلب مسؤول بواسطة اسم المستخدم.
     * @param username اسم المستخدم للمسؤول المطلوب
     * @return كائن Admin إذا وجد، أو null إذا لم يوجد
     * @throws SQLException في حال حدوث خطأ في استعلام SQL
     */
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
