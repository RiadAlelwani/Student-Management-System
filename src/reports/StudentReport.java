package reports;

import application.StudentService;
import domain.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * تقرير يعرض قائمة جميع الطلاب مع تفاصيلهم.
 * يتم عرض التقرير في JPanel يحتوي جدولًا ومعلومات موجزة عن العدد الإجمالي للطلاب.
 */
public class StudentReport implements Report {
    private final StudentService studentService;
    private JPanel reportPanel;

    /**
     * منشئ التقرير مع خدمة الطلاب.
     * 
     * @param studentService خدمة الوصول إلى بيانات الطلاب
     */
    public StudentReport(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * يعرض التقرير بإنشاء جدول يحتوي بيانات جميع الطلاب.
     * 
     * @param parent العنصر الأب لعرض النوافذ الحوارية والأخطاء
     */
    @Override
    public void show(Component parent) {
        try {
            List<Student> students = studentService.getAll();
            String[] columns = {"ID", "Name", "Email", "Gender", "Age", "Major", "GPA"};
            Object[][] data = new Object[students.size()][columns.length];

            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                data[i][0] = s.getId();
                data[i][1] = s.getName();
                data[i][2] = s.getEmail();
                data[i][3] = s.getGender();
                data[i][4] = s.getAge();
                data[i][5] = s.getMajor();
                data[i][6] = String.format("%.2f", s.getGpa());
            }

            reportPanel = createReportPanel(
                    "Student Report",
                    data,
                    columns,
                    "Total Students: " + students.size()
            );

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading student data");
            reportPanel = null;
        }
    }

    /**
     * إرجاع لوحة التقرير التي تحتوي الجدول والبيانات، أو null إذا لم يتم إنشاؤها.
     * 
     * @return JPanel التقرير
     */
    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    /**
     * ينشئ لوحة تقرير تحتوي جدول الطلاب مع عنوان ومعلومات موجزة.
     * 
     * @param title عنوان التقرير
     * @param data بيانات الجدول (صفوف وأعمدة)
     * @param columns أسماء الأعمدة
     * @param summary نص ملخص يظهر فوق الجدول (مثل عدد الطلاب)
     * @return JPanel جاهزة للعرض
     */
    private JPanel createReportPanel(String title, Object[][] data, String[] columns, String summary) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // عنوان التقرير
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // ملخص المعلومات أعلى الجدول مباشرة
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel(summary));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // جدول الطلاب مع تمرير شريط التمرير
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
