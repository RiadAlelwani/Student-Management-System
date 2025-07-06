package domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String major;
    private double gpa;
    private List<Enrollment> enrollments;

    // كونستركتور افتراضي
    public Student() {
        super(0, "", "", "", 0);
        this.major = "";
        this.gpa = 0.0;
        this.enrollments = new ArrayList<>();
    }

    // كونستركتور لإنشاء طالب مع جميع التفاصيل
    public Student(int id, String name, String email, String gender, int age, String major, double gpa) {
        super(id, name, email, gender, age);
        setMajor(major);
        setGpa(gpa);
        this.enrollments = new ArrayList<>();
    }

    // كونستركتور نسخ
    public Student(Student other) {
        super(other.getId(), other.getName(), other.getEmail(), other.getGender(), other.getAge());
        setMajor(other.getMajor());
        setGpa(other.getGpa());
        this.enrollments = other.getEnrollments() != null 
            ? new ArrayList<>(other.getEnrollments()) 
            : new ArrayList<>();
    }

    // getters و setters مع تحقق من صحة المدخلات
    public String getMajor() { return major; }
    public void setMajor(String major) {
        if (major == null || major.isBlank()) throw new IllegalArgumentException("Major is required");
        this.major = major;
    }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        this.gpa = gpa;
    }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) {
        if (enrollments == null) throw new IllegalArgumentException("Enrollments list cannot be null");
        this.enrollments = enrollments;
    }

    // إضافة تسجيل جديد للطالب
    public void addEnrollment(Enrollment e) {
        if (e == null) throw new IllegalArgumentException("Enrollment cannot be null");
        this.enrollments.add(e);
    }

    // تفاصيل الطالب
    @Override
    public String getDetails() {
        return String.format("Student ID: %d, Name: %s, Email: %s, Gender: %s, Age: %d, Major: %s, GPA: %.2f",
                getId(), getName(), getEmail(), getGender(), getAge(), major, gpa);
    }

    // عرض معلومات الطالب
    @Override
    public void displayInfo() {
        System.out.println(getDetails());
    }

    @Override
    public String toString() {
        return name;
    }
}