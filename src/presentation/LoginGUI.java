package presentation; // يشير إلى أن هذا الكلاس من طبقة العرض (واجهة المستخدم)

// استيراد الكائنات الضرورية
import domain.Admin;                      // الكلاس الذي يمثل مسؤول النظام
import application.AdminService;         // كلاس الخدمة الذي يتعامل مع قواعد البيانات الخاصة بالمسؤول

import javax.swing.*;                    // مكونات الواجهة الرسومية
import java.awt.*;                       // لتنسيق النوافذ والمكونات
import java.awt.event.ActionEvent;       // لمعالجة حدث الضغط على زر
import java.sql.Connection;              // لتمرير الاتصال بقاعدة البيانات

/**
 * نافذة تسجيل الدخول الخاصة بالمشرف (Admin).
 */
public class LoginGUI extends JDialog {
    private static final long serialVersionUID = 1L;

    // مكونات واجهة المستخدم
    private JTextField tfUsername;            // حقل إدخال اسم المستخدم
    private JPasswordField pfPassword;        // حقل إدخال كلمة المرور
    private JButton loginBtn;                 // زر تسجيل الدخول

    // خدمة المسؤول للتفاعل مع قاعدة البيانات
    private AdminService adminService;

    // لتحديد ما إذا تم التحقق من هوية المستخدم بنجاح
    private boolean authenticated = false;

    /**
     * المُنشئ: يُنشئ نافذة حوار (Dialog) لتسجيل الدخول
     * @param parent النافذة الأب (MainFrame مثلاً)
     * @param conn الاتصال بقاعدة البيانات
     */
    public LoginGUI(Frame parent, Connection conn) {
        // إنشاء نافذة حوار بعنوان "Login" وبوضعية Modal (تمنع التفاعل مع النوافذ الأخرى حتى تُغلق)
        super(parent, "Login", true);

        // تهيئة خدمة Admin باستخدام الاتصال
        try {
            adminService = new AdminService(conn);
        } catch (Exception ex) {
            showError("Connection error: " + ex.getMessage());
            return;
        }

        // إنشاء مكونات الإدخال
        tfUsername = new JTextField(15);
        pfPassword = new JPasswordField(15);
        loginBtn = new JButton("Login");

        // تنسيق واجهة الإدخال
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // هوامش بين المكونات
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // صف اسم المستخدم
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(tfUsername, gbc);

        // صف كلمة المرور
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(pfPassword, gbc);

        // زر الدخول
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginBtn, gbc);

        // إضافة اللوحة إلى النافذة
        add(panel);

        // عند الضغط على الزر يتم التحقق من بيانات الدخول
        loginBtn.addActionListener(this::authenticate);

        // جعل زر الدخول هو الزر الافتراضي (يتم تفعيله عند الضغط على Enter)
        getRootPane().setDefaultButton(loginBtn);

        // حجم النافذة وموقعها
        setSize(400, 250);
        setLocationRelativeTo(parent); // لتكون في منتصف الشاشة نسبةً إلى النافذة الأب
    }

    /**
     * يُستخدم من الخارج لمعرفة ما إذا كان المستخدم قد سجل الدخول بنجاح
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * دالة التحقق من بيانات الدخول عند الضغط على زر "Login"
     */
    private void authenticate(ActionEvent e) {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();

        // التحقق من عدم ترك الحقول فارغة
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and Password are required");
            return;
        }

        try {
            // تحقق من صحة البيانات عبر الخدمة
            Admin admin = adminService.login(username, password);
            if (admin != null) {
                // تم التحقق بنجاح
                authenticated = true;
                dispose(); // إغلاق نافذة تسجيل الدخول
            } else {
                showError("Invalid credentials");
            }
        } catch (Exception ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    /**
     * عرض رسالة خطأ في نافذة منبثقة
     */
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
