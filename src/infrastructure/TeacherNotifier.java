package infrastructure;

import domain.Teacher;
import java.util.ArrayList;
import java.util.List;

/**
 * TeacherNotifier هي فئة مساعدة تتبع نمط المراقب.
 * تسمح لأجزاء مختلفة من التطبيق بتلقي إشعارات عند تحديث قائمة المعلمين.
 */
public class TeacherNotifier {
    private static final List<TeacherObserver> observers = new ArrayList<>();

    /**
     * تسجيل مراقب جديد ليتم إعلامه عند تغيير قائمة المعلمين.
     * @param observer المراقب الذي يريد تلقي التحديثات
     */
    public static void register(TeacherObserver observer) {
        observers.add(observer);
    }

    /**
     * إعلام جميع المراقبين المسجلين بقائمة المعلمين المحدثة.
     * @param updatedList أحدث قائمة من كائنات Teacher ليتم بثها للمراقبين
     */
    public static void notifyAllObservers(List<Teacher> updatedList) {
        for (TeacherObserver observer : observers) {
            observer.onTeacherListChanged(updatedList);
        }
    }
}