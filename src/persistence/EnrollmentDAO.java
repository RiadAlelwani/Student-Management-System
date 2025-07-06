package persistence;

import domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * فئة للوصول إلى بيانات التسجيلات في قاعدة البيانات
 */
public class EnrollmentDAO {
    private final Connection conn;

    public EnrollmentDAO(Connection conn) {
        this.conn = conn;
    }

    // إضافة تسجيل جديد
    public void add(Enrollment e) throws SQLException {
        String sql = "INSERT INTO enrollment (student_id, course_id, grade, semester_id, teacher_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getStudentId());
            stmt.setInt(2, e.getCourseId());
            stmt.setDouble(3, e.getGrade());
            stmt.setInt(4, e.getSemester().getId());
            stmt.setInt(5, e.getTeacher().getId());
            stmt.executeUpdate();
        }
    }

    // تحديث بيانات التسجيل
    public void update(Enrollment e) throws SQLException {
        String sql = "UPDATE enrollment SET grade = ?, semester_id = ?, teacher_id = ? WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, e.getGrade());
            stmt.setInt(2, e.getSemester().getId());
            stmt.setInt(3, e.getTeacher().getId());
            stmt.setInt(4, e.getStudentId());
            stmt.setInt(5, e.getCourseId());
            stmt.executeUpdate();
        }
    }

    // حذف تسجيل
    public void delete(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        }
    }

    // الحصول على جميع التسجيلات مع تفاصيلها
    public List<Enrollment> getAll() throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.season, s.year, s.is_open, t.id AS teacher_id, t.name AS teacher_name, " +
                     "t.email AS teacher_email, t.gender AS teacher_gender, t.age AS teacher_age, " +
                     "t.department_id, t.salary, st.name AS student_name, st.email AS student_email, " +
                     "st.gender AS student_gender, st.age AS student_age, st.major, st.gpa, " +
                     "c.name AS course_name, c.department_id AS course_department " +
                     "FROM enrollment e " +
                     "JOIN semester s ON e.semester_id = s.id " +
                     "JOIN teacher t ON e.teacher_id = t.id " +
                     "JOIN student st ON e.student_id = st.id " +
                     "JOIN course c ON e.course_id = c.id";

        try (Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapEnrollment(rs));
            }
        }
        return list;
    }

    // الحصول على تسجيل بواسطة معرف الطالب والمقرر
    public Enrollment getByIds(int studentId, int courseId) throws SQLException {
        String sql = "SELECT e.*, s.season, s.year, s.is_open, t.id AS teacher_id, t.name AS teacher_name, " +
                     "t.email AS teacher_email, t.gender AS teacher_gender, t.age AS teacher_age, " +
                     "t.department_id, t.salary, st.name AS student_name, st.email AS student_email, " +
                     "st.gender AS student_gender, st.age AS student_age, st.major, st.gpa, " +
                     "c.name AS course_name, c.department_id AS course_department " +
                     "FROM enrollment e " +
                     "JOIN semester s ON e.semester_id = s.id " +
                     "JOIN teacher t ON e.teacher_id = t.id " +
                     "JOIN student st ON e.student_id = st.id " +
                     "JOIN course c ON e.course_id = c.id " +
                     "WHERE e.student_id = ? AND e.course_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapEnrollment(rs);
            }
        }
        return null;
    }

    // تقرير: تسجيلات طالب معين بالاسم
    public List<Enrollment> getEnrollmentsByStudentName(String name) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.season, s.year, s.is_open, t.id AS teacher_id, t.name AS teacher_name, " +
                     "t.email AS teacher_email, t.gender AS teacher_gender, t.age AS teacher_age, " +
                     "t.department_id, t.salary, st.name AS student_name, st.email AS student_email, " +
                     "st.gender AS student_gender, st.age AS student_age, st.major, st.gpa, " +
                     "c.name AS course_name, c.department_id AS course_department " +
                     "FROM enrollment e " +
                     "JOIN semester s ON e.semester_id = s.id " +
                     "JOIN teacher t ON e.teacher_id = t.id " +
                     "JOIN student st ON e.student_id = st.id " +
                     "JOIN course c ON e.course_id = c.id " +
                     "WHERE st.name LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEnrollment(rs));
                }
            }
        }
        return list;
    }

    // دالة مساعدة لإنشاء كائن تسجيل من ResultSet
    private Enrollment mapEnrollment(ResultSet rs) throws SQLException {
        try {
            // التحقق من القيم الأساسية قبل إنشاء الكائنات
            String studentName = rs.getString("student_name");
            String studentEmail = rs.getString("student_email");

            if (studentName == null || studentName.isBlank()) {
                System.err.println("⚠️ Skipping record: student_name is null or blank");
                return null;
            }

            if (studentEmail == null || studentEmail.isBlank()) {
                System.err.println("⚠️ Skipping record: student_email is null or blank");
                return null;
            }

            Semester semester = new Semester(
                rs.getInt("semester_id"),
                rs.getString("season"),
                rs.getInt("year"),
                rs.getBoolean("is_open")
            );

            Teacher teacher = new Teacher(
                rs.getInt("teacher_id"),
                rs.getString("teacher_name"),
                rs.getString("teacher_email"),
                rs.getString("teacher_gender"),
                rs.getInt("teacher_age"),
                rs.getInt("department_id"),
                rs.getDouble("salary")
            );

            Student student = new Student(
                rs.getInt("student_id"),
                studentName,
                studentEmail,
                rs.getString("student_gender"),
                rs.getInt("student_age"),
                rs.getString("major"),
                rs.getDouble("gpa")
            );

            Course course = new Course();
            course.setId(rs.getInt("course_id"));
            course.setName(rs.getString("course_name"));
            course.setDepartmentId(rs.getInt("course_department"));

            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setGrade(rs.getDouble("grade"));
            enrollment.setSemester(semester);
            enrollment.setTeacher(teacher);

            return enrollment;

        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Skipping record due to invalid data: " + e.getMessage());
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("❌ Failed to map enrollment from result set", ex);
        }
    }

    public List<Enrollment> getEnrollmentsByCourseAndSemester(String courseName, String season, int year) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.*, s.season, s.year, s.is_open, t.id AS teacher_id, t.name AS teacher_name, " +
                     "t.email AS teacher_email, t.gender AS teacher_gender, t.age AS teacher_age, " +
                     "t.department_id, t.salary, st.name AS student_name, st.email AS student_email, " +
                     "st.gender AS student_gender, st.age AS student_age, st.major, st.gpa, " +
                     "c.name AS course_name, c.department_id AS course_department " +
                     "FROM enrollment e " +
                     "JOIN semester s ON e.semester_id = s.id " +
                     "JOIN teacher t ON e.teacher_id = t.id " +
                     "JOIN student st ON e.student_id = st.id " +
                     "JOIN course c ON e.course_id = c.id " +
                     "WHERE c.name LIKE ? AND s.season = ? AND s.year = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + courseName + "%");
            stmt.setString(2, season);
            stmt.setInt(3, year);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEnrollment(rs));
                }
            }
        }
        return list;
    }
}
