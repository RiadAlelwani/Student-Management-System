package domain;

/**
 * تمثل كائن المعلم، يرث من الفئة المجردة Person.
 * يحتوي على معلومات القسم، الراتب، ومعرف القسم.
 */
public class Teacher extends Person {
    private String departmentName;
    private double salary;
    private int department_id;

    /**
     * كونستركتور افتراضي ينشئ معلمًا فارغًا بقيم مبدئية.
     */
    public Teacher() {
        super(0, "", "", "", 0);
        this.departmentName = "";
        this.salary = 0.0;
        this.department_id = 0;
    }

    /**
     * إنشاء معلم مع كافة التفاصيل.
     *
     * @param id معرف المعلم.
     * @param name اسم المعلم.
     * @param email البريد الإلكتروني.
     * @param gender الجنس.
     * @param age العمر.
     * @param department_id معرف القسم.
     * @param salary الراتب (غير سالب).
     * @throws IllegalArgumentException إذا كانت القيم غير صحيحة.
     */
    public Teacher(int id, String name, String email, String gender, int age, int department_id, double salary) {
        super(id, name, email, gender, age);
        setDepartmentId(department_id);
        setSalary(salary);
    }

    /**
     * كونستركتور نسخ لعمل نسخة طبق الأصل من معلم آخر.
     *
     * @param other المعلم المراد نسخه.
     */
    public Teacher(Teacher other) {
        super(other.getId(), other.getName(), other.getEmail(), other.getGender(), other.getAge());
        setDepartmentName(other.getDepartmentName());
        setSalary(other.getSalary());
        setDepartmentId(other.getDepartmentId());
    }

    /**
     * @return اسم القسم الذي ينتمي إليه المعلم.
     */
    public String getDepartmentName() { return departmentName; }

    /**
     * تعيين اسم القسم.
     *
     * @param departmentName اسم القسم (لا يمكن أن يكون فارغًا أو null).
     * @throws IllegalArgumentException إذا كان الاسم فارغًا أو null.
     */
    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isBlank()) throw new IllegalArgumentException("Department name is required");
        this.departmentName = departmentName;
    }

    /**
     * @return راتب المعلم.
     */
    public double getSalary() { return salary; }

    /**
     * تعيين راتب المعلم.
     *
     * @param salary الراتب (يجب أن يكون صفر أو أكبر).
     * @throws IllegalArgumentException إذا كان الراتب سالبًا.
     */
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }

    /**
     * @return معرف القسم الذي ينتمي إليه المعلم.
     */
    public int getDepartmentId() { return department_id; }

    /**
     * تعيين معرف القسم.
     *
     * @param department_id معرف القسم (يجب أن يكون صفر أو أكبر).
     * @throws IllegalArgumentException إذا كان المعرف سالبًا.
     */
    public void setDepartmentId(int department_id) {
        if (department_id < 0) throw new IllegalArgumentException("Invalid department ID");
        this.department_id = department_id;
    }

    /**
     * إرجاع تفاصيل المعلم كنص.
     *
     * @return تفاصيل المعلم تشمل الاسم، القسم، والراتب.
     */
    @Override
    public String getDetails() {
        return "Teacher - " + name + ", Department: " + departmentName + ", Salary: " + String.format("%.2f", salary);
    }

    /**
     * عرض معلومات المعلم في وحدة التحكم.
     */
    @Override
    public void displayInfo() {
        System.out.println(getDetails());
    }

    /**
     * تمثيل نصي للمعلم (الاسم فقط).
     *
     * @return اسم المعلم.
     */
    @Override
    public String toString() {
        return name;
    }
}
