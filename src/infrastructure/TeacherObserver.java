package infrastructure;

import domain.Teacher;
import java.util.List;

/**
 * واجهة لمراقبة التغييرات في قائمة المعلمين.
 * الفئات التي تنفذ هذه الواجهة سيتم إعلامها عند تغيير قائمة المعلمين.
 */
public interface TeacherObserver {
    /**
     * يتم استدعاؤها عند تغيير قائمة المعلمين، مع توفير القائمة المحدثة.
     * @param updatedList القائمة الجديدة من كائنات Teacher بعد التغيير
     */
    void onTeacherListChanged(List<Teacher> updatedList);

    /**
     * يتم استدعاؤها عند تغيير قائمة المعلمين، دون توفير القائمة الجديدة.
     * مفيد للحالات التي يمكن فيها للمراقب جلب أحدث البيانات بنفسه.
     */
    void onTeacherListChanged();
}