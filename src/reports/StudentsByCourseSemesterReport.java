package reports;

import application.StudentService;
import domain.Enrollment;
import domain.Semester;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * تقرير يعرض الطلاب المسجلين في مقرر معين خلال فصل دراسي معين.
 * يقوم التقرير بطلب اسم المقرر والفصل الدراسي، ثم يعرض جدولًا يحتوي بيانات الطلاب المسجلين مع درجاتهم.
 */
public class StudentsByCourseSemesterReport implements Report {
    private final StudentService studentService;
    private JPanel reportPanel;

    /**
     * منشئ التقرير مع خدمة الطلاب.
     *
     * @param studentService خدمة الوصول إلى بيانات الطلاب والتسجيلات
     */
    public StudentsByCourseSemesterReport(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * يعرض التقرير، يطلب من المستخدم إدخال اسم المقرر والفصل الدراسي، ثم يعرض الطلاب المسجلين.
     *
     * @param parent العنصر الأب لعرض النوافذ الحوارية والأخطاء
     */
    @Override
    public void show(Component parent) {
        try {
            String courseName = JOptionPane.showInputDialog(parent, "Enter Course Name:");
            if (courseName == null || courseName.trim().isEmpty()) {
                reportPanel = null;
                return;
            }

            Semester semester = ReportUtils.askForSemester(parent);
            if (semester == null) {
                reportPanel = null;
                return;
            }

            List<Enrollment> enrollments = studentService.getEnrollmentsByCourseAndSemester(
                    courseName.trim(), semester.getSeason(), semester.getYear());

            if (enrollments.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "No students found for " + courseName + " in " + semester,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                reportPanel = null;
                return;
            }

            // تحضير بيانات الطلاب والدرجات للعرض في الجدول
            String[] columns = {"Student Name", "Email", "Gender", "Age", "Major", "GPA", "Grade"};
            Object[][] data = new Object[enrollments.size()][columns.length];

            for (int i = 0; i < enrollments.size(); i++) {
                var s = enrollments.get(i).getStudent();
                double grade = enrollments.get(i).getGrade();
                data[i][0] = s.getName();
                data[i][1] = s.getEmail();
                data[i][2] = s.getGender();
                data[i][3] = s.getAge();
                data[i][4] = s.getMajor();
                data[i][5] = String.format("%.2f", s.getGpa());
                data[i][6] = grade >= 0 ? String.format("%.2f", grade) : "N/A";
            }

            // إنشاء لوحة التقرير مع العنوان والمعلومات والجدول
            reportPanel = createReportPanel(
                    "Students in Course",
                    data,
                    columns,
                    "Course: " + courseName,
                    "Semester: " + semester,
                    "Total Students: " + enrollments.size()
            );

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading course enrollment data");
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
     * @param title   عنوان التقرير
     * @param data    بيانات الجدول (صفوف وأعمدة)
     * @param columns أسماء الأعمدة
     * @param line1   السطر الأول من المعلومات فوق الجدول (مثل اسم المقرر)
     * @param line2   السطر الثاني من المعلومات فوق الجدول (مثل الفصل الدراسي)
     * @param footer  نص الملخص أو المجموع (مثل عدد الطلاب)
     * @return JPanel جاهزة للعرض
     */
    private JPanel createReportPanel(String title, Object[][] data, String[] columns,
                                     String line1, String line2, String footer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // العنوان العلوي للتقرير
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // معلومات نصية فوق الجدول
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(line1));
        infoPanel.add(new JLabel(line2));
        infoPanel.add(new JLabel(footer));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // جدول البيانات داخل JScrollPane
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
