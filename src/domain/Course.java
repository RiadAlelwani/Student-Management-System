package domain;

/**
 * يمثل مقرر دراسي يحتوي على معلومات مثل الاسم، الوصف، عدد الساعات، 
 * المعلم المسؤول، والقسم التابع له.
 */
public class Course {
    /** معرف المقرر */
    private int id;

    /** اسم المقرر */
    private String name;

    /** وصف المقرر */
    private String description;

    /** عدد الساعات المعتمدة (الكريدتس) */
    private int credits;

    /** معرف المعلم المسؤول عن المقرر */
    private int teacherId;

    /** معرف القسم التابع له المقرر */
    private int departmentId;

    /** اسم المعلم (للعرض فقط) */
    private String teacherName;

    /** اسم القسم (للعرض فقط) */
    private String departmentName;

    /**
     * كونستركتور افتراضي لإنشاء كائن Course فارغ.
     */
    public Course() {}

    /**
     * كونستركتور لإنشاء مقرر جديد بدون معرف.
     * 
     * @param name اسم المقرر
     * @param description وصف المقرر
     * @param credits عدد الساعات المعتمدة (1-10)
     * @param teacherId معرف المعلم المسؤول
     * @param departmentId معرف القسم
     * @throws IllegalArgumentException إذا كانت القيم غير صالحة
     */
    public Course(String name, String description, int credits, int teacherId, int departmentId) {
        setName(name);
        setDescription(description);
        setCredits(credits);
        setTeacherId(teacherId);
        setDepartmentId(departmentId);
    }

    /**
     * كونستركتور لإنشاء مقرر مع معرف محدد (للحالات مثل التحديث).
     * 
     * @param id معرف المقرر
     * @param name اسم المقرر
     * @param description وصف المقرر
     * @param credits عدد الساعات المعتمدة (1-10)
     * @param teacherId معرف المعلم المسؤول
     * @param departmentId معرف القسم
     * @throws IllegalArgumentException إذا كانت القيم غير صالحة
     */
    public Course(int id, String name, String description, int credits, int teacherId, int departmentId) {
        setId(id);
        setName(name);
        setDescription(description);
        setCredits(credits);
        setTeacherId(teacherId);
        setDepartmentId(departmentId);
    }

    /**
     * الحصول على معرف المقرر.
     * @return المعرف
     */
    public int getId() { return id; }

    /**
     * تعيين معرف المقرر.
     * @param id المعرف الجديد
     */
    public void setId(int id) { this.id = id; }

    /**
     * الحصول على اسم المقرر.
     * @return اسم المقرر
     */
    public String getName() { return name; }

    /**
     * تعيين اسم المقرر.
     * @param name الاسم الجديد
     * @throws IllegalArgumentException إذا كان الاسم فارغًا أو null
     */
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Course name is required");
        this.name = name;
    }

    /**
     * الحصول على وصف المقرر.
     * @return الوصف
     */
    public String getDescription() { return description; }

    /**
     * تعيين وصف المقرر.
     * @param description الوصف الجديد
     * @throws IllegalArgumentException إذا كان الوصف فارغًا أو null
     */
    public void setDescription(String description) {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description is required");
        this.description = description;
    }

    /**
     * الحصول على عدد الساعات المعتمدة (الكريدتس).
     * @return عدد الساعات
     */
    public int getCredits() { return credits; }

    /**
     * تعيين عدد الساعات المعتمدة.
     * @param credits عدد الساعات بين 1 و 10
     * @throws IllegalArgumentException إذا كانت القيمة خارج النطاق
     */
    public void setCredits(int credits) {
        if (credits < 1 || credits > 10)
            throw new IllegalArgumentException("Credits must be between 1 and 10");
        this.credits = credits;
    }

    /**
     * الحصول على معرف المعلم المسؤول.
     * @return معرف المعلم
     */
    public int getTeacherId() { return teacherId; }

    /**
     * تعيين معرف المعلم المسؤول.
     * @param teacherId المعرف الجديد (غير سالب)
     * @throws IllegalArgumentException إذا كانت القيمة سالبة
     */
    public void setTeacherId(int teacherId) {
        if (teacherId < 0)
            throw new IllegalArgumentException("Invalid teacher ID");
        this.teacherId = teacherId;
    }

    /**
     * الحصول على معرف القسم.
     * @return معرف القسم
     */
    public int getDepartmentId() { return departmentId; }

    /**
     * تعيين معرف القسم.
     * @param departmentId المعرف الجديد (غير سالب)
     * @throws IllegalArgumentException إذا كانت القيمة سالبة
     */
    public void setDepartmentId(int departmentId) {
        if (departmentId < 0)
            throw new IllegalArgumentException("Invalid department ID");
        this.departmentId = departmentId;
    }

    /**
     * الحصول على اسم المعلم (للعرض فقط).
     * @return اسم المعلم
     */
    public String getTeacherName() { return teacherName; }

    /**
     * تعيين اسم المعلم (للعرض فقط).
     * @param teacherName الاسم الجديد
     */
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    /**
     * الحصول على اسم القسم (للعرض فقط).
     * @return اسم القسم
     */
    public String getDepartmentName() { return departmentName; }

    /**
     * تعيين اسم القسم (للعرض فقط).
     * @param departmentName الاسم الجديد
     */
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    /**
     * تمثيل النصي للمقرر، يعيد الاسم فقط.
     * @return اسم المقرر
     */
    @Override
    public String toString() {
        return name;
    }
}
