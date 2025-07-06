package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * تمثل طالبًا في النظام، يرث من الفئة المجردة Person.
 * تحتوي على التخصص، المعدل التراكمي، وقائمة التسجيلات.
 */
public class Student extends Person {
    private String major;
    private double gpa;
    private List<Enrollment> enrollments;

    /**
     * كونستركتور افتراضي ينشئ طالبًا فارغًا بقيم مبدئية.
     */
    public Student() {
        super(0, "", "", "", 0);
        this.major = "";
        this.gpa = 0.0;
        this.enrollments = new ArrayList<>();
    }

    /**
     * إنشاء طالب بجميع التفاصيل.
     *
     * @param id معرف الطالب.
     * @param name اسم الطالب.
     * @param email البريد الإلكتروني.
     * @param gender الجنس.
     * @param age العمر.
     * @param major التخصص الأكاديمي.
     * @param gpa المعدل التراكمي (بين 0.0 و4.0).
     * @throws IllegalArgumentException في حال كانت القيم غير صحيحة.
     */
    public Student(int id, String name, String email, String gender, int age, String major, double gpa) {
        super(id, name, email, gender, age);
        setMajor(major);
        setGpa(gpa);
        this.enrollments = new ArrayList<>();
    }

    /**
     * كونستركتور نسخ لعمل نسخة طبق الأصل من طالب آخر.
     *
     * @param other الطالب المراد نسخه.
     */
    public Student(Student other) {
        super(other.getId(), other.getName(), other.getEmail(), other.getGender(), other.getAge());
        setMajor(other.getMajor());
        setGpa(other.getGpa());
        this.enrollments = other.getEnrollments() != null 
            ? new ArrayList<>(other.getEnrollments()) 
            : new ArrayList<>();
    }

    /**
     * @return التخصص الأكاديمي للطالب.
     */
    public String getMajor() { return major; }

    /**
     * تعيين التخصص الأكاديمي.
     *
     * @param major التخصص (لا يمكن أن يكون فارغًا).
     * @throws IllegalArgumentException إذا كان التخصص فارغًا أو null.
     */
    public void setMajor(String major) {
        if (major == null || major.isBlank()) throw new IllegalArgumentException("Major is required");
        this.major = major;
    }

    /**
     * @return المعدل التراكمي للطالب.
     */
    public double getGpa() { return gpa; }

    /**
     * تعيين المعدل التراكمي.
     *
     * @param gpa المعدل (يجب أن يكون بين 0.0 و4.0).
     * @throws IllegalArgumentException إذا كان المعدل خارج النطاق.
     */
    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        this.gpa = gpa;
    }

    /**
     * @return قائمة التسجيلات الخاصة بالطالب.
     */
    public List<Enrollment> getEnrollments() { return enrollments; }

    /**
     * تعيين قائمة التسجيلات.
     *
     * @param enrollments قائمة التسجيلات (لا يمكن أن تكون null).
     * @throws IllegalArgumentException إذا كانت القائمة null.
     */
    public void setEnrollments(List<Enrollment> enrollments) {
        if (enrollments == null) throw new IllegalArgumentException("Enrollments list cannot be null");
        this.enrollments = enrollments;
    }

    /**
     * إضافة تسجيل جديد لقائمة التسجيلات.
     *
     * @param e التسجيل الجديد.
     * @throws IllegalArgumentException إذا كان التسجيل null.
     */
    public void addEnrollment(Enrollment e) {
        if (e == null) throw new IllegalArgumentException("Enrollment cannot be null");
        this.enrollments.add(e);
    }

    /**
     * إرجاع تفاصيل الطالب كنص.
     *
     * @return تفاصيل الطالب مع كافة الخصائص المهمة.
     */
    @Override
    public String getDetails() {
        return String.format("Student ID: %d, Name: %s, Email: %s, Gender: %s, Age: %d, Major: %s, GPA: %.2f",
                getId(), getName(), getEmail(), getGender(), getAge(), major, gpa);
    }

    /**
     * عرض معلومات الطالب في وحدة التحكم.
     */
    @Override
    public void displayInfo() {
        System.out.println(getDetails());
    }

    /**
     * تمثيل نصي للطالب (يتم عرض الاسم).
     */
    @Override
    public String toString() {
        return name;
    }
}
