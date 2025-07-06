package domain;

public class Teacher extends Person {
	private String departmentName;
    private double salary;
    private int department_id;

    public Teacher() {
        super(0, "", "", "", 0);
        this.departmentName = "";
        this.salary = 0.0;
        this.department_id = 0;
    }

    public Teacher(int id, String name, String email, String gender, int age, int department_id, double salary) {
        super(id, name, email, gender, age);
        setDepartmentId(department_id);
        setSalary(salary);
        //this.departmentName = "";
    }

    public Teacher(Teacher other) {
        super(other.getId(), other.getName(), other.getEmail(), other.getGender(), other.getAge());
        setDepartmentName(other.getDepartmentName());
        setSalary(other.getSalary());
        setDepartmentId(other.getDepartmentId());
    }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isBlank()) throw new IllegalArgumentException("Department name is required");
        this.departmentName = departmentName;
    }

    public double getSalary() { return salary; }
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }

    public int getDepartmentId() { return department_id; }
    public void setDepartmentId(int department_id) {
        if (department_id < 0) throw new IllegalArgumentException("Invalid department ID");
        this.department_id = department_id;
    }

    @Override
    public String getDetails() {
        return "Teacher - " + name + ", Department: " + departmentName + ", Salary: " + String.format("%.2f", salary);
    }

    @Override
    public void displayInfo() {
        System.out.println(getDetails());
    }

    @Override
    public String toString() {
        return name;
    }
}
