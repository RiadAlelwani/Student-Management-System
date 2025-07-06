package application;

import persistence.EnrollmentDAO;
import domain.Enrollment;
import java.sql.Connection;
import java.util.List;

/**
 * خدمة لإدارة تسجيلات الطلاب في المقررات.
 */
public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;

    /**
     * إنشاء خدمة التسجيل بتمرير اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات
     */
    public EnrollmentService(Connection conn) {
        this.enrollmentDAO = new EnrollmentDAO(conn);
    }

    /**
     * إضافة تسجيل جديد.
     * @param e التسجيل الجديد
     * @throws Exception إذا كان الطالب مسجلًا مسبقًا في نفس المقرر
     */
    public void add(Enrollment e) throws Exception {
        Enrollment existing = enrollmentDAO.getByIds(e.getStudentId(), e.getCourseId());
        if (existing != null) {
            throw new Exception("Student is already enrolled in this course.");
        }
        enrollmentDAO.add(e);
    }

    /**
     * تحديث تسجيل موجود.
     * @param e التسجيل بعد التعديل
     * @throws Exception في حالة خطأ
     */
    public void update(Enrollment e) throws Exception {
        enrollmentDAO.update(e);
    }

    /**
     * حذف تسجيل الطالب من مقرر معين.
     * @param studentId معرف الطالب
     * @param courseId معرف المقرر
     * @throws Exception في حالة خطأ
     */
    public void delete(int studentId, int courseId) throws Exception {
        enrollmentDAO.delete(studentId, courseId);
    }

    /**
     * جلب كل التسجيلات.
     * @return قائمة التسجيلات
     * @throws Exception في حالة خطأ
     */
    public List<Enrollment> getAll() throws Exception {
        return enrollmentDAO.getAll();
    }

    /**
     * جلب تسجيل معين بواسطة معرف الطالب والمقرر.
     * @param studentId معرف الطالب
     * @param courseId معرف المقرر
     * @return التسجيل أو null إذا لم يوجد
     * @throws Exception في حالة خطأ
     */
    public Enrollment getByIds(int studentId, int courseId) throws Exception {
        return enrollmentDAO.getByIds(studentId, courseId);
    }
}
