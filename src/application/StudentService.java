package application;

import persistence.StudentDAO;
import persistence.EnrollmentDAO;
import domain.Student;
import domain.Enrollment;

import java.sql.Connection;
import java.util.List;

/**
 * خدمة لإدارة بيانات الطلاب والتسجيلات الخاصة بهم.
 */
public class StudentService {
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;

    /**
     * إنشاء الخدمة باستخدام اتصال قاعدة البيانات.
     */
    public StudentService(Connection conn) {
        this.studentDAO = new StudentDAO(conn);
        this.enrollmentDAO = new EnrollmentDAO(conn);
    }

    /**
     * إضافة طالب جديد.
     */
    public void add(Student student) throws Exception {
        studentDAO.addStudent(student);
    }

    /**
     * تحديث بيانات طالب موجود.
     */
    public void update(Student student) throws Exception {
        studentDAO.updateStudent(student);
    }

    /**
     * حذف طالب بواسطة المعرف.
     */
    public void delete(int id) throws Exception {
        studentDAO.deleteStudent(id);
    }

    /**
     * البحث عن طلاب بالاسم.
     */
    public List<Student> searchByName(String name) throws Exception {
        return studentDAO.searchByName(name);
    }

    /**
     * جلب جميع الطلاب.
     */
    public List<Student> getAll() throws Exception {
        return studentDAO.getAll();
    }

    /**
     * الحصول على تسجيلات الطالب حسب اسمه.
     */
    public List<Enrollment> getEnrollmentsByStudentName(String name) throws Exception {
        return enrollmentDAO.getEnrollmentsByStudentName(name);
    }

    /**
     * الحصول على تسجيلات الطلاب في مقرر معين وفصل دراسي محدد.
     */
    public List<Enrollment> getEnrollmentsByCourseAndSemester(String courseName, String season, int year) throws Exception {
        return enrollmentDAO.getEnrollmentsByCourseAndSemester(courseName, season, year);
    }

    /**
     * الحصول على تسجيلات طالب معين في فصل دراسي محدد.
     */
    public List<Enrollment> getEnrollmentsByStudentAndSemester(String studentName, String season, int year) throws Exception {
        return studentDAO.getEnrollmentsByStudentAndSemester(studentName, season, year);
    }
}
