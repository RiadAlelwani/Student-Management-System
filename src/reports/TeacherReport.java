package reports;

import application.TeacherService;
import domain.Teacher;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeacherReport implements Report {
    private final TeacherService teacherService;
    private JPanel reportPanel;

    public TeacherReport(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public void show(Component parent) {
        try {
            List<Teacher> teachers = teacherService.getAll();
            String[] columns = {"ID", "Name", "Email", "Gender", "Age", "Department", "Salary"};
            Object[][] data = new Object[teachers.size()][columns.length];

            for (int i = 0; i < teachers.size(); i++) {
                Teacher t = teachers.get(i);
                data[i][0] = t.getId();
                data[i][1] = t.getName();
                data[i][2] = t.getEmail();
                data[i][3] = t.getGender();
                data[i][4] = t.getAge();
                data[i][5] = t.getDepartmentName();
                data[i][6] = String.format("%.2f", t.getSalary());
            }

            reportPanel = createReportPanel(
                    "Teacher Report",
                    data,
                    columns,
                    "Total Teachers: " + teachers.size()
            );

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading teacher data");
            reportPanel = null;
        }
    }

    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    private JPanel createReportPanel(String title, Object[][] data, String[] columns, String summary) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // عنوان التقرير
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // معلومات مثل إجمالي عدد المعلمين
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel(summary));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // جدول المعلمين
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
