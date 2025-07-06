package reports;

import domain.Semester;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

/**
 * فئة تحتوي على أدوات مساعدة لإنشاء وعرض التقارير داخل واجهة Swing.
 * تشمل إنشاء اللوحات، عرض رسائل الخطأ، وطلب تحديد فصل دراسي من المستخدم.
 */
public class ReportUtils {

    /**
     * ينشئ JPanel يحتوي على تقرير مكوّن من جدول بيانات مع رؤوس وأزرار.
     * 
     * @param title      عنوان التقرير الظاهر في أعلى اللوحة (يمكن أن يكون null)
     * @param data       مصفوفة بيانات الجدول (صفوف وأعمدة)
     * @param columns    أسماء أعمدة الجدول
     * @param header1    نص ترويستي أول أعلى الجدول (يمكن أن يكون null)
     * @param header2    نص ترويستي ثاني أعلى الجدول (يمكن أن يكون null)
     * @param footer     نص ترويستي أسفل الجدول (يمكن أن يكون null)
     * @return           JPanel يحتوي التقرير كاملاً مع الجدول وزر الطباعة
     */
    public static JPanel createReportPanel(String title, Object[][] data, String[] columns,
                                           String header1, String header2, String footer) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // إنشاء الجدول من البيانات والأعمدة
        JTable table = new JTable(data, columns);
        table.setRowHeight(25);

        // إنشاء لوحة الترويسة العلوية (العنوان والنصوص الترويسية)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        if (title != null) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(titleLabel);
            headerPanel.add(Box.createVerticalStrut(10));  // مسافة رأسية بين العنوان وباقي العناصر
        }

        if (header1 != null) {
            JLabel l1 = new JLabel(header1);
            headerPanel.add(l1);
        }
        if (header2 != null) {
            JLabel l2 = new JLabel(header2);
            headerPanel.add(l2);
        }
        if (footer != null) {
            JLabel l3 = new JLabel(footer);
            headerPanel.add(l3);
        }

        // زر الطباعة مع التعامل مع الاستثناءات أثناء الطباعة
        JButton printButton = new JButton("Print Report");
        printButton.addActionListener(e -> {
            try {
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat(header1 + " - " + header2),
                        new MessageFormat(footer != null ? footer : ""));
                if (!complete) {
                    JOptionPane.showMessageDialog(panel, "تم إلغاء الطباعة",
                            "طباعة", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (PrinterException ex) {
                showError(panel, ex, "فشل في الطباعة");
            }
        });

        // تجميع الترويسة العلوية وزر الطباعة والجدول في لوحة التقرير
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(printButton, BorderLayout.EAST);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * يعرض نافذة حوارية لرسالة خطأ مع تفاصيل الاستثناء.
     * 
     * @param parent العنصر الأب لعرض النافذة فوقه (مثل JFrame أو JPanel)
     * @param ex     الاستثناء الذي وقع (يمكن أن يكون null)
     * @param msg    رسالة توضيحية للخطأ لعرضها للمستخدم
     */
    public static void showError(Component parent, Exception ex, String msg) {
        JOptionPane.showMessageDialog(parent,
                msg + ": " + (ex != null ? ex.getMessage() : "خطأ غير معروف"),
                "خطأ", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * يعرض نافذة حوارية تطلب من المستخدم اختيار فصل دراسي (موسم وسنة).
     * 
     * @param parent العنصر الأب لعرض النافذة فوقه
     * @return       كائن Semester يحتوي على الموسم والسنة المختارة، أو null إذا ألغى المستخدم أو حدث خطأ
     */
    public static Semester askForSemester(Component parent) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JComboBox<String> seasonCombo = new JComboBox<>(new String[]{"Spring", "Summer", "Fall", "Winter"});
        JTextField yearField = new JTextField(String.valueOf(java.time.Year.now().getValue()));

        panel.add(new JLabel("الموسم:"));
        panel.add(seasonCombo);
        panel.add(new JLabel("السنة:"));
        panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(parent, panel,
                "اختيار الفصل الدراسي", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                return new Semester((String) seasonCombo.getSelectedItem(),
                        Integer.parseInt(yearField.getText()));
            } catch (NumberFormatException e) {
                showError(parent, e, "تنسيق السنة غير صحيح");
            }
        }
        return null;
    }
}
