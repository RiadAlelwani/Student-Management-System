package reports;

import domain.Semester;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

public class ReportUtils {

    public static final Font DEFAULT_FONT = new Font("Tahoma", Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font("Tahoma", Font.PLAIN, 12);
    public static final Font TITLE_FONT = new Font("Tahoma", Font.PLAIN, 12);

    static {
        UIManager.put("Button.font", DEFAULT_FONT);
        UIManager.put("Label.font", DEFAULT_FONT);
    }

    public static JPanel createReportPanel(String title, Object[][] data, String[] columns,
                                         String header1, String header2, String footer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // إنشاء الجدول
        JTable table = new JTable(data, columns);
        table.setFont(DEFAULT_FONT);
        table.setRowHeight(25);
        table.getTableHeader().setFont(BOLD_FONT);

        // إنشاء الترويسة العلوية
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        if (title != null) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(TITLE_FONT);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));
        }
        
        if (header1 != null) {
            JLabel l1 = new JLabel(header1);
            l1.setFont(DEFAULT_FONT);
            headerPanel.add(l1);
        }
        if (header2 != null) {
            JLabel l2 = new JLabel(header2);
            l2.setFont(DEFAULT_FONT);
            headerPanel.add(l2);
        }
        if (footer != null) {
            JLabel l3 = new JLabel(footer);
            l3.setFont(DEFAULT_FONT);
            headerPanel.add(l3);
        }

        // زر الطباعة
        JButton printButton = new JButton("Print Report");
        printButton.setFont(DEFAULT_FONT);
        printButton.addActionListener(e -> {
            try {
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat(header1 + " - " + header2),
                        new MessageFormat(footer != null ? footer : ""));
                if (!complete) {
                    JOptionPane.showMessageDialog(panel, "Printing was cancelled",
                            "Print", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (PrinterException ex) {
                showError(panel, ex, "Printing failed");
            }
        });

        // تجميع العناصر
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(printButton, BorderLayout.EAST);
        
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void showError(Component parent, Exception ex, String msg) {
        JOptionPane.showMessageDialog(parent,
                msg + ": " + (ex != null ? ex.getMessage() : "Unknown error"),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static Semester askForSemester(Component parent) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JComboBox<String> seasonCombo = new JComboBox<>(new String[]{"Spring", "Summer", "Fall", "Winter"});
        JTextField yearField = new JTextField(String.valueOf(java.time.Year.now().getValue()));

        panel.add(new JLabel("Season:"));
        panel.add(seasonCombo);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(parent, panel,
                "Select Semester", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                return new Semester((String) seasonCombo.getSelectedItem(),
                        Integer.parseInt(yearField.getText()));
            } catch (NumberFormatException e) {
                showError(parent, e, "Invalid year format");
            }
        }
        return null;
    }
}