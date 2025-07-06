package presentation;

import domain.Semester;
import infrastructure.SemesterNotifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import application.SemesterService;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

/**
 * واجهة المستخدم الرسومية لإدارة فصول الدراسة (Semesters).
 * تتيح إضافة، تعديل، حذف وعرض فصول الدراسة من قاعدة البيانات.
 * تدعم إشعار المراقبين (Observers) عند حدوث تغييرات على البيانات.
 */
public class SemesterGUI extends JPanel {

    private static final long serialVersionUID = 1L;

    /** خدمة الفصل الدراسي للتعامل مع قاعدة البيانات */
    private final SemesterService semesterService;

    /** جدول لعرض بيانات الفصول */
    private final JTable table;

    /** نموذج بيانات الجدول */
    private final DefaultTableModel tableModel;

    /** حقل إدخال السنة */
    private final JTextField tfYear;

    /** قائمة منسدلة لاختيار الفصل الدراسي (الفصل) */
    private final JComboBox<String> cbTerm;

    /** خانة اختيار لحالة الفتح (مفتوح/مغلق) */
    private final JCheckBox cbIsOpen;

    /**
     * المُنشئ الذي يهيئ الواجهة، يتلقى اتصال قاعدة البيانات.
     * يقوم بتهيئة المكونات، تحميل البيانات، وتوصيل الأحداث.
     *
     * @param conn اتصال قاعدة البيانات
     */
    public SemesterGUI(Connection conn) {
        setLayout(new BorderLayout());

        try {
            semesterService = new SemesterService(conn);
        } catch (Exception e) {
            showError(e);
            throw new RuntimeException(e);
        }

        // إنشاء مكونات الإدخال
        cbTerm = new JComboBox<>(new String[]{"Fall", "Spring", "Summer"});
        tfYear = new JTextField(15);
        cbIsOpen = new JCheckBox("Is Open");

        // إعداد لوحة الإدخال
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Semester Info"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Term:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbTerm, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfYear, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbIsOpen, gbc);

        topPanel.add(inputPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // إعداد الجدول لعرض الفصول الدراسية
        tableModel = new DefaultTableModel(new String[]{"ID", "Term", "Year", "Is Open"}, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);  // جعل الجدول غير قابل للتعديل مباشرة
     // تلوين الصفوف بالتناوب لتحسين المظهر
        presentation.GUIUtils.configureTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // أزرار العمليات
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // تحميل البيانات من قاعدة البيانات وعرضها في الجدول
        loadData();

        // حدث زر الإضافة: إضافة فصل جديد ثم إعلام المراقبين وتحديث العرض
        addBtn.addActionListener(e -> {
            try {
                Semester s = new Semester(
                        0,
                        cbTerm.getSelectedItem().toString(),
                        Integer.parseInt(tfYear.getText()),
                        cbIsOpen.isSelected()
                );
                semesterService.add(s);
                SemesterNotifier.notifyAllObservers();  // إشعار المراقبين بالتغيير
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // حدث زر التحديث: تعديل الفصل المحدد في الجدول
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                Semester s = new Semester(
                        id,
                        cbTerm.getSelectedItem().toString(),
                        Integer.parseInt(tfYear.getText()),
                        cbIsOpen.isSelected()
                );
                semesterService.update(s);
                SemesterNotifier.notifyAllObservers();
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // حدث زر الحذف: حذف الفصل المحدد
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                semesterService.delete(id);
                SemesterNotifier.notifyAllObservers();
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // زر مسح الحقول
        clearBtn.addActionListener(e -> clearFields());

        // عند النقر على صف في الجدول، تعبئة الحقول بالقيم المختارة
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                cbTerm.setSelectedItem(tableModel.getValueAt(row, 1).toString());
                tfYear.setText(tableModel.getValueAt(row, 2).toString());
                cbIsOpen.setSelected((Boolean) tableModel.getValueAt(row, 3));
            }
        });
    }

    /**
     * تحميل جميع بيانات الفصول الدراسية من الخدمة وعرضها في الجدول.
     */
    private void loadData() {
        try {
            List<Semester> list = semesterService.getAll();
            fillTable(list);
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * ملء بيانات الجدول بقائمة الفصول المعطاة.
     *
     * @param list قائمة الفصول التي سيتم عرضها
     */
    private void fillTable(List<Semester> list) {
        tableModel.setRowCount(0); // تنظيف الجدول قبل الإضافة
        for (Semester s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getSeason(),
                    s.getYear(),
                    s.isOpen()
            });
        }
    }

    /**
     * مسح حقول الإدخال وإلغاء اختيار الصفوف في الجدول.
     */
    private void clearFields() {
        cbTerm.setSelectedIndex(0);
        tfYear.setText("");
        cbIsOpen.setSelected(false);
        table.clearSelection();
    }

    /**
     * عرض رسالة خطأ في نافذة منبثقة وطباعتها في الكونسول.
     *
     * @param e الاستثناء الذي وقع
     */
    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
