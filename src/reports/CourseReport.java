package reports;

import application.CourseService;
import domain.Course;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * تقرير لعرض بيانات المقررات مع معلومات المعلم والقسم.
 * 
 * يقوم بجلب بيانات المقررات من خدمة CourseService،
 * ثم يعرضها في جدول مع معلومات ملخصة عن عدد المقررات.
 */
public class CourseReport implements Report {
    private final CourseService courseService;
    private JPanel reportPanel;

    /**
     * منشئ التقرير مع ربطه بخدمة المقررات.
     * 
     * @param courseService خدمة للوصول إلى بيانات المقررات
     */
    public CourseReport(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * عرض التقرير داخل مكون واجهة المستخدم الأب.
     * 
     * @param parent المكون الأب (لإظهار رسائل الخطأ أو التنبيهات)
     */
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

    /**
     * استرجاع لوحة التقرير التي تحتوي على جدول البيانات.
     * 
     * @return مكون JPanel يحتوي على التقرير
     */
    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    /**
     * إنشاء لوحة التقرير مع عنوان، جدول بيانات، وملخص.
     * 
     * @param title عنوان التقرير
     * @param data مصفوفة البيانات لعرضها في الجدول
     * @param columns أسماء أعمدة الجدول
     * @param summary ملخص أو معلومات إضافية فوق الجدول
     * @return JPanel يحتوي على التقرير كاملًا
     */
    private JPanel createReportPanel(String title, Object[][] data, String[] columns, String summary) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // عنوان التقرير
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة لعرض معلومات الملخص
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel(summary));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // جدول البيانات مع تمرير الأعمدة والصفوف
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
