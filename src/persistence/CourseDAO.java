package persistence;

import domain.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة DAO للتعامل مع جدول المقررات الدراسية (course) في قاعدة البيانات.
 */
public class CourseDAO {
    private final Connection conn;

    /**
     * إنشاء كائن CourseDAO مع تمرير اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات
     */
    public CourseDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * إضافة مقرر جديد إلى قاعدة البيانات.
     * @param course كائن Course يحتوي بيانات المقرر الجديد
     * @throws SQLException في حال حدوث خطأ في تنفيذ الاستعلام
     */
    public void add(Course course) throws SQLException {
        String sql = "INSERT INTO course (name, description, credits, teacher_id, department_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, course.getTeacherId());
            stmt.setInt(5, course.getDepartmentId());
            stmt.executeUpdate();
        }
    }

    /**
     * تحديث بيانات مقرر موجود في قاعدة البيانات.
     * @param course كائن Course يحتوي البيانات المحدثة (يجب أن يحتوي على معرف id صحيح)
     * @throws SQLException في حال حدوث خطأ في تنفيذ الاستعلام
     */
    public void update(Course course) throws SQLException {
        String sql = "UPDATE course SET name=?, description=?, credits=?, teacher_id=?, department_id=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, course.getTeacherId());
            stmt.setInt(5, course.getDepartmentId());
            stmt.setInt(6, course.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * حذف مقرر دراسي من قاعدة البيانات بواسطة معرفه.
     * @param id معرف المقرر المراد حذفه
     * @throws SQLException في حال حدوث خطأ في تنفيذ الاستعلام
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM course WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * جلب جميع المقررات الدراسية مع أسماء المعلمين والأقسام المرتبطة بها.
     * @return قائمة بجميع المقررات
     * @throws SQLException في حال حدوث خطأ في تنفيذ الاستعلام
     */
    public List<Course> getAll() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.*, t.name AS teacher_name, d.name AS department_name " +
                     "FROM course c " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "LEFT JOIN department d ON c.department_id = d.id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course c = new Course(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("credits"),
                    rs.getInt("teacher_id"),
                    rs.getInt("department_id")
                );
                c.setTeacherName(rs.getString("teacher_name"));
                c.setDepartmentName(rs.getString("department_name"));
                list.add(c);
            }
        }
        return list;
    }

    /**
     * جلب مقرر دراسي بواسطة معرفه مع أسماء المعلم والقسم المرتبطين.
     * @param id معرف المقرر
     * @return كائن Course إذا وجد، أو null إذا لم يوجد
     * @throws SQLException في حال حدوث خطأ في تنفيذ الاستعلام
     */
    public Course getById(int id) throws SQLException {
        String sql = "SELECT c.*, t.name AS teacher_name, d.name AS department_name " +
                     "FROM course c " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "LEFT JOIN department d ON c.department_id = d.id " +
                     "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Course c = new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits"),
                        rs.getInt("teacher_id"),
                        rs.getInt("department_id")
                    );
                    c.setTeacherName(rs.getString("teacher_name"));
                    c.setDepartmentName(rs.getString("department_name"));
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * جلب قائمة المقررات التي يدرسها معلم معين بواسطة معرفه.
     * @param teacherId معرف المعلم
     * @return قائمة بالمقررات التي يدرسها المعلم
     * @throws SQLException في حال حدوث خطأ في تنفيذ الاستعلام
     */
    public List<Course> findByTeacherId(int teacherId) throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.*, t.name AS teacher_name, d.name AS department_name " +
                     "FROM course c " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "LEFT JOIN department d ON c.department_id = d.id " +
                     "WHERE c.teacher_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Course c = new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits"),
                        rs.getInt("teacher_id"),
                        rs.getInt("department_id")
                    );
                    c.setTeacherName(rs.getString("teacher_name"));
                    c.setDepartmentName(rs.getString("department_name"));
                    list.add(c);
                }
            }
        }
        return list;
    }
}
