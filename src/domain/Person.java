package domain;

/**
 * الفئة المجردة Person تمثل كائن شخص أساسي يحتوي على الخصائص المشتركة
 * مثل الاسم، البريد الإلكتروني، الجنس، والعمر.
 * هذه الفئة تُستخدم كأساس للفئات الفرعية التي تمثل أنواعًا مختلفة من الأشخاص.
 * 
 * الفئة تطبق واجهة PersonInfo التي تحدد طرقًا لعرض المعلومات.
 */
public abstract class Person implements PersonInfo {
    /** المعرف الفريد للشخص */
    protected int id;

    /** اسم الشخص */
    protected String name;

    /** البريد الإلكتروني */
    protected String email;

    /** الجنس */
    protected String gender;

    /** العمر */
    protected int age;

    /**
     * كونستركتور لإنشاء شخص بدون معرف (المعرف يُفترض أنه يُحدد لاحقًا).
     * 
     * @param name اسم الشخص (غير فارغ)
     * @param email البريد الإلكتروني (يجب أن يحتوي على '@')
     * @param gender الجنس (غير فارغ)
     * @param age العمر (18 أو أكثر)
     * @throws IllegalArgumentException إذا كانت القيم غير صحيحة
     */
    public Person(String name, String email, String gender, int age) {
        this(0, name, email, gender, age);
    }

    /**
     * كونستركتور لإنشاء شخص مع معرف محدد.
     * 
     * @param id المعرف الفريد (≥0)
     * @param name اسم الشخص (غير فارغ)
     * @param email البريد الإلكتروني (يجب أن يحتوي على '@')
     * @param gender الجنس (غير فارغ)
     * @param age العمر (18 أو أكثر)
     * @throws IllegalArgumentException إذا كانت القيم غير صحيحة
     */
    public Person(int id, String name, String email, String gender, int age) {
        setId(id);
        setName(name);
        setEmail(email);
        setGender(gender);
        setAge(age);
    }

    /** 
     * @return المعرف الفريد للشخص 
     */
    public int getId() { return id; }

    /**
     * تعيين المعرف الفريد.
     * @param id معرف ≥ 0
     */
    public void setId(int id) { this.id = id; }

    /** 
     * @return اسم الشخص 
     */
    public String getName() { return name; }

    /**
     * تعيين اسم الشخص.
     * @param name اسم غير فارغ
     * @throws IllegalArgumentException إذا كان الاسم فارغًا أو null
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        this.name = name;
    }

    /** 
     * @return البريد الإلكتروني 
     */
    public String getEmail() { return email; }

    /**
     * تعيين البريد الإلكتروني.
     * يجب أن يحتوي على الرمز '@'.
     * @param email بريد إلكتروني صحيح
     * @throws IllegalArgumentException إذا كان البريد غير صحيح
     */
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.email = email;
    }

    /** 
     * @return الجنس 
     */
    public String getGender() { return gender; }

    /**
     * تعيين الجنس.
     * @param gender قيمة غير فارغة
     * @throws IllegalArgumentException إذا كانت القيمة فارغة أو null
     */
    public void setGender(String gender) {
        if (gender == null || gender.isBlank()) throw new IllegalArgumentException("Gender is required");
        this.gender = gender;
    }

    /** 
     * @return العمر 
     */
    public int getAge() { return age; }

    /**
     * تعيين العمر.
     * يجب أن يكون 18 أو أكثر.
     * @param age العمر
     * @throws IllegalArgumentException إذا كان العمر أقل من 18
     */
    public void setAge(int age) {
        if (age < 18) throw new IllegalArgumentException("Age must be at least 18");
        this.age = age;
    }

    /**
     * طريقة مجردة يجب على الفئات الفرعية تنفيذها لتقديم تفاصيل الشخص بشكل نصي.
     * @return نص يصف تفاصيل الشخص
     */
    public abstract String getDetails();

    /**
     * طريقة مجردة يجب على الفئات الفرعية تنفيذها لعرض معلومات الشخص.
     */
    public abstract void displayInfo();
}