package infrastructure;

/**
 * واجهة CourseObserver تحدد العقد للمراقبين الذين يرغبون في تلقي إشعارات
 * عند تحديث قائمة المقررات أو المعلمين في النظام.
 * تستخدم عادة في نمط تصميم المراقب حيث تستمع مكونات واجهة المستخدم أو وحدات أخرى
 * للتغييرات في البيانات وتقوم بتحديث نفسها وفقًا لذلك.
 */
public interface CourseObserver {
    /**
     * يتم استدعاء هذه الطريقة عند تغيير قائمة المقررات.
     */
    void onCourseListChanged();

    /**
     * يتم استدعاء هذه الطريقة عند تغيير قائمة المعلمين.
     * مفيد للمكونات التي تحتاج إلى تحديث البيانات المتعلقة بالمعلمين.
     */
    void onTeacherListChanged();
}