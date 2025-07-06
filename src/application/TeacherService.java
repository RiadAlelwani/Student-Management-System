package application;

import persistence.TeacherDAO;
import domain.Teacher;
import infrastructure.TeacherNotifier;
import infrastructure.TeacherObserver;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * خدمة إدارة بيانات المعلمين مع دعم نمط المراقب (Observer Pattern)
 */
public class TeacherService {
    private final TeacherDAO teacherDAO;
    private final List<TeacherObserver> observers = new ArrayList<>();

    public TeacherService(Connection conn) {
        this.teacherDAO = new TeacherDAO(conn);
    }

    // تسجيل مراقب محلي
    public void addObserver(TeacherObserver observer) {
        observers.add(observer);
    }

    // إزالة مراقب محلي
    public void removeObserver(TeacherObserver observer) {
        observers.remove(observer);
    }

    // إعلام جميع المراقبين المحليين
    private void notifyObservers() {
        for (TeacherObserver observer : observers) {
            observer.onTeacherListChanged();
        }
    }

    // إضافة معلم جديد وتنبيه المراقبين
    public void add(Teacher t) throws Exception {
        teacherDAO.addTeacher(t);
        notifyObservers();
        TeacherNotifier.notifyAllObservers(getAll());
    }

    // تحديث معلم موجود وتنبيه المراقبين
    public void update(Teacher t) throws Exception {
        teacherDAO.updateTeacher(t);
        notifyObservers();
        TeacherNotifier.notifyAllObservers(getAll());
    }

    // حذف معلم وتنبيه المراقبين
    public void delete(int id) throws Exception {
        teacherDAO.deleteTeacher(id);
        notifyObservers();
        TeacherNotifier.notifyAllObservers(getAll());
    }

    // جلب جميع المعلمين
    public List<Teacher> getAll() throws Exception {
        return teacherDAO.getAll();
    }

    // البحث عن معلمين حسب الاسم
    public List<Teacher> searchByName(String keyword) throws Exception {
        return teacherDAO.searchByName(keyword);
    }
}
