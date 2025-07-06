package presentation;

import application.CourseService;
import application.StudentService;
import application.TeacherService;
import persistence.DBConnection;
import reports.*;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.text.MessageFormat;

/**
 * ReportGUI هي واجهة رسومية رئيسية لعرض تقارير مختلفة في النظام.
 * تشمل تقارير عن الطلاب، المعلمين، الدورات، والتسجيلات.
 * تحتوي على لوحة أزرار لاختيار التقرير ولوحة عرض لعرض نتائج التقرير المحدد.
 */
public class ReportGUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.PLAIN, 12);
    private static final Font TABLE_FONT = new Font("Tahoma", Font.PLAIN, 12);
    private static final Dimension BUTTON_SPACING = new Dimension(0, 8);

    private StudentService studentService;
    private TeacherService teacherService;
    private CourseService courseService;

    private JPanel reportDisplayPanel;
    private JPanel currentReportPanel;

    // زر التقرير المختار حالياً (لتمييزه)
    private JButton activeButton = null;

    public ReportGUI() {
        initServices();
        initUI();
    }

    private void initServices() {
        try {
            Connection conn = DBConnection.getConnection();
            this.studentService = new StudentService(conn);
            this.teacherService = new TeacherService(conn);
            this.courseService = new CourseService(conn);
        } catch (Exception ex) {
            ReportUtils.showError(this, ex, "Failed to connect to database");
            throw new RuntimeException("Failed to initialize services", ex);
        }
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createReportsPanel(), BorderLayout.WEST);
        add(createReportDisplayPanel(), BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("\uD83D\uDCCA Reports Module");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createReportsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addReportButton(mainPanel, "\uD83D\uDCCB Student Report", () -> showReport(new StudentReport(studentService)));
        addReportButton(mainPanel, "\uD83D\uDCCB Teacher Report", () -> showReport(new TeacherReport(teacherService)));
        addReportButton(mainPanel, "\uD83D\uDCCB Course Report", () -> showReport(new CourseReport(courseService)));
        addReportButton(mainPanel, "\uD83D\uDCCB Student Enrollment Report", () -> showReport(new StudentEnrollmentReport(studentService)));
        addReportButton(mainPanel, "\uD83D\uDCCB Courses By Teacher", () -> showReport(new CoursesByTeacherReport(courseService, teacherService)));
        addReportButton(mainPanel, "\uD83D\uDCCB Students By Course & Semester", () -> showReport(new StudentsByCourseSemesterReport(studentService)));
        addReportButton(mainPanel, "\uD83D\uDCCB Semester Result Report", () -> showReport(new SemesterResultReport(studentService)));

        return mainPanel;
    }

    private void addReportButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(e -> {
            action.run();
            if (activeButton != null) {
                activeButton.setEnabled(true);
            }
            button.setEnabled(false);
            activeButton = button;
        });
        panel.add(button);
        panel.add(Box.createRigidArea(BUTTON_SPACING));
    }

    private JPanel createReportDisplayPanel() {
        reportDisplayPanel = new JPanel(new BorderLayout());
        reportDisplayPanel.setBorder(BorderFactory.createTitledBorder("Report View"));
        return reportDisplayPanel;
    }

    private void showReport(Report report) {
        try {
            report.show(this);
            JPanel reportPanel = report.getReportPanel();

            if (reportPanel == null) {
                ReportUtils.showError(this, null, "Report panel is empty");
                return;
            }

            currentReportPanel = reportPanel;
            applyReportFonts(currentReportPanel);

            reportDisplayPanel.removeAll();

            JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton printButton = new JButton("\uD83D\uDDA8 Print");
            printButton.setFont(BUTTON_FONT);
            printButton.addActionListener(e -> printCurrentReport());
            printPanel.add(printButton);

            reportDisplayPanel.add(currentReportPanel, BorderLayout.CENTER);
            reportDisplayPanel.add(printPanel, BorderLayout.SOUTH);

            reportDisplayPanel.revalidate();
            reportDisplayPanel.repaint();

        } catch (Exception ex) {
            ReportUtils.showError(this, ex, "Error displaying report");
        }
    }

    private void applyReportFonts(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(TABLE_FONT);
            } else if (comp instanceof JTable) {
                ((JTable) comp).setFont(TABLE_FONT);
                ((JTable) comp).getTableHeader().setFont(TABLE_FONT.deriveFont(Font.BOLD));
            } else if (comp instanceof Container) {
                applyReportFonts((Container) comp);
            }
        }
    }

    private void printCurrentReport() {
        if (currentReportPanel == null) {
            JOptionPane.showMessageDialog(this, "No report to print", "Print Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTable tableToPrint = findTableInPanel(currentReportPanel);
        if (tableToPrint == null) {
            JOptionPane.showMessageDialog(this, "No table found in the report to print", "Print Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean complete = tableToPrint.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Report Print"),
                    new MessageFormat("Page - {0}"));
            if (!complete) {
                JOptionPane.showMessageDialog(this, "Printing was cancelled", "Print", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTable findTableInPanel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTable) return (JTable) comp;
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JTable) return (JTable) view;
            }
            if (comp instanceof Container) {
                JTable result = findTableInPanel((Container) comp);
                if (result != null) return result;
            }
        }
        return null;
    }
}
