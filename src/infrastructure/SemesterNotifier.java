// infrastructure/SemesterNotifier.java
package infrastructure;

import java.util.ArrayList;
import java.util.List;

import domain.Semester;
import presentation.EnrollmentGUI;
import presentation.SemesterGUI;

/**
 * SemesterNotifier هي فئة مساعدة تتبع نمط تصميم المراقب.
 * تدير قائمة من SemesterObservers وتخطرهم عند تغيير قائمة الفصول الدراسية.
 */
public class SemesterNotifier {
    private static final List<SemesterObserver> observers = new ArrayList<>();

    /**
     * تسجيل EnrollmentGUI كمراقب.
     * @param enrollmentGUI نسخة EnrollmentGUI المراد تسجيلها
     */
    public static void register(EnrollmentGUI enrollmentGUI) {
        observers.add((SemesterObserver) enrollmentGUI);
    }

    /**
     * تسجيل SemesterGUI كمراقب.
     * @param semesterGUI نسخة SemesterGUI المراد تسجيلها
     */
    public static void register(SemesterGUI semesterGUI) {
        observers.add((SemesterObserver) semesterGUI);
    }

    /**
     * إعلام جميع المراقبين المسجلين بتغيير قائمة الفصول الدراسية.
     * سيقوم كل مراقب بالرد عن طريق تحديث عرضه أو بياناته وفقًا لذلك.
     */
    public static void notifyAllObservers() {
        for (SemesterObserver observer : observers) {
            observer.onSemesterListChanged();
        }
    }

    /**
     * إعلام جميع المراقبين المسجلين بتغيير قائمة الفصول الدراسية،
     * مع تمرير القائمة المحدثة إليهم.
     *
     * @param updatedList قائمة الفصول الدراسية الجديدة
     */
    public static void notifyAllObservers(List<Semester> updatedList) {
        for (SemesterObserver observer : observers) {
            observer.onSemesterListChanged(updatedList);
        }
    }
}
