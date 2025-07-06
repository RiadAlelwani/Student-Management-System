package domain;

public class Course {
    private int id;
    private String name;
    private String description;
    private int credits;
    private int teacherId;
    private int departmentId;

    private String teacherName;     // اسم المعلم للعرض فقط
    private String departmentName;  // اسم القسم للعرض فقط

    public Course() {}

    public Course(String name, String description, int credits, int teacherId, int departmentId) {
        setName(name);
        setDescription(description);
        setCredits(credits);
        setTeacherId(teacherId);
        setDepartmentId(departmentId);
    }

    public Course(int id, String name, String description, int credits, int teacherId, int departmentId) {
        setId(id);
        setName(name);
        setDescription(description);
        setCredits(credits);
        setTeacherId(teacherId);
        setDepartmentId(departmentId);
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Course name is required");
        this.name = name;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description is required");
        this.description = description;
    }

    public int getCredits() { return credits; }
    public void setCredits(int credits) {
        if (credits < 1 || credits > 10) throw new IllegalArgumentException("Credits must be between 1 and 10");
        this.credits = credits;
    }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) {
        if (teacherId < 0) throw new IllegalArgumentException("Invalid teacher ID");
        this.teacherId = teacherId;
    }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) {
        if (departmentId < 0) throw new IllegalArgumentException("Invalid department ID");
        this.departmentId = departmentId;
    }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    @Override
    public String toString() {
        return name;
    }
}
