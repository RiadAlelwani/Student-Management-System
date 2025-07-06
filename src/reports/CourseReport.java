package reports;

import application.CourseService;
import domain.Course;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CourseReport implements Report {
    private final CourseService courseService;
    private JPanel reportPanel;

    public CourseReport(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void show(Component parent) {
        try {
            List<Course> courses = courseService.getAllWithTeacherAndDepartment();
            String[] columns = {"ID", "Name", "Description", "Credits", "Teacher", "Department"};
            Object[][] data = new Object[courses.size()][columns.length];

            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                data[i][0] = c.getId();
                data[i][1] = c.getName();
                data[i][2] = c.getDescription();
                data[i][3] = c.getCredits();
                data[i][4] = c.getTeacherName();
                data[i][5] = c.getDepartmentName();
            }

            reportPanel = createReportPanel(
                    "Course Report",
                    data,
                    columns,
                    "Total Courses: " + courses.size()
            );

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading course data");
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

        // معلومات مثل إجمالي عدد المقررات
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel(summary));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // الجدول
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
