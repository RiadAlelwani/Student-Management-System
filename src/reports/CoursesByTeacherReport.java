package reports;

import application.CourseService;
import application.TeacherService;
import domain.Course;
import domain.Teacher;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CoursesByTeacherReport implements Report {
    private final CourseService courseService;
    private final TeacherService teacherService;
    private JPanel reportPanel;

    public CoursesByTeacherReport(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.reportPanel = null;
    }

    @Override
    public void show(Component parent) {
        try {
            // طلب اسم المدرس
            String teacherName = JOptionPane.showInputDialog(parent,
                    "Enter Teacher Name:", "Courses by Teacher", JOptionPane.QUESTION_MESSAGE);

            if (teacherName == null || teacherName.trim().isEmpty()) {
                reportPanel = null;  // لا شيء لعرضه
                return;
            }

            List<Teacher> teachers = teacherService.searchByName(teacherName.trim());
            if (teachers.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "No teacher found with name: " + teacherName,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                reportPanel = null;
                return;
            }

            Teacher teacher = teachers.get(0);
            List<Course> courses = courseService.getCoursesByTeacherId(teacher.getId());

            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "No courses found for teacher: " + teacherName,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                reportPanel = null;
                return;
            }

            String[] columns = {"Course ID", "Name", "Description", "Credits", "Department"};
            Object[][] data = new Object[courses.size()][columns.length];

            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                data[i][0] = c.getId();
                data[i][1] = c.getName();
                data[i][2] = c.getDescription();
                data[i][3] = c.getCredits();
                data[i][4] = c.getDepartmentName();
            }

            reportPanel = createReportPanel(
                    "Courses Taught by " + teacher.getName(),
                    data,
                    columns,
                    new String[]{
                        "Teacher: " + teacher.getName(),
                        "Department: " + teacher.getDepartmentName(),
                        "Total Courses: " + courses.size()
                    }
            );

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading courses by teacher");
            reportPanel = null;
        }
    }

    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    private JPanel createReportPanel(String title, Object[][] data, String[] columns, String[] summaries) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // عنوان التقرير
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // ملخصات إضافية (معلومات عن المعلم والقسم وعدد المقررات)
        JPanel infoPanel = new JPanel(new GridLayout(summaries.length, 1));
        for (String summary : summaries) {
            infoPanel.add(new JLabel(summary));
        }
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // جدول المقررات
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
