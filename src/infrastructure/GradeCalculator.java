package infrastructure;

import domain.Student;

/**
 * هذه الواجهة تحدد عقدًا لحساب المعدل التراكمي (GPA)
 * لطالب معين. أي فئة تنفذ هذه الواجهة يجب أن توفر
 * تنفيذًا لطريقة calculateGPA.
 */
public interface GradeCalculator {
    /**
     * حساب المعدل التراكمي للطالب المعطى.
     * @param student الطالب المراد حساب معدله التراكمي
     * @return المعدل التراكمي المحسوب كقيمة double
     * @throws Exception إذا حدث أي خطأ أثناء الحساب (مثل بيانات غير صالحة)
     */
    double calculateGPA(Student student) throws Exception;
}