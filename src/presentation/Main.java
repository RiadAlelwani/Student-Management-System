package presentation; // الحزمة التي تحتوي على واجهات المستخدم (UI)

// استيراد الحزم المطلوبة
import java.sql.Connection;
import javax.swing.*;

import persistence.DBConnection; // كلاس خاص بإنشاء الاتصال بقاعدة البيانات

public class Main {
    public static void main(String[] args) {
        // تشغيل الواجهة الرسومية على خيط مخصص من Swing (لضمان سلامة الواجهة)
        SwingUtilities.invokeLater(() -> {
            try {
                // إنشاء الاتصال بقاعدة البيانات مرة واحدة لتمريره لجميع الواجهات
                Connection conn = DBConnection.getConnection();

                // --- عرض نافذة التقارير بشكل مستقل (للاختبار أو الاستخدام المباشر) ---
                /*
                JFrame reportsFrame = new JFrame("نظام إدارة التقارير");
                reportsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                reportsFrame.setSize(1000, 700);
                reportsFrame.setLocationRelativeTo(null);
                reportsFrame.setContentPane(new ReportGUI());
                reportsFrame.setVisible(true);
                return;  // إلغاء باقي الكود حتى لا تظهر النافذة الرئيسية الأخرى
                */

                // إنشاء نافذة مؤقتة (dummy) تستخدم فقط كـ "parent" لواجهة تسجيل الدخول
                JFrame dummyFrame = new JFrame(); // نافذة فارغة
                dummyFrame.setUndecorated(true);  // إخفاء الشريط العلوي للنافذة
                dummyFrame.setLocationRelativeTo(null); // تمركزها وسط الشاشة
                dummyFrame.setVisible(true); // يجب أن تكون مرئية لكي تعمل LoginGUI بشكل صحيح

                // عرض نافذة تسجيل الدخول (LoginGUI هي نافذة من نوع JDialog)
                LoginGUI login = new LoginGUI(dummyFrame, conn);
                login.setVisible(true); // تظهر نافذة تسجيل الدخول وتنتظر المستخدم

                // بعد إغلاق نافذة الدخول، نقوم بإغلاق النافذة المؤقتة
                dummyFrame.dispose();

                // التحقق مما إذا تم تسجيل الدخول بنجاح
                if (!login.isAuthenticated()) {
                    // إذا فشل تسجيل الدخول، يتم إنهاء التطبيق
                    System.exit(0);
                }

                // إنشاء النافذة الرئيسية للتطبيق بعد تسجيل الدخول
                JFrame frame = new JFrame("Student Management System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // إغلاق التطبيق عند الضغط على ×
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // تحديد حجم النافذة
                frame.setLocationRelativeTo(null); // تمركزها في منتصف الشاشة

                // إنشاء نافذة تبويبات (Tabbed Pane) لعرض أقسام النظام
                JTabbedPane tabbedPane = new JTabbedPane();

                // إضافة كل واجهة (Panel) كنظام فرعي ضمن النظام الكلي (كل تبويب يمثل وحدة)
                tabbedPane.addTab("Students", new StudentGUI(conn));         // إدارة الطلاب
                tabbedPane.addTab("Teachers", new TeacherGUI(conn));         // إدارة المعلمين
                tabbedPane.addTab("Courses", new CourseGUI(conn));           // إدارة الكورسات
                tabbedPane.addTab("Enrollments", new EnrollmentGUI(conn));   // تسجيل المواد
                tabbedPane.addTab("Semester", new SemesterGUI(conn));        // الفصول الدراسية
                tabbedPane.addTab("Reports", new ReportGUI());               // التقارير
                tabbedPane.addTab("Admin Register", new AdminRegisterGUI(conn)); // إدارة حسابات الإدمن

                // إضافة التبويبات إلى الإطار الرئيسي
                frame.add(tabbedPane);

                // إظهار النافذة الرئيسية
                frame.setVisible(true);

            } catch (Exception e) {
                // في حال فشل الاتصال أو حدوث خطأ عام، يتم عرض رسالة خطأ للمستخدم
                JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
            }
        });
    }
} 