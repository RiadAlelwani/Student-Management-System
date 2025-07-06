package domain;

public class Enrollment {
    private Student student;
    private Course course;
    private Teacher teacher;
    private double grade = -1; // -1 يعني لا توجد درجة حتى الآن
    private Semester semester;

    // كونستركتور افتراضي
    public Enrollment() {}

    // كونستركتور كامل: بيانات الطالب، المقرر، المعلم، الدرجة، الفصل الدراسي
    public Enrollment(Student student, Course course, Teacher teacher, double grade, Semester semester) {
        setStudent(student);
        setCourse(course);
        setTeacher(teacher);
        setGrade(grade);
        setSemester(semester);
    }

    // كونستركتور بمعرفات فقط (غير مفضل استخدامه بدون تعيين الاسم لاحقًا)
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

    // getters و setters مع التحقق من صحة البيانات
    public Student getStudent() { return student; }
    public void setStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        this.student = student;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        this.course = course;
    }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public double getGrade() { return grade; }
    public void setGrade(double grade) {
        if (grade < 0.0 || grade > 100.0)
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        this.grade = grade;
    }

    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) {
        if (semester == null) throw new IllegalArgumentException("Semester cannot be null");
        this.semester = semester;
    }

    // مساعدة لإرجاع معرفات الطالب والمقرر
    public int getStudentId() { return student != null ? student.getId() : -1; }
    public int getCourseId() { return course != null ? course.getId() : -1; }

    @Override
    public String toString() {
        return student.getName() + " - " + course.getName();
    }
}
