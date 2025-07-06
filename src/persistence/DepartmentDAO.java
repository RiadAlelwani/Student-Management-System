package persistence;

import domain.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private final Connection conn;

    public DepartmentDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Department> getAll() throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM department";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }
    public Department getById(int id) throws SQLException {
        String sql = "SELECT * FROM department WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Department(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null; // إذا لم يتم العثور على القسم
    }

}
