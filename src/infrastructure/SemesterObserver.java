// infrastructure/SemesterObserver.java
package infrastructure;
import domain.Semester;
import java.util.List;

public interface SemesterObserver {
    void onSemesterListChanged();
    default void onSemesterListChanged(List<Semester> updatedList) {}
}