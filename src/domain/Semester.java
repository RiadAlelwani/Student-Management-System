package domain;

	public class Semester {
    private int id;
    private String season;
    private int year;
    private boolean isOpen;

    public Semester() {}

    public Semester(String season, int year, boolean isOpen) {
        setSeason(season);
        setYear(year);
        setOpen(isOpen);
    }

    public Semester(String season, int year) {
        setSeason(season);
        setYear(year);
    }

    public Semester(int id, String season, int year, boolean isOpen) {
        setId(id);
        setSeason(season);
        setYear(year);
        setOpen(isOpen);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSeason() { return season; }
    public void setSeason(String season) {
        if (season == null || season.isBlank()) throw new IllegalArgumentException("Season is required");
        this.season = season;
    }

    public int getYear() { return year; }
    public void setYear(int year) {
        if (year < 2000 || year > 2100) throw new IllegalArgumentException("Invalid academic year");
        this.year = year;
    }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }

    @Override
    public String toString() {
        return season + " " + year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Semester other = (Semester) obj;
        return year == other.year && season.equalsIgnoreCase(other.season);
    }

    @Override
    public int hashCode() {
        return 31 * year + season.toLowerCase().hashCode();
    }
}
