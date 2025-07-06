package persistence;

import domain.*;
import domain.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات الطلاب في قاعدة البيانات
 */
public class StudentDAO {
    private final Connection conn;

    /**
     * منشئ الفئة يأخذ اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات المفتوح
     */
    public StudentDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * إضافة طالب جديد إلى قاعدة البيانات.
     * @param s كائن الطالب الذي يحتوي على البيانات المراد إضافتها
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO student (name, email, gender, age, major, gpa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getName());
            stmt.setString(2, s.getEmail());
            stmt.setString(3, s.getGender());
            stmt.setInt(4, s.getAge());
            stmt.setString(5, s.getMajor());
            stmt.setDouble(6, s.getGpa());
            stmt.executeUpdate();
        }
    }

    /**
     * الحصول على قائمة بجميع الطلاب المسجلين في قاعدة البيانات.
     * @return قائمة تحتوي على جميع كائنات الطلاب
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public List<Student> getAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("major"),
                    rs.getDouble("gpa")
                ));
            }
        }
        return list;
    }

    /**
     * تحديث بيانات طالب موجود في قاعدة البيانات.
     * @param s كائن الطالب الذي يحتوي على البيانات الجديدة (يجب أن يحتوي على المعرف)
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE student SET name=?, email=?, gender=?, age=?, major=?, gpa=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getName());
            stmt.setString(2, s.getEmail());
            stmt.setString(3, s.getGender());
            stmt.setInt(4, s.getAge());
            stmt.setString(5, s.getMajor());
            stmt.setDouble(6, s.getGpa());
            stmt.setInt(7, s.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * حذف طالب من قاعدة البيانات بواسطة المعرف.
     * @param id معرف الطالب المراد حذفه
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * البحث عن طلاب حسب اسم جزئي (باستخدام LIKE).
     * @param name جزء من اسم الطالب للبحث به
     * @return قائمة الطلاب الذين تحتوي أسماؤهم على النص المحدد
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public List<Student> searchByName(String name) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("major"),
                    rs.getDouble("gpa")
                ));
            }
        }
        return list;
    }

    /**
     * الحصول على بيانات طالب بواسطة معرفه.
     * @param id معرف الطالب
     * @return كائن الطالب إذا وجد، أو null إذا لم يتم العثور عليه
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getInt("age"),
                    rs.getString("major"),
                    rs.getDouble("gpa")
                );
            }
        }
        return null;
    }

    /**
     * تحديث المعدل التراكمي (GPA) لطالب معين.
     * @param studentId معرف الطالب المراد تحديث معدله
     * @param gpa المعدل الجديد المراد تعيينه
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public void updateGPA(int studentId, double gpa) throws SQLException {
        String sql = "UPDATE student SET gpa = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, gpa);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
        }
    }

    /**
     * جلب تسجيلات طالب معين في فصل دراسي محدد حسب اسم الطالب، الفصل، والسنة.
     * @param studentName اسم الطالب (يمكن أن يكون جزئي)
     * @param season اسم الفصل الدراسي (مثل "Fall", "Spring")
     * @param year سنة الفصل الدراسي
     * @return قائمة التسجيلات التي تطابق المعايير مع بيانات الطالب، المادة، المعلم، الفصل والدرجة
     * @throws SQLException في حال حدوث خطأ أثناء تنفيذ الاستعلام
     */
    public List<Enrollment> getEnrollmentsByStudentAndSemester(String studentName, String season, int year) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = """
            SELECT e.*, s.name AS student_name, c.name AS course_name, c.credits, 
                   t.name AS teacher_name, sem.season, sem.year
            FROM enrollment e
            JOIN student s ON e.student_id = s.id
            JOIN course c ON e.course_id = c.id
            JOIN teacher t ON e.teacher_id = t.id
            JOIN semester sem ON e.semester_id = sem.id
            WHERE s.name LIKE ? AND sem.season = ? AND sem.year = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + studentName + "%");
            ps.setString(2, season);
            ps.setInt(3, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setGrade(rs.getDouble("grade"));

                    Student student = new Student();
                    student.setName(rs.getString("student_name"));
                    e.setStudent(student);

                    Course course = new Course();
                    course.setName(rs.getString("course_name"));
                    course.setCredits(rs.getInt("credits"));
                    e.setCourse(course);

                    Teacher teacher = new Teacher();
                    teacher.setName(rs.getString("teacher_name"));
                    e.setTeacher(teacher);

                    Semester sem = new Semester();
                    sem.setSeason(rs.getString("season"));
                    sem.setYear(rs.getInt("year"));
                    e.setSemester(sem);

                    list.add(e);
                }
            }
        }
        return list;
    }

}
