package domain;

public class Admin extends Person {
    private String username;
    private String password;

    // كونستركتور لإنشاء مسؤول جديد
    public Admin(String name, String email, String gender, int age, String username, String password) {
        super(0, name, email, gender, age);
        setUsername(username);
        setPassword(password);
    }

    // كونستركتور لإنشاء مسؤول مع معرف محدد
    public Admin(int id, String name, String email, String gender, int age, String username, String password) {
        super(id, name, email, gender, age);
        setUsername(username);
        setPassword(password);
    }

    // كونستركتور نسخ
    public Admin(Admin other) {
        super(other.getId(), other.getName(), other.getEmail(), other.getGender(), other.getAge());
        setUsername(other.getUsername());
        setPassword(other.getPassword());
    }

    // getters و setters مع تحقق من صحة المدخلات
    public String getUsername() { return username; }
    public void setUsername(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
        this.username = username;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password is required");
        this.password = password;
    }

    // تفاصيل المسؤول
    @Override
    public String getDetails() {
        return "Admin: " + getName() + ", Email: " + getEmail();
    }

    // عرض معلومات المسؤول
    @Override
    public void displayInfo() {
        System.out.println(getDetails());
    }
}