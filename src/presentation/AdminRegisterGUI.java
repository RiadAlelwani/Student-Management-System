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

/**
 * واجهة تسجيل وإدارة المستخدمين من نوع Admin.
 * توفر إمكانية إدخال بيانات مشرف جديد، تعديل بيانات مشرف موجود، حذف مشرف،
 * وعرض جميع المشرفين في جدول تفاعلي.
 */
public class AdminRegisterGUI extends JPanel {
    private static final long serialVersionUID = 1L; // رقم تعريف للسريال (لضمان التوافق عند تسلسل الكائنات)

    /** كائن الوصول للبيانات Admin (التعامل مع قاعدة البيانات) */
    private AdminDAO adminDAO;

    /** جدول لعرض بيانات المشرفين */
    private JTable table;

    /** نموذج الجدول لتخزين البيانات المعروضة */
    private DefaultTableModel tableModel;

    /** حقول إدخال البيانات النصية للاسم، البريد الإلكتروني، العمر، واسم المستخدم */
    private JTextField tfName, tfEmail, tfAge, tfUsername;

    /** قائمة لاختيار الجنس (ذكر/أنثى) */
    private JComboBox<String> cbGender;

    /** حقل لإدخال كلمة المرور */
    private JPasswordField pfPassword;

    /**
     * المُنشئ - يُنشئ الواجهة ويستقبل اتصال قاعدة البيانات.
     * يقوم بتهيئة المكونات الرسومية، تحميل بيانات المشرفين من قاعدة البيانات،
     * وضبط أحداث الأزرار والتفاعل مع الجدول.
     *
     * @param conn اتصال قاعدة البيانات المستخدم لإنشاء AdminDAO
     */
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
        table.setDefaultEditor(Object.class, null);  // تعطيل التعديل المباشر
        // تلوين الصفوف بالتناوب لتحسين المظهر
        presentation.GUIUtils.configureTable(table);
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
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfName.setText(tableModel.getValueAt(row, 0).toString());
                tfEmail.setText(tableModel.getValueAt(row, 1).toString());
                cbGender.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                tfAge.setText(tableModel.getValueAt(row, 3).toString());
                tfUsername.setText(tableModel.getValueAt(row, 4).toString());
                pfPassword.setText(""); // لا يتم عرض كلمة المرور لأسباب أمنية
            }
        });
    }

    /**
     * دالة لإضافة مشرف جديد.
     * تتحقق من صحة البيانات، تشفر كلمة المرور، وتضيف السجل لقاعدة البيانات.
     * بعد الإضافة يتم تحديث الجدول ومسح الحقول.
     */
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
            Admin admin = getAdminFromFields(hashedPassword); // إنشاء كائن المشرف من الحقول
            adminDAO.insert(admin); // إدخاله في قاعدة البيانات
            showMessage("Admin added", false);
            loadData();     // تحديث الجدول
            clearFields();  // مسح الحقول
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * دالة لتحديث مشرف موجود.
     * تقوم بتحديث بيانات المشرف في قاعدة البيانات باستخدام اسم المستخدم كمفتاح.
     * بعد التحديث يتم تحديث الجدول ومسح الحقول.
     */
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

    /**
     * دالة لحذف مشرف من قاعدة البيانات.
     * تتطلب تحديد اسم المستخدم، وبعد الحذف يتم تحديث الجدول ومسح الحقول.
     */
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

    /**
     * دالة لإنشاء كائن Admin من بيانات الحقول المدخلة.
     *
     * @param hashedPassword كلمة المرور المشفرة لاستخدامها في الكائن
     * @return كائن Admin جديد يمثل بيانات الإدخال
     */
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

    /**
     * تحميل جميع بيانات المشرفين من قاعدة البيانات إلى الجدول.
     * يتم مسح المحتويات السابقة وإضافة السجلات الجديدة.
     */
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

    /**
     * مسح جميع الحقول وإلغاء التحديد في الجدول.
     * يستخدم لتنظيف النموذج قبل إدخال بيانات جديدة أو بعد تنفيذ عمليات.
     */
    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        cbGender.setSelectedIndex(0);
        tfAge.setText("");
        tfUsername.setText("");
        pfPassword.setText("");
        table.clearSelection();
    }

    /**
     * عرض رسالة للمستخدم.
     *
     * @param msg  نص الرسالة
     * @param error إذا كانت الرسالة خطأ (true) أو نجاح (false)
     */
    private void showMessage(String msg, boolean error) {
        JOptionPane.showMessageDialog(this, msg, error ? "Error" : "Success",
                error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * عرض رسالة خطأ عند حصول استثناء.
     *
     * @param e الاستثناء المراد عرضه
     */
    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
