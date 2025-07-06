package infrastructure;

import domain.Enrollment;
import domain.Student;
import java.util.List;
import application.CourseService;

/**
 * تنفيذ لحاسبة المعدل التراكمي تستخدم التدرج المرجح
 * بناءً على ساعات معتمدة للمقررات.
 */
public class WeightedGradeCalculator implements GradeCalculator {
    private CourseService courseService;

    /**
     * مُنشئ لتهيئة الحاسبة مع نسخة CourseService.
     * @param courseService الخدمة المستخدمة للوصول إلى بيانات المقرر
     */
    public WeightedGradeCalculator(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * حساب المعدل التراكمي للطالب باستخدام المتوسط المرجح للدرجات
     * بناءً على ساعات معتمدة للمقررات.
     * @param student الطالب المراد حساب معدله التراكمي
     * @return المعدل التراكمي كقيمة double
     * @throws Exception إذا حدث أي خطأ أثناء جلب بيانات المقرر
     */
    @Override
    public double calculateGPA(Student student) throws Exception {
        List<Enrollment> enrollments = student.getEnrollments();
        if (enrollments == null || enrollments.isEmpty()) return 0;

        double totalPoints = 0;
        double totalCredits = 0;

        for (Enrollment e : enrollments) {
            double grade = e.getGrade();
            int credits = courseService.getById(e.getCourseId()).getCredits();
            double normalizedGrade = (grade / 100.0) * 4.0;
            totalPoints += normalizedGrade * credits;
            totalCredits += credits;
        }

        return totalCredits == 0 ? 0 : totalPoints / totalCredits;
    }
}