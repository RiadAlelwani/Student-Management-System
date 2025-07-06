package application;

import domain.Department;
import persistence.DepartmentDAO;
import java.sql.Connection;
import java.util.List;

public class DepartmentService {
    private final DepartmentDAO departmentDAO;

    public DepartmentService(Connection conn) {
        this.departmentDAO = new DepartmentDAO(conn);
    }

    public List<Department> getAll() throws Exception {
        return departmentDAO.getAll();
    }
}
