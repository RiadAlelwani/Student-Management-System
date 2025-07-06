package presentation;  // الحزمة التي ينتمي إليها الكلاس، تمثل طبقة العرض (واجهة المستخدم)

import persistence.AdminDAO;         // DAO للوصول إلى قاعدة البيانات الخاصة بالمشرفين
import domain.Admin;                 // كائن يمثل المشرف
import infrastructure.PasswordUtil; // أداة لتشفير كلمات المرور

import javax.swing.*;                // مكتبة Java لإنشاء الواجهات الرسومية
import javax.swing.table.DefaultTableModel; // نموذج الجدول لعرض البيانات
import java.awt.*;                   // مكتبة للتحكم في التنسيقات والتصميمات
import java.awt.event.*;            // مكتبة للتعامل مع الأحداث (كالضغط على الأزرار)
import java.sql.Connection;         // مكتبة للتعامل مع الاتصال بقاعدة البيانات
import java.util.List;              // قائمة لتخزين بيانات المشرفين

// واجهة تسجيل وإدارة المستخدمين من نوع Admin
public class AdminRegisterGUI extends JPanel {
    private static final long serialVersionUID = 1L; // رقم تعريف للسريال (لضمان التوافق)

    private AdminDAO adminDAO;  // كائن الوصول للبيانات Admin (التعامل مع قاعدة البيانات)

    // مكونات الواجهة الرسومية
    private JTable table;                        // جدول لعرض بيانات المشرفين
    private DefaultTableModel tableModel;        // نموذج الجدول لتخزين البيانات المعروضة
    private JTextField tfName, tfEmail, tfAge, tfUsername; // حقول إدخال البيانات النصية
    private JComboBox<String> cbGender;          // قائمة لاختيار الجنس (ذكر/أنثى)
    private JPasswordField pfPassword;           // حقل لإدخال كلمة المرور

    // المُنشئ - يُنشئ الواجهة ويستقبل اتصال قاعدة البيانات
    public AdminRegisterGUI(Connection conn) {
        setLayout(new BorderLayout()); // استخدام تصميم BorderLayout لتقسيم الواجهة

        try {
            adminDAO = new AdminDAO(conn); // تهيئة DAO للاتصال بقاعدة البيانات
        } catch (Exception e) {
            showError(e); // في حال فشل الاتصال يتم عرض رسالة الخطأ
            return;
        }

        // ====== الجزء العلوي: النموذج لإدخال البيانات ======
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout()); // تصميم مرن للمكونات
        inputPanel.setBorder(BorderFactory.createTitledBorder("Admin Info")); // عنوان للوحة الإدخال
        GridBagConstraints gbc = new GridBagConstraints(); // إعدادات التصميم الداخلي
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // إنشاء الحقول
        tfName = new JTextField(15);
        tfEmail = new JTextField(15);
        cbGender = new JComboBox<>(new String[]{"Male", "Female"});
        tfAge = new JTextField(15);
        tfUsername = new JTextField(15);
        pfPassword = new JPasswordField(15);

        // إضافة الحقول والتسميات باستخدام GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfName, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        inputPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbGender, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfAge, gbc);

        gbc.gridx = 0; gbc.gridy++;
        inputPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfUsername, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(pfPassword, gbc);

