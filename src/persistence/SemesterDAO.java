package persistence;

import domain.Semester;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات الفصول الدراسية في قاعدة البيانات
 */
public class SemesterDAO {
    private final Connection conn;

    public SemesterDAO(Connection conn) {
        this.conn = conn;
    }

    // إضافة فصل دراسي جديد
    public void add(Semester semester) throws SQLException {
        String sql = "INSERT INTO semester (season, year, is_open) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, semester.getSeason());
            stmt.setInt(2, semester.getYear());
            stmt.setBoolean(3, semester.isOpen());
            stmt.executeUpdate();
        }
    }

    // تحديث بيانات الفصل الدراسي
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

    // حذف فصل دراسي
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM semester WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // الحصول على جميع الفصول الدراسية
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

    // الحصول على فصل دراسي بواسطة المعرف
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

    // البحث عن فصول دراسية بالاسم
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