package application;

import persistence.CourseDAO;
import domain.Course;
import java.sql.Connection;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService(Connection conn) {
        this.courseDAO = new CourseDAO(conn);
    }

    public void add(Course course) throws Exception {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        courseDAO.add(course);
    }

    public void update(Course course) throws Exception {
        if (course == null || course.getId() <= 0) throw new IllegalArgumentException("Invalid course");
        courseDAO.update(course);
    }

    public void delete(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid course ID");
        courseDAO.delete(id);
    }

    public List<Course> getAllCourses() throws Exception {
        return courseDAO.getAll();
    }

    public Course getById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("Invalid course ID");
        return courseDAO.getById(id);
    }

    // **أضف هذه الدالة**
    public List<Course> getAllWithTeacherAndDepartment() throws Exception {
        return courseDAO.getAll();
    }
    public List<Course> getCoursesByTeacherId(int teacherId) throws Exception {
        return courseDAO.findByTeacherId(teacherId);
    }

}
