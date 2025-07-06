// StudentService.java - تحديث لدعم التقرير حسب المادة والفصل
package application;

import persistence.StudentDAO;
import persistence.EnrollmentDAO;
import domain.Student;
import domain.Enrollment;

import java.sql.Connection;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;
    private final EnrollmentDAO enrollmentDAO;

    public StudentService(Connection conn) {
        this.studentDAO = new StudentDAO(conn);
        this.enrollmentDAO = new EnrollmentDAO(conn);
    }

    public void add(Student student) throws Exception {
        studentDAO.addStudent(student);
    }

    public void update(Student student) throws Exception {
        studentDAO.updateStudent(student);
    }

    public void delete(int id) throws Exception {
        studentDAO.deleteStudent(id);
    }

    public List<Student> searchByName(String name) throws Exception {
        return studentDAO.searchByName(name);
    }

    public List<Student> getAll() throws Exception {
        return studentDAO.getAll();
    }

    public List<Enrollment> getEnrollmentsByStudentName(String name) throws Exception {
        return enrollmentDAO.getEnrollmentsByStudentName(name);
    }

    // ✅ دالة جديدة: تسجيلات الطلاب حسب اسم المادة والفصل
    public List<Enrollment> getEnrollmentsByCourseAndSemester(String courseName, String season, int year) throws Exception {
        return enrollmentDAO.getEnrollmentsByCourseAndSemester(courseName, season, year);
    }
    public List<Enrollment> getEnrollmentsByStudentAndSemester(String studentName, String season, int year) throws Exception {
        return studentDAO.getEnrollmentsByStudentAndSemester(studentName, season, year);
    }

}
