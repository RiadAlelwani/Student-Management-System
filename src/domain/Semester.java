package domain;

/**
 * تمثل فصلًا دراسيًا في النظام، يتضمن الموسم الدراسي، السنة، وحالة الفتح.
 */
public class Semester {
    private int id;
    private String season;
    private int year;
    private boolean isOpen;

    /**
     * كونستركتور افتراضي لإنشاء فصل دراسي فارغ.
     */
    public Semester() {}

    /**
     * إنشاء فصل دراسي بموسم وسنة وحالة الفتح.
     *
     * @param season الموسم الدراسي (مثلاً: "Spring", "Fall").
     * @param year السنة الأكاديمية.
     * @param isOpen هل الفصل مفتوح للتسجيل.
     * @throws IllegalArgumentException إذا كان الموسم فارغًا أو السنة غير صحيحة.
     */
    public Semester(String season, int year, boolean isOpen) {
        setSeason(season);
        setYear(year);
        setOpen(isOpen);
    }

    /**
     * إنشاء فصل دراسي بموسم وسنة فقط (الحالة غير محددة).
     *
     * @param season الموسم الدراسي.
     * @param year السنة الأكاديمية.
     */
    public Semester(String season, int year) {
        setSeason(season);
        setYear(year);
    }

    /**
     * إنشاء فصل دراسي بكافة الخصائص مع معرف.
     *
     * @param id معرف الفصل الدراسي.
     * @param season الموسم الدراسي.
     * @param year السنة الأكاديمية.
     * @param isOpen هل الفصل مفتوح للتسجيل.
     */
    public Semester(int id, String season, int year, boolean isOpen) {
        setId(id);
        setSeason(season);
        setYear(year);
        setOpen(isOpen);
    }

    /** @return معرف الفصل الدراسي */
    public int getId() { return id; }

    /** @param id تعيين معرف الفصل الدراسي */
    public void setId(int id) { this.id = id; }

    /** @return الموسم الدراسي */
    public String getSeason() { return season; }

    /**
     * تعيين الموسم الدراسي.
     *
     * @param season الموسم الدراسي (لا يمكن أن يكون فارغًا).
     * @throws IllegalArgumentException إذا كان الموسم فارغًا أو null.
     */
    public void setSeason(String season) {
        if (season == null || season.isBlank()) throw new IllegalArgumentException("Season is required");
        this.season = season;
    }

    /** @return السنة الأكاديمية */
    public int getYear() { return year; }

    /**
     * تعيين السنة الأكاديمية.
     *
     * @param year السنة الأكاديمية (بين 2000 و2100).
     * @throws IllegalArgumentException إذا كانت السنة خارج النطاق.
     */
    public void setYear(int year) {
        if (year < 2000 || year > 2100) throw new IllegalArgumentException("Invalid academic year");
        this.year = year;
    }

    /** @return هل الفصل مفتوح للتسجيل */
    public boolean isOpen() { return isOpen; }

    /** @param open تعيين حالة فتح الفصل الدراسي */
    public void setOpen(boolean open) { isOpen = open; }

    /**
     * تمثيل نصي للفصل الدراسي (مثال: "Fall 2025").
     */
    @Override
    public String toString() {
        return season + " " + year;
    }

    /**
     * التحقق من المساواة بين فصلين دراسيين بناءً على الموسم والسنة (غير حساسة لحالة الحروف).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Semester other = (Semester) obj;
        return year == other.year && season.equalsIgnoreCase(other.season);
    }

    /**
     * حساب قيمة هاش تعتمد على السنة والموسم (غير حساس لحالة الحروف).
     */
    @Override
    public int hashCode() {
        return 31 * year + season.toLowerCase().hashCode();
    }
}