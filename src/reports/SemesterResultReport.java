package reports;

import domain.Enrollment;
import domain.Semester;
import application.StudentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * تقرير نتائج فصل دراسي لطالب معين.
 * يقوم المستخدم بإدخال اسم الطالب واختيار فصل دراسي، ثم يعرض جدول المقررات المسجلة في ذلك الفصل مع الدرجات والمعدل التراكمي GPA.
 */
public class SemesterResultReport implements Report {
    private final StudentService studentService;
    private JPanel reportPanel;

    /**
     * منشئ التقرير مع خدمة الطلاب.
     * 
     * @param studentService خدمة الوصول إلى بيانات الطلاب والتسجيلات
     */
    public SemesterResultReport(StudentService studentService) {
        this.studentService = studentService;
        this.reportPanel = null;
    }

    /**
     * يعرض التقرير عبر نافذة حوارية لأخذ اسم الطالب والفصل الدراسي، ثم يُنشئ اللوحة المناسبة للعرض.
     * 
     * @param parent العنصر الأب لعرض النوافذ الحوارية فوقه
     */
    @Override
    public void show(Component parent) {
        try {
            // طلب اسم الطالب
            String studentName = JOptionPane.showInputDialog(parent,
                    "Enter Student Name:", "Semester Results", JOptionPane.QUESTION_MESSAGE);
            if (studentName == null || studentName.trim().isEmpty()) {
                reportPanel = null;
                return;
            }

            // جلب التسجيلات الخاصة بالطالب
            List<Enrollment> enrollments = studentService.getEnrollmentsByStudentName(studentName.trim());
            if (enrollments.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "No enrollments found for student: " + studentName,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                reportPanel = null;
                return;
            }

            // طلب اختيار فصل دراسي من المستخدم
            Semester semester = ReportUtils.askForSemester(parent);
            if (semester == null) {
                reportPanel = null;
                return;
            }

            // تصفية التسجيلات حسب الفصل الدراسي المختار
            List<Enrollment> filtered = enrollments.stream()
                    .filter(e -> e.getSemester().equals(semester))
                    .toList();

            if (filtered.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                        "No records found for semester: " + semester,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                reportPanel = null;
                return;
            }

            // تجهيز بيانات الجدول
            String[] columns = {"Course", "Teacher", "Grade"};
            Object[][] data = new Object[filtered.size()][columns.length];
            double totalGrades = 0;
            int count = 0;

            for (int i = 0; i < filtered.size(); i++) {
                Enrollment enr = filtered.get(i);
                data[i][0] = enr.getCourse().getName();
                data[i][1] = enr.getTeacher().getName();
                double grade = enr.getGrade();
                data[i][2] = grade >= 0 ? String.format("%.2f", grade) : "N/A";

                if (grade >= 0) {
                    totalGrades += grade;
                    count++;
                }
            }

            // حساب المعدل التراكمي GPA
            double gpa = count > 0 ? totalGrades / count : 0.0;

            // إنشاء لوحة التقرير
            reportPanel = createReportPanel(
                    "Semester Results",
                    data, columns,
                    "Student: " + studentName,
                    "Semester: " + semester.toString(),
                    "GPA: " + String.format("%.2f", gpa)
            );

        } catch (Exception ex) {
            ReportUtils.showError(parent, ex, "Error loading semester results");
            reportPanel = null;
        }
    }

    /**
     * إرجاع لوحة التقرير التي تم إنشاؤها بعد استدعاء show().
     * 
     * @return JPanel يحتوي تقرير نتائج الفصل الدراسي أو null إذا لم يتم إنشاء التقرير
     */
    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    /**
     * ينشئ لوحة تقرير مهيكلة مع العنوان، معلومات الطالب، الجدول، والمعلومات الإضافية.
     * 
     * @param title عنوان التقرير
     * @param data بيانات الجدول
     * @param columns أسماء أعمدة الجدول
     * @param line1 نص معلومات أول (اسم الطالب)
     * @param line2 نص معلومات ثاني (الفصل الدراسي)
     * @param line3 نص معلومات ثالث (المعدل التراكمي)
     * @return JPanel جاهزة للعرض تحتوي التقرير كاملًا
     */
    private JPanel createReportPanel(String title, Object[][] data, String[] columns,
                                     String line1, String line2, String line3) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // العنوان العلوي
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // معلومات: الاسم والفصل والمعدل - فوق الجدول
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(line1));
        infoPanel.add(new JLabel(line2));
        infoPanel.add(new JLabel(line3));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE); // فوق الجدول مباشرة

        // الجدول مع تمرير الشريط
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
