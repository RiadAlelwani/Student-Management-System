// infrastructure/SemesterObserver.java
package infrastructure;

import domain.Semester;
import java.util.List;

/**
 * هذه الواجهة تحدد مستمعًا للتغييرات في قائمة الفصول الدراسية.
 * الفئات التي تنفذ هذه الواجهة يجب أن تحدد ما يحدث
 * عند تعديل قائمة الفصول الدراسية.
 */
public interface SemesterObserver {

    /**
     * يتم استدعاء هذه الطريقة عند تغيير قائمة الفصول الدراسية.
     * يجب على الفئات المنفذة تحديد المنطق للاستجابة
     * لمثل هذه التغييرات (مثل تحديث مكون واجهة المستخدم).
     */
    void onSemesterListChanged();

    /**
     * (اختياري) يتم استدعاء هذه الطريقة عند تغيير قائمة الفصول الدراسية،
     * مع تمرير القائمة المحدّثة. يمكن للفئات المنفذة اختيار استخدام هذه
     * الطريقة للحصول على تفاصيل إضافية عن التغيير.
     *
     * @param updatedList قائمة الفصول الدراسية بعد التغيير
     */
    default void onSemesterListChanged(List<Semester> updatedList) {}
}
