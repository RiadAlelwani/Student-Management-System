package reports;

import application.StudentService;
import domain.Enrollment;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * تقرير تسجيلات طالب معين.
 * يسمح للمستخدم بإدخال اسم الطالب، ثم يعرض جميع التسجيلات (المقررات، المعلم، الفصل، الدرجة) الخاصة به.
 */
public class StudentEnrollmentReport implements Report {
    private final StudentService studentService;
    private JPanel reportPanel;

    /**
     * منشئ التقرير مع خدمة الطلاب.
     * 
     * @param studentService خدمة الوصول إلى بيانات الطلاب والتسجيلات
     */
    public StudentEnrollmentReport(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * يعرض التقرير عبر نافذة حوارية لأخذ اسم الطالب، ثم يُنشئ لوحة تقرير تعرض تسجيلات الطالب.
     * 
     * @param parent العنصر الأب لعرض النوافذ الحوارية فوقه
     */
    @Override
    public void show(Component parent) {
        try {
            String studentName = JOptionPane.showInputDialog(parent, 
                    "Enter Student Name:", "Student Enrollments", JOptionPane.QUESTION_MESSAGE);
            if (studentName == null || studentName.trim().isEmpty()) {
                reportPanel = null;
                return;
            }

            List<Enrollment> enrollments = studentService.getEnrollmentsByStudentName(studentName.trim());
            if (enrollments.isEmpty()) {
                JOptionPane.showMessageDialog(parent, 
                        "No enrollments found for student: " + studentName, 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                reportPanel = null;
                return;
            }

            reportPanel = createReportPanel(studentName, enrollments);

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading enrollments");
            reportPanel = null;
        }
    }

    /**
     * إرجاع لوحة التقرير التي تم إنشاؤها بعد استدعاء show().
     * 
     * @return JPanel يحتوي تقرير التسجيلات أو null إذا لم يتم إنشاء التقرير
     */
    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    /**
     * ينشئ لوحة تقرير تحتوي جدول تسجيلات الطالب مع معلومات موجزة.
     * 
     * @param studentName اسم الطالب
     * @param enrollments قائمة تسجيلات الطالب
     * @return JPanel جاهزة للعرض تحتوي التقرير
     */
    private JPanel createReportPanel(String studentName, List<Enrollment> enrollments) {
        String[] columns = {"Course", "Teacher", "Semester", "Grade"};
        Object[][] data = new Object[enrollments.size()][columns.length];

        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enr = enrollments.get(i);
            data[i][0] = enr.getCourse().getName();
            data[i][1] = enr.getTeacher().getName();
            data[i][2] = enr.getSemester().toString();
            data[i][3] = enr.getGrade() >= 0 ? String.format("%.2f", enr.getGrade()) : "N/A";
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // العنوان الرئيسي
        JLabel titleLabel = new JLabel("Enrollments for " + studentName);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // معلومات الطالب أعلى الجدول
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel("Student: " + studentName));
        infoPanel.add(new JLabel("Total Enrollments: " + enrollments.size()));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE);

        // الجدول مع تمرير شريط
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
