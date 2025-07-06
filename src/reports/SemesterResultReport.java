package reports;

import domain.Enrollment;
import domain.Semester;
import application.StudentService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SemesterResultReport implements Report {
    private final StudentService studentService;
    private JPanel reportPanel;

    public SemesterResultReport(StudentService studentService) {
        this.studentService = studentService;
        this.reportPanel = null;
    }

    @Override
    public void show(Component parent) {
        try {
            String studentName = JOptionPane.showInputDialog(parent,
                    "Enter Student Name:", "Semester Results", JOptionPane.QUESTION_MESSAGE);
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

            Semester semester = ReportUtils.askForSemester(parent);
            if (semester == null) {
                reportPanel = null;
                return;
            }

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

            double gpa = count > 0 ? totalGrades / count : 0.0;

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

    @Override
    public JPanel getReportPanel() {
        return reportPanel;
    }

    private JPanel createReportPanel(String title, Object[][] data, String[] columns,
                                     String line1, String line2, String line3) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        //  العنوان العلوي
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        //  معلومات: الاسم والفصل والمعدل - فوق الجدول
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(line1));
        infoPanel.add(new JLabel(line2));
        infoPanel.add(new JLabel(line3));
        panel.add(infoPanel, BorderLayout.BEFORE_FIRST_LINE); // فوق الجدول مباشرة

        //  الجدول
        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}
