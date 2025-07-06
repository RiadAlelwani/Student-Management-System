package infrastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseNotifier هو فئة مساعدة تدير قائمة المراقبين المهتمين بتحديثات قائمة المقررات.
 * تتبع نمط تصميم المراقب (Observer).
 */
public class CourseNotifier {
    // قائمة ثابتة لتخزين جميع المراقبين المسجلين
    private static final List<CourseObserver> observers = new ArrayList<>();

    /**
     * تسجيل مراقب جديد ليتم إعلامه عند تغيير قائمة المقررات.
     * @param observer مراقب المقررات المراد تسجيله
     */
    public static void register(CourseObserver observer) {
        observers.add(observer);
    }

    /**
     * إعلام جميع المراقبين المسجلين بتغيير قائمة المقررات.
     * يستدعي طريقة onCourseListChanged() لكل مراقب.
     */
    public static void notifyAllObservers() {
        for (CourseObserver observer : observers) {
            observer.onCourseListChanged();
        }
    }
}