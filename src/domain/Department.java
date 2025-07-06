package domain;

public class Department {
    private int id;
    private String name;

    public Department() {}

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Department name is required");
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