        // إضافة لوحة الإدخال إلى الجزء العلوي
        topPanel.add(inputPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // ====== الجزء الأوسط: جدول عرض بيانات المشرفين ======
        tableModel = new DefaultTableModel(new String[]{"Name", "Email", "Gender", "Age", "Username"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER); // جدول داخل Scroll Pane لتفعيل التمرير

        // ====== الجزء السفلي: أزرار العمليات ======
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");       // زر إضافة مشرف
        JButton updateBtn = new JButton("Update"); // زر تعديل مشرف
        JButton deleteBtn = new JButton("Delete"); // زر حذف مشرف
        JButton clearBtn = new JButton("Clear");   // زر مسح الحقول

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.SOUTH); // إضافة الأزرار أسفل الواجهة

        // تحميل البيانات عند بدء التشغيل
        loadData();

        // تعيين أحداث الأزرار
        addBtn.addActionListener(e -> insertAdmin());
        updateBtn.addActionListener(e -> updateAdmin());
        deleteBtn.addActionListener(e -> deleteAdmin());
        clearBtn.addActionListener(e -> clearFields());

        // عند الضغط على صف في الجدول يتم تعبئة الحقول تلقائيًا
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfName.setText(tableModel.getValueAt(row, 0).toString());
                tfEmail.setText(tableModel.getValueAt(row, 1).toString());
                cbGender.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                tfAge.setText(tableModel.getValueAt(row, 3).toString());
                tfUsername.setText(tableModel.getValueAt(row, 4).toString());
                pfPassword.setText(""); // لا يتم عرض كلمة المرور
            }
        });
    }

    // دالة لإضافة مشرف جديد
    private void insertAdmin() {
        try {
            String username = tfUsername.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                showMessage("Username and password required", true);
                return;
            }

            if (adminDAO.existsByUsername(username)) {
                showMessage("Username already exists", true);
                return;
            }

            String hashedPassword = PasswordUtil.hashPassword(password); // تشفير كلمة المرور
            Admin admin = getAdminFromFields(hashedPassword); // إنشاء كائن المشرف
            adminDAO.insert(admin); // إدخاله في قاعدة البيانات
            showMessage("Admin added", false);
            loadData();     // تحديث الجدول
            clearFields();  // مسح الحقول
        } catch (Exception e) {
            showError(e);
        }
    }

    // دالة لتحديث مشرف موجود
    private void updateAdmin() {
        try {
            String username = tfUsername.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();

            if (username.isEmpty()) {
                showMessage("Select a record to update", true);
                return;
            }

            String hashedPassword = PasswordUtil.hashPassword(password);
            Admin admin = getAdminFromFields(hashedPassword);
            adminDAO.update(admin);
            showMessage("Admin updated", false);
            loadData();
            clearFields();
        } catch (Exception e) {
            showError(e);
        }
    }

    // دالة لحذف مشرف من قاعدة البيانات
    private void deleteAdmin() {
        try {
            String username = tfUsername.getText().trim();

            if (username.isEmpty()) {
                showMessage("Select a record to delete", true);
                return;
            }

            adminDAO.delete(username);
            showMessage("Admin deleted", false);
            loadData();
            clearFields();
        } catch (Exception e) {
            showError(e);
        }
    }

    // دالة لإنشاء كائن Admin من بيانات الحقول
    private Admin getAdminFromFields(String hashedPassword) {
        return new Admin(
                tfName.getText().trim(),
                tfEmail.getText().trim(),
                cbGender.getSelectedItem().toString(),
                Integer.parseInt(tfAge.getText().trim()),
                tfUsername.getText().trim(),
                hashedPassword
        );
    }

    // تحميل جميع بيانات المشرفين من قاعدة البيانات إلى الجدول
    private void loadData() {
        try {
            tableModel.setRowCount(0); // مسح الجدول
            List<Admin> list = adminDAO.getAll(); // جلب البيانات
            for (Admin a : list) {
                tableModel.addRow(new Object[]{
                        a.getName(), a.getEmail(), a.getGender(), a.getAge(), a.getUsername()
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    // مسح جميع الحقول وإلغاء التحديد في الجدول
    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        cbGender.setSelectedIndex(0);
        tfAge.setText("");
        tfUsername.setText("");
        pfPassword.setText("");
        table.clearSelection();
    }

    // عرض رسالة للمستخدم
    private void showMessage(String msg, boolean error) {
        JOptionPane.showMessageDialog(this, msg, error ? "Error" : "Success",
                error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    // عرض رسالة خطأ عند حصول استثناء
    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
