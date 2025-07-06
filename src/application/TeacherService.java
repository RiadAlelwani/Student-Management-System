package application;

import persistence.TeacherDAO;
import domain.Teacher;
import infrastructure.TeacherNotifier;
import infrastructure.TeacherObserver;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {
    private final TeacherDAO teacherDAO;
    private final List<TeacherObserver> observers = new ArrayList<>();

    public TeacherService(Connection conn) {
        this.teacherDAO = new TeacherDAO(conn);
    }

    public void addObserver(TeacherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TeacherObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (TeacherObserver observer : observers) {
            observer.onTeacherListChanged();
        }
    }

    public void add(Teacher t) throws Exception {
        teacherDAO.addTeacher(t);
        notifyObservers();
        TeacherNotifier.notifyAllObservers(getAll());
    }

    public void update(Teacher t) throws Exception {
        teacherDAO.updateTeacher(t);
        notifyObservers();
        TeacherNotifier.notifyAllObservers(getAll());
    }

    public void delete(int id) throws Exception {
        teacherDAO.deleteTeacher(id);
        notifyObservers();
        TeacherNotifier.notifyAllObservers(getAll());
    }

    public List<Teacher> getAll() throws Exception {
        return teacherDAO.getAll();
    }

    public List<Teacher> searchByName(String keyword) throws Exception {
        return teacherDAO.searchByName(keyword);
    }
}
