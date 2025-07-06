package persistence;

import domain.Teacher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات المعلمين في قاعدة البيانات.
 */
public class TeacherDAO {
    private final Connection conn;

    /**
     * منشئ الفئة يأخذ اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات المفتوح
     */
    public TeacherDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * إضافة معلم جديد إلى قاعدة البيانات.
     * @param t كائن المعلم الذي يحتوي على البيانات المراد إضافتها
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void addTeacher(Teacher t) throws SQLException {
        String sql = "INSERT INTO teacher (name, email, gender, age, department_id, salary) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getGender());
            ps.setInt(4, t.getAge());
            ps.setInt(5, t.getDepartmentId());
            ps.setDouble(6, t.getSalary());
            ps.executeUpdate();
        }
    }

    /**
     * الحصول على قائمة بجميع المعلمين مع أسماء الأقسام الخاصة بهم.
     * @return قائمة تحتوي على جميع كائنات المعلمين
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public List<Teacher> getAll() throws SQLException {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT t.*, d.name AS department_name FROM teacher t JOIN department d ON t.department_id = d.id";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                double salary = parseSalary(rs.getString("salary"));
                Teacher t = new Teacher(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getInt("department_id"),
                    salary
                );
                t.setDepartmentName(rs.getString("department_name") != null ? rs.getString("department_name") : "N/A");
                list.add(t);
            }
        }
        return list;
    }

    /**
     * تحديث بيانات معلم موجود في قاعدة البيانات.
     * @param t كائن المعلم الذي يحتوي على البيانات الجديدة (يجب أن يحتوي على المعرف)
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void updateTeacher(Teacher t) throws SQLException {
        String sql = "UPDATE teacher SET name = ?, email = ?, gender = ?, age = ?, department_id = ?, salary = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getGender());
            ps.setInt(4, t.getAge());
            ps.setInt(5, t.getDepartmentId());
            ps.setDouble(6, t.getSalary());
            ps.setInt(7, t.getId());
            ps.executeUpdate();
        }
    }

    /**
     * حذف معلم من قاعدة البيانات بواسطة المعرف.
     * @param id معرف المعلم المراد حذفه
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void deleteTeacher(int id) throws SQLException {
        String sql = "DELETE FROM teacher WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * البحث عن معلمين حسب اسم جزئي (باستخدام LIKE) مع جلب أسماء الأقسام.
     * @param name جزء من اسم المعلم للبحث به
     * @return قائمة المعلمين الذين تحتوي أسماؤهم على النص المحدد مع بيانات القسم
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public List<Teacher> searchByName(String name) throws SQLException {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT t.*, d.name AS department_name FROM teacher t JOIN department d ON t.department_id = d.id WHERE t.name LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double salary = parseSalary(rs.getString("salary"));
                    Teacher t = new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getInt("department_id"),
                        salary
                    );
                    t.setDepartmentName(rs.getString("department_name") != null ? rs.getString("department_name") : "N/A");
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * دالة خاصة لتحويل قيمة الراتب من نص قد يحتوي على فاصلة عشرية إلى رقم عشري.
     * @param salaryStr نص الراتب المحتمل أن يحتوي على فاصلة بدلاً من نقطة
     * @return قيمة الراتب كرقم عشري (double)، أو 0.0 إذا تعذر التحويل
     */
    private double parseSalary(String salaryStr) {
        if (salaryStr == null || salaryStr.isEmpty()) return 0.0;
        try {
            salaryStr = salaryStr.replace(",", ".");
            return Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            System.err.println("⚠️ Failed to parse salary: " + salaryStr);
            return 0.0;
        }
    }
}
