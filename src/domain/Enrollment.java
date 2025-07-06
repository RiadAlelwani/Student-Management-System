package domain;

/**
 * يمثل تسجيل طالب في مقرر معين خلال فصل دراسي معين،
 * مع إمكانية تعيين المعلم والدرجة المحققة.
 */
public class Enrollment {
    /** الطالب المسجل */
    private Student student;

    /** المقرر الدراسي */
    private Course course;

    /** المعلم المسؤول عن هذا التسجيل (اختياري) */
    private Teacher teacher;

    /** الدرجة المحققة، -1 تعني عدم وجود درجة حتى الآن */
    private double grade = -1;

    /** الفصل الدراسي المرتبط بالتسجيل */
    private Semester semester;

    /**
     * كونستركتور افتراضي لإنشاء تسجيل فارغ.
     */
    public Enrollment() {}

    /**
     * كونستركتور كامل لإنشاء تسجيل مع بيانات كاملة.
     * @param student الطالب
     * @param course المقرر
     * @param teacher المعلم
     * @param grade الدرجة المحققة (0-100)
     * @param semester الفصل الدراسي
     * @throws IllegalArgumentException إذا كانت بعض القيم غير صحيحة
     */
    public Enrollment(Student student, Course course, Teacher teacher, double grade, Semester semester) {
        setStudent(student);
        setCourse(course);
        setTeacher(teacher);
        setGrade(grade);
        setSemester(semester);
    }

    /**
     * كونستركتور لإنشاء تسجيل بمعرفات الطالب والمقرر فقط،
     * يستخدم لإنشاء تسجيل مؤقت مع تعيين أسماء لاحقًا.
     * @param studentId معرف الطالب
     * @param courseId معرف المقرر
     * @param grade الدرجة المحققة (0-100)
     * @param semester الفصل الدراسي
     */
    public Enrollment(int studentId, int courseId, double grade, Semester semester) {
        Student s = new Student();
        s.setId(studentId);
        setStudent(s);

        Course c = new Course();
        c.setId(courseId);
        setCourse(c);

        setGrade(grade);
        setSemester(semester);
    }

    /** @return الطالب */
    public Student getStudent() { return student; }

    /**
     * تعيين الطالب.
     * @param student الطالب الجديد
     * @throws IllegalArgumentException إذا كان الطالب null
     */
    public void setStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        this.student = student;
    }

    /** @return المقرر */
    public Course getCourse() { return course; }

    /**
     * تعيين المقرر.
     * @param course المقرر الجديد
     * @throws IllegalArgumentException إذا كان المقرر null
     */
    public void setCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        this.course = course;
    }

    /** @return المعلم */
    public Teacher getTeacher() { return teacher; }

    /**
     * تعيين المعلم (يمكن أن يكون null).
     * @param teacher المعلم الجديد
     */
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    /** @return الدرجة */
    public double getGrade() { return grade; }

    /**
     * تعيين الدرجة.
     * @param grade الدرجة (بين 0 و100)
     * @throws IllegalArgumentException إذا كانت الدرجة خارج النطاق
     */
    public void setGrade(double grade) {
        if (grade < 0.0 || grade > 100.0)
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        this.grade = grade;
    }

    /** @return الفصل الدراسي */
    public Semester getSemester() { return semester; }

    /**
     * تعيين الفصل الدراسي.
     * @param semester الفصل الدراسي الجديد
     * @throws IllegalArgumentException إذا كان الفصل الدراسي null
     */
    public void setSemester(Semester semester) {
        if (semester == null) throw new IllegalArgumentException("Semester cannot be null");
        this.semester = semester;
    }

    /**
     * @return معرف الطالب إذا كان معرفًا، وإلا -1
     */
    public int getStudentId() { return student != null ? student.getId() : -1; }

    /**
     * @return معرف المقرر إذا كان معرفًا، وإلا -1
     */
    public int getCourseId() { return course != null ? course.getId() : -1; }

    /**
     * تمثيل نصي للتسجيل يشمل اسم الطالب واسم المقرر.
     * @return نص يصف التسجيل
     */
    @Override
    public String toString() {
        return student.getName() + " - " + course.getName();
    }
}
