package presentation;

import persistence.DBConnection;

import javax.swing.*;
import java.sql.Connection;

/**
 * نقطة دخول التطبيق (Main class).
 * 
 * يقوم البرنامج بتهيئة واجهة المستخدم الرسومية وإنشاء اتصال بقاعدة البيانات، 
 * ثم عرض نافذة تسجيل الدخول. 
 * عند نجاح تسجيل الدخول يتم فتح النافذة الرئيسية للنظام التي تحتوي على التبويبات المختلفة.
 */
public class Main {

    /**
     * الدالة الرئيسية التي يبدأ منها تنفيذ التطبيق.
     * 
     * @param args مصفوفة وسيطات سطر الأوامر (غير مستخدمة حالياً)
     */
    public static void main(String[] args) {
        // تنفيذ الكود الخاص بالواجهة الرسومية ضمن مؤشر الأحداث في Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // تعيين مظهر النظام الأساسي (اختياري لتحسين شكل الواجهة حسب النظام المستخدم)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // إنشاء اتصال بقاعدة البيانات عبر فئة DBConnection
                Connection conn = DBConnection.getConnection();

                /*
                 * --- قسم تجريبي لعرض نافذة التقارير فقط ---
                 * 
                 * يمكنك إلغاء التعليق لتجربة نافذة التقارير بشكل مستقل بدون تسجيل دخول أو باقي النظام.
                 */
                /*
                JFrame reportsFrame = new JFrame("نظام إدارة التقارير");
                reportsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                reportsFrame.setSize(1000, 700);
                reportsFrame.setLocationRelativeTo(null);
                reportsFrame.setContentPane(new ReportGUI());
                reportsFrame.setVisible(true);
                return;
                */

                // إنشاء نافذة مؤقتة بدون أشرطة وأزرار (للاستخدام كنافذة أب للنوافذ الحوارية)
                JFrame dummyFrame = new JFrame();
                dummyFrame.setUndecorated(true);
                dummyFrame.setLocationRelativeTo(null);
                dummyFrame.setVisible(true);

                // إنشاء وعرض نافذة تسجيل الدخول (مودال modal ترتبط بالنافذة المؤقتة)
                LoginGUI login = new LoginGUI(dummyFrame, conn);
                login.setVisible(true);

                // إغلاق النافذة المؤقتة بعد إغلاق نافذة تسجيل الدخول
                dummyFrame.dispose();

                // التحقق من نجاح تسجيل الدخول
                if (!login.isAuthenticated()) {
                    System.exit(0);  // إنهاء التطبيق إذا فشل تسجيل الدخول
                }

                // إنشاء النافذة الرئيسية للتطبيق
                JFrame frame = new JFrame("Student Management System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 700);             // تحديد حجم النافذة
                frame.setLocationRelativeTo(null);    // تمركز النافذة في منتصف الشاشة
                // لا يتم تكبير النافذة تلقائيًا باستخدام setExtendedState حتى لا تؤثر على حجمها الافتراضي

                // إنشاء تبويبات النظام الرئيسية وإضافة كل تبويب للوظائف المختلفة
                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("Students", new StudentGUI(conn));
                tabbedPane.addTab("Teachers", new TeacherGUI(conn));
                tabbedPane.addTab("Courses", new CourseGUI(conn));
                tabbedPane.addTab("Enrollments", new EnrollmentGUI(conn));
                tabbedPane.addTab("Semester", new SemesterGUI(conn));
                tabbedPane.addTab("Reports", new ReportGUI());
                tabbedPane.addTab("Admin Register", new AdminRegisterGUI(conn));

                // إضافة التبويبات إلى النافذة الرئيسية
                frame.add(tabbedPane);

                // عرض النافذة
                frame.setVisible(true);

            } catch (Exception e) {
                // عرض رسالة خطأ في حالة فشل الاتصال بقاعدة البيانات أو أي خطأ أثناء التهيئة
                JOptionPane.showMessageDialog(null,
                        "Database connection failed: " + e.getMessage(),
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
