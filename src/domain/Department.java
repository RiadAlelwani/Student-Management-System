package domain;

/**
 * تمثل قسمًا أكاديميًا في النظام، يحتوي على معرف واسم القسم.
 */
public class Department {
    /** معرف القسم */
    private int id;

    /** اسم القسم */
    private String name;

    /**
     * كونستركتور افتراضي لإنشاء كائن Department فارغ.
     */
    public Department() {}

    /**
     * كونستركتور لإنشاء قسم جديد بمعرف واسم محددين.
     *
     * @param id معرف القسم
     * @param name اسم القسم
     * @throws IllegalArgumentException إذا كان الاسم فارغًا أو null
     */
    public Department(int id, String name) {
        this.id = id;
        setName(name);
    }

    /**
     * الحصول على معرف القسم.
     * @return المعرف
     */
    public int getId() { return id; }

    /**
     * تعيين معرف القسم.
     * @param id المعرف الجديد
     */
    public void setId(int id) { this.id = id; }

    /**
     * الحصول على اسم القسم.
     * @return اسم القسم
     */
    public String getName() { return name; }

    /**
     * تعيين اسم القسم.
     * @param name الاسم الجديد
     * @throws IllegalArgumentException إذا كان الاسم فارغًا أو null
     */
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Department name is required");
        this.name = name;
    }

    /**
     * تمثيل نصي للكائن، يعيد اسم القسم.
     * @return اسم القسم كنص
     */
    @Override
    public String toString() {
        return name;
    }
}
