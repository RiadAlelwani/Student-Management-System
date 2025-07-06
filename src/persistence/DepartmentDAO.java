package persistence;

import domain.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات الأقسام في قاعدة البيانات.
 * توفر عمليات الاستعلام عن جميع الأقسام أو قسم معين بواسطة المعرف.
 */
public class DepartmentDAO {
    private final Connection conn;

    /**
     * إنشاء DAO مع ربط الاتصال بقاعدة البيانات.
     * @param conn اتصال قاعدة البيانات المفتوح
     */
    public DepartmentDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * جلب قائمة جميع الأقسام من قاعدة البيانات.
     * @return قائمة تحتوي على كل الكائنات Department
     * @throws SQLException في حالة حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public List<Department> getAll() throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM department";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }

    /**
     * الحصول على قسم معين من قاعدة البيانات بواسطة معرفه.
     * @param id المعرف الفريد للقسم
     * @return كائن Department إذا تم العثور عليه، أو null إذا لم يوجد
     * @throws SQLException في حالة حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public Department getById(int id) throws SQLException {
        String sql = "SELECT * FROM department WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Department(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null; // إذا لم يتم العثور على القسم
    }
}
