package persistence;

import domain.Semester;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات الفصول الدراسية في قاعدة البيانات.
 * توفر عمليات إضافة، تحديث، حذف، واستعلام عن الفصول الدراسية.
 */
public class SemesterDAO {
    private final Connection conn;

    /**
     * إنشاء DAO مع ربط الاتصال بقاعدة البيانات.
     * @param conn اتصال قاعدة البيانات المفتوح
     */
    public SemesterDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * إضافة فصل دراسي جديد إلى قاعدة البيانات.
     * @param semester كائن الفصل الدراسي الجديد
     * @throws SQLException في حالة حدوث خطأ أثناء التنفيذ
     */
    public void add(Semester semester) throws SQLException {
        String sql = "INSERT INTO semester (season, year, is_open) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, semester.getSeason());
            stmt.setInt(2, semester.getYear());
            stmt.setBoolean(3, semester.isOpen());
            stmt.executeUpdate();
        }
    }

    /**
     * تحديث بيانات فصل دراسي موجود.
     * @param semester كائن الفصل الدراسي مع البيانات المحدثة
     * @throws SQLException في حالة حدوث خطأ أثناء التنفيذ
     */
    public void update(Semester semester) throws SQLException {
        String sql = "UPDATE semester SET season = ?, year = ?, is_open = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, semester.getSeason());
            stmt.setInt(2, semester.getYear());
            stmt.setBoolean(3, semester.isOpen());
            stmt.setInt(4, semester.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * حذف فصل دراسي من قاعدة البيانات بناءً على المعرف.
     * @param id معرف الفصل الدراسي
     * @throws SQLException في حالة حدوث خطأ أثناء التنفيذ
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM semester WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * الحصول على جميع الفصول الدراسية.
     * @return قائمة تحتوي على جميع الفصول الدراسية المخزنة
     * @throws SQLException في حالة حدوث خطأ أثناء التنفيذ
     */
    public List<Semester> getAll() throws SQLException {
        List<Semester> list = new ArrayList<>();
        String sql = "SELECT * FROM semester";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Semester(
                    rs.getInt("id"),
                    rs.getString("season"),
                    rs.getInt("year"),
                    rs.getBoolean("is_open")
                ));
            }
        }
        return list;
    }

    /**
     * الحصول على فصل دراسي بواسطة معرفه.
     * @param id معرف الفصل الدراسي
     * @return كائن الفصل الدراسي أو null إذا لم يتم العثور عليه
     * @throws SQLException في حالة حدوث خطأ أثناء التنفيذ
     */
    public Semester getById(int id) throws SQLException {
        String sql = "SELECT * FROM semester WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Semester(
                        rs.getInt("id"),
                        rs.getString("season"),
                        rs.getInt("year"),
                        rs.getBoolean("is_open")
                    );
                }
            }
        }
        return null;
    }

    /**
     * البحث عن فصول دراسية حسب اسم الفصل (season) جزئياً.
     * @param name نص البحث في اسم الفصل الدراسي
     * @return قائمة الفصول التي تطابق النص البحثي جزئياً
     * @throws SQLException في حالة حدوث خطأ أثناء التنفيذ
     */
    public List<Semester> searchByName(String name) throws SQLException {
        List<Semester> list = new ArrayList<>();
        String sql = "SELECT * FROM semester WHERE season LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Semester(
                        rs.getInt("id"),
                        rs.getString("season"),
                        rs.getInt("year"),
                        rs.getBoolean("is_open")
                    ));
                }
            }
        }
        return list;
    }
}
