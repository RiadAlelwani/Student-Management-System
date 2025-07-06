// infrastructure/SemesterNotifier.java
package infrastructure;

import domain.Semester;
import java.util.ArrayList;
import java.util.List;

public class SemesterNotifier {
    private static final List<SemesterObserver> observers = new ArrayList<>();

    public static void register(SemesterObserver o) { observers.add(o); }
    public static void notifyAllObservers() {
        for (SemesterObserver o : observers) o.onSemesterListChanged();
    }

    public static void notifyAllObservers(List<Semester> updatedList) {
        for (SemesterObserver o : observers) o.onSemesterListChanged(updatedList);
    }
}
 