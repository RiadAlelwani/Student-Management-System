package application;

import persistence.SemesterDAO;
import domain.Semester;
import java.sql.Connection;
import java.util.List;

public class SemesterService {
    private final SemesterDAO dao;

    // إنشاء خدمة الفصول الدراسية مع اتصال قاعدة البيانات
    public SemesterService(Connection conn) {
        this.dao = new SemesterDAO(conn);
    }

    // إضافة فصل دراسي جديد
    public void add(Semester s) throws Exception {
        dao.add(s);
    }

    // تحديث معلومات فصل دراسي
    public void update(Semester s) throws Exception {
        dao.update(s);
    }

    // حذف فصل دراسي
    public void delete(int id) throws Exception {
        dao.delete(id);
    }

    // الحصول على جميع الفصول الدراسية
    public List<Semester> getAll() throws Exception {
        return dao.getAll();
    }

    // الحصول على فصل دراسي بواسطة المعرف
    public Semester getById(int id) throws Exception {
        return dao.getById(id);
    }

    // البحث عن فصول دراسية بالاسم
    public List<Semester> searchByName(String name) throws Exception {
        return dao.searchByName(name);
    }
}