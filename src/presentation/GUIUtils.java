package presentation;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * فئة مساعدة تحتوي على أدوات وإعدادات شائعة الاستخدام في واجهات المستخدم الرسومية (GUI).
 * توفر دوال لتنسيق الجداول، عرض رسائل الخطأ، إعداد حقول البحث، وإعداد تخطيطات GridBagLayout بسهولة.
 */
public class GUIUtils {

    /**
     * إعداد جدول ليكون غير قابل للتعديل ويسمح بتحديد صف واحد فقط.
     * كما يقوم بتلوين الصفوف بالتناوب بلونين محددين لتحسين المظهر.
     *
     * @param table جدول Swing الذي سيتم إعداده
     */
    public static void configureTable(JTable table) {
        table.setDefaultEditor(Object.class, null); // منع التعديل على الخلايا
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // تحديد صف واحد فقط
        setAlternatingRowColors(table, Color.WHITE, new Color(240, 240, 240)); // ألوان الصفوف بالتناوب
    }

    /**
     * تلوين الصفوف بالتناوب بلونين مختلفين.
     * الصفوف الزوجية تأخذ اللون الأول، والصفوف الفردية تأخذ اللون الثاني.
     * يتم تجاهل هذا التلوين عندما يكون الصف محددًا.
     *
     * @param table جدول Swing الذي سيتم تلوين صفوفه
     * @param color1 اللون الأول للصفوف الزوجية
     * @param color2 اللون الثاني للصفوف الفردية
     */
    public static void setAlternatingRowColors(JTable table, Color color1, Color color2) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                private static final long serialVersionUID = 1L;

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (!isSelected) {
                        if (row % 2 == 0) {
                            c.setBackground(color1);
                        } else {
                            c.setBackground(color2);
                        }
                    } else {
                        c.setBackground(table.getSelectionBackground());
                    }
                    return c;
                }
            });
        }
    }

    /**
     * عرض رسالة خطأ في نافذة منبثقة (Dialog).
     *
     * @param parent المكون الأب الذي تظهر عليه النافذة
     * @param message نص رسالة الخطأ التي سيتم عرضها
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * إعداد حقل بحث (JTextField) ليتم تنفيذ إجراء معين عند تغير النص فيه.
     * هذا مفيد لتحديث نتائج البحث تلقائيًا أثناء الكتابة.
     *
     * @param searchField حقل النص الذي يتم مراقبته
     * @param searchAction الإجراء الذي سيتم تنفيذه عند تغيير النص
     */
    public static void setupSearchField(JTextField searchField, Runnable searchAction) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchAction.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchAction.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchAction.run(); }
        });
    }

    /**
     * إضافة مستمع نقر (MouseListener) لجدول بحيث يتم تنفيذ إجراء معين عند اختيار صف.
     *
     * @param table جدول Swing الذي سيتم إضافة المستمع له
     * @param action الإجراء الذي سيتم تنفيذه عند النقر على الصف
     */
    public static void addTableSelectionListener(JTable table, Runnable action) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
    }

    /**
     * إنشاء كائن GridBagConstraints مع إعدادات افتراضية.
     * يحدد الموقع في شبكة GridBagLayout ويضيف حشوة وإعداد ملء أفقي.
     *
     * @param x موقع العمود (gridx)
     * @param y موقع الصف (gridy)
     * @return كائن GridBagConstraints مُعد مسبقًا للاستخدام
     */
    public static GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
}
