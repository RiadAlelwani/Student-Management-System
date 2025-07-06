// Report.java - واجهة التقرير العامة
package reports;

import java.awt.Component;

import javax.swing.JPanel;

/**
 * واجهة عامة للتقارير في النظام.
 * 
 * تحتوي على دالة واحدة فقط هي show، 
 * والتي تُستخدم لعرض التقرير داخل مكوّن واجهة المستخدم الأب.
 * 
 * جميع تقارير النظام يجب أن تنفذ هذه الواجهة لتوحيد طريقة العرض.
 */
public interface Report {

    /**
     * عرض التقرير.
     * 
     * @param parent مكوّن واجهة المستخدم الأب (مثل JPanel أو JFrame)
     *               يتم عرض التقرير داخل هذا المكوّن أو استخدامه كمرجع للنوافذ الحوارية.
     */
    void show(Component parent);
    
    // طريقة جديدة لإرجاع JPanel يحتوي التقرير للعرض ضمن ReportGUI
    JPanel getReportPanel();
}
