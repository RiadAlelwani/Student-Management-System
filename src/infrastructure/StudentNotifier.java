package infrastructure;

import java.util.ArrayList;
import java.util.List;
import presentation.EnrollmentGUI;
import presentation.StudentGUI;

/**
 * StudentNotifier هي فئة مساعدة تتبع نمط تصميم المراقب.
 * تدير قائمة من StudentObservers وتخطرهم عند تغيير قائمة الطلاب.
 */
public class StudentNotifier {
    private static final List<StudentObserver> observers = new ArrayList<>();

    /**
     * تسجيل EnrollmentGUI كمراقب.
     * @param enrollmentGUI نسخة EnrollmentGUI المراد تسجيلها
     */
    public static void register(EnrollmentGUI enrollmentGUI) {
        observers.add((StudentObserver) enrollmentGUI);
    }

    /**
     * تسجيل StudentGUI كمراقب.
     * @param studentGUI نسخة StudentGUI المراد تسجيلها
     */
    public static void register(StudentGUI studentGUI) {
        observers.add((StudentObserver) studentGUI);
    }

    /**
     * إعلام جميع المراقبين المسجلين بتغيير قائمة الطلاب.
     * سيقوم كل مراقب بالرد عن طريق تحديث عرضه أو بياناته وفقًا لذلك.
     */
    public static void notifyAllObservers() {
        for (StudentObserver observer : observers) {
            observer.onStudentListChanged();
        }
    }
}