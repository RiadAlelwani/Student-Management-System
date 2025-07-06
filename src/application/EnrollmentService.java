package application;

import persistence.EnrollmentDAO;
import domain.Enrollment;
import java.sql.Connection;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;

    // إنشاء خدمة التسجيل مع اتصال قاعدة البيانات
    public EnrollmentService(Connection conn) {
        this.enrollmentDAO = new EnrollmentDAO(conn);
    }

    // إضافة تسجيل جديد للطالب في مقرر
    public void add(Enrollment e) throws Exception {
        // التحقق من عدم تسجيل الطالب مسبقًا في نفس المقرر
        Enrollment existing = enrollmentDAO.getByIds(e.getStudentId(), e.getCourseId());
        if (existing != null) {
            throw new Exception("Student is already enrolled in this course.");
        }
        enrollmentDAO.add(e);
    }

    // تحديث معلومات التسجيل
    public void update(Enrollment e) throws Exception {
        enrollmentDAO.update(e);
    }

    // حذف تسجيل الطالب من مقرر
    public void delete(int studentId, int courseId) throws Exception {
        enrollmentDAO.delete(studentId, courseId);
    }

    // الحصول على جميع عمليات التسجيل
    public List<Enrollment> getAll() throws Exception {
        return enrollmentDAO.getAll();
    }

    // الحصول على تسجيل بواسطة معرف الطالب والمقرر
    public Enrollment getByIds(int studentId, int courseId) throws Exception {
        return enrollmentDAO.getByIds(studentId, courseId);
    }
}