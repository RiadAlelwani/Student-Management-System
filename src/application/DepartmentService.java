package application;

import domain.Department;
import persistence.DepartmentDAO;
import java.sql.Connection;
import java.util.List;

/**
 * خدمة لإدارة الأقسام.
 */
public class DepartmentService {
    private final DepartmentDAO departmentDAO;

    /**
     * إنشاء خدمة الأقسام بتمرير اتصال قاعدة البيانات.
     * @param conn اتصال قاعدة البيانات
     */
    public DepartmentService(Connection conn) {
        this.departmentDAO = new DepartmentDAO(conn);
    }

    /**
     * جلب جميع الأقسام.
     * @return قائمة الأقسام
     * @throws Exception في حال حدوث خطأ أثناء جلب البيانات
     */
    public List<Department> getAll() throws Exception {
        return departmentDAO.getAll();
    }
}
