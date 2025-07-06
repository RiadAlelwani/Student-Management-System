package domain;

public abstract class Person implements PersonInfo {
    protected int id;
    protected String name;
    protected String email;
    protected String gender;
    protected int age;

    // كونستركتور لإنشاء شخص بدون معرف
    public Person(String name, String email, String gender, int age) {
        this(0, name, email, gender, age);
    }

    // كونستركتور لإنشاء شخص مع معرف
    public Person(int id, String name, String email, String gender, int age) {
        setId(id);
        setName(name);
        setEmail(email);
        setGender(gender);
        setAge(age);
    }

    // getters و setters مع تحقق من صحة المدخلات
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        this.name = name;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        this.email = email;
    }

    public String getGender() { return gender; }
    public void setGender(String gender) {
        if (gender == null || gender.isBlank()) throw new IllegalArgumentException("Gender is required");
        this.gender = gender;
    }

    public int getAge() { return age; }
    public void setAge(int age) {
        if (age < 18) throw new IllegalArgumentException("Age must be at least 18");
        this.age = age;
    }

    // طرق مجردة يجب تنفيذها في الفئات الفرعية
    public abstract String getDetails();
    public abstract void displayInfo();
}