package domain;

/**
 * تمثل كائن مسؤول (Admin) في النظام، وهو يرث من الكلاس Person.
 * يحتوي على معلومات تسجيل الدخول (اسم المستخدم وكلمة المرور).
 */
public class Admin extends Person {

    /** اسم المستخدم للمسؤول */
    private String username;

    /** كلمة المرور للمسؤول */
    private String password;

    /**
     * كونستركتور لإنشاء مسؤول جديد بدون معرف.
     * 
     * @param name اسم المسؤول
     * @param email البريد الإلكتروني
     * @param gender الجنس (Male/Female)
     * @param age العمر
     * @param username اسم المستخدم
     * @param password كلمة المرور
     * @throws IllegalArgumentException إذا كان اسم المستخدم أو كلمة المرور فارغًا أو null
     */
    public Admin(String name, String email, String gender, int age, String username, String password) {
        super(0, name, email, gender, age);
        setUsername(username);
        setPassword(password);
    }

    /**
     * كونستركتور لإنشاء مسؤول مع معرف محدد.
     * 
     * @param id معرف المسؤول
     * @param name اسم المسؤول
     * @param email البريد الإلكتروني
     * @param gender الجنس
     * @param age العمر
     * @param username اسم المستخدم
     * @param password كلمة المرور
     * @throws IllegalArgumentException إذا كان اسم المستخدم أو كلمة المرور فارغًا أو null
     */
    public Admin(int id, String name, String email, String gender, int age, String username, String password) {
        super(id, name, email, gender, age);
        setUsername(username);
        setPassword(password);
    }

    /**
     * كونستركتور نسخ لإنشاء نسخة من كائن مسؤول آخر.
     * 
     * @param other المسؤول المراد نسخه
     */
    public Admin(Admin other) {
        super(other.getId(), other.getName(), other.getEmail(), other.getGender(), other.getAge());
        setUsername(other.getUsername());
        setPassword(other.getPassword());
    }

    /**
     * الحصول على اسم المستخدم.
     * 
     * @return اسم المستخدم
     */
    public String getUsername() { return username; }

    /**
     * تعيين اسم المستخدم.
     * 
     * @param username اسم المستخدم الجديد
     * @throws IllegalArgumentException إذا كان اسم المستخدم فارغًا أو null
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username is required");
        this.username = username;
    }

    /**
     * الحصول على كلمة المرور.
     * 
     * @return كلمة المرور
     */
    public String getPassword() { return password; }

    /**
     * تعيين كلمة المرور.
     * 
     * @param password كلمة المرور الجديدة
     * @throws IllegalArgumentException إذا كانت كلمة المرور فارغة أو null
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password is required");
        this.password = password;
    }

    /**
     * الحصول على تفاصيل المسؤول كنص.
     * 
     * @return نص يحتوي على اسم المسؤول والبريد الإلكتروني
     */
    @Override
    public String getDetails() {
        return "Admin: " + getName() + ", Email: " + getEmail();
    }

    /**
     * عرض معلومات المسؤول على الكونسول.
     */
    @Override
    public void displayInfo() {
        System.out.println(getDetails());
    }

    /**
     * تمثيل نصي لكائن المسؤول.
     * 
     * @return نص يحتوي على المعرف والاسم واسم المستخدم
     */
    @Override
    public String toString() {
        return "Admin{id=" + getId() + ", name=" + getName() + ", username=" + username + "}";
    }
}
