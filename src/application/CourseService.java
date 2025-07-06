package application;

import persistence.CourseDAO;
import domain.Course;
import java.sql.Connection;
import java.util.List;

/**
 * خدمة لإدارة العمليات المتعلقة بالكورسات (الدورات التعليمية).
 */
public class CourseService {
    private final CourseDAO courseDAO;

    /**
     * إنشاء الخدمة بتمرير اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات
     */
    public CourseService(Connection conn) {
        this.courseDAO = new CourseDAO(conn);
    }

    /**
     * إضافة كورس جديد.
     * @param course الكورس لإضافته
     * @throws Exception في حال وجود خطأ في الإدخال أو تنفيذ العملية
     */
    public void add(Course course) throws Exception {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        courseDAO.add(course);
    }

    /**
     * تحديث بيانات كورس موجود.
     * @param course الكورس مع بيانات محدثة
     * @throws Exception في حال وجود خطأ أو كورس غير صالح
     */
    public void update(Course course) throws Exception {
        if (course == null || course.getId() <= 0) throw new IllegalArgumentException("Invalid course");
        courseDAO.update(course);
    }

    /**
     * حذف كورس عبر معرفه.
     * @param id معرف الكورس
     * @throws Exception في حال معرف غير صالح أو خطأ في الحذف
     */
    public void delete(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid course ID");
        courseDAO.delete(id);
    }

    /**
     * جلب جميع الكورسات.
     * @return قائمة الكورسات
     * @throws Exception في حال حدوث خطأ أثناء جلب البيانات
     */
    public List<Course> getAllCourses() throws Exception {
        return courseDAO.getAll();
    }

    /**
     * جلب كورس حسب المعرف.
     * @param id معرف الكورس
     * @return كائن الكورس
     * @throws Exception في حال معرف غير صالح أو خطأ
     */
    public Course getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid course ID");
        return courseDAO.getById(id);
    }

    /**
     * جلب جميع الكورسات مع معلومات المعلم والقسم (إن كانت متوفرة).
     * (حالياً يعيد نفس getAll()، يمكن تعديلها لاحقًا)
     * @return قائمة الكورسات
     * @throws Exception في حال خطأ
     */
    public List<Course> getAllWithTeacherAndDepartment() throws Exception {
        return courseDAO.getAll();
    }

    /**
     * جلب الكورسات الخاصة بمعلم معين.
     * @param teacherId معرف المعلم
     * @return قائمة الكورسات
     * @throws Exception في حال خطأ
     */
    public List<Course> getCoursesByTeacherId(int teacherId) throws Exception {
        return courseDAO.findByTeacherId(teacherId);
    }
}
