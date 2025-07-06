package presentation;

import domain.*;
import application.*;
import infrastructure.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class EnrollmentGUI extends JPanel
        implements StudentObserver, CourseObserver, TeacherObserver, SemesterObserver {

    private static final long serialVersionUID = 1L;

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final SemesterService semesterService;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField tfGrade;
    private final JComboBox<Student> cbStudent;
    private final JComboBox<Course> cbCourse;
    private final JComboBox<Semester> cbSemester;
    private final JComboBox<Teacher> cbTeacher;

    public EnrollmentGUI(Connection conn) throws Exception {
        setLayout(new BorderLayout());

        // إنشاء الخدمات
        this.enrollmentService = new EnrollmentService(conn);
        this.studentService = new StudentService(conn);
        this.courseService = new CourseService(conn);
        this.teacherService = new TeacherService(conn);
        this.semesterService = new SemesterService(conn);

        // إنشاء عناصر الواجهة
        cbStudent = new JComboBox<>();
        cbCourse = new JComboBox<>();
        cbSemester = new JComboBox<>();
        cbTeacher = new JComboBox<>();
        tfGrade = new JTextField(15);

        // بناء لوحة الإدخال (البيانات)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enrollment Info"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbStudent, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(cbCourse, gbc);

        gbc.gridx = 4;
        inputPanel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(tfGrade, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbSemester, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Teacher:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(cbTeacher, gbc);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(inputPanel);
        add(topPanel, BorderLayout.NORTH);

        // إنشاء الجدول وتهيئته
        tableModel = new DefaultTableModel(new String[]{"Student", "Course", "Grade", "Semester", "Teacher"}, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null); // لجعل الجدول للعرض فقط
        add(new JScrollPane(table), BorderLayout.CENTER);

        // أزرار التحكم
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // تحميل البيانات الأوليّة في ComboBoxes والجدول
        loadStudents();
        loadCourses();
        loadSemesters();
        loadTeachers();
        loadData();

        // تسجيل EnrollmentGUI كمراقب لجميع الأنواع
        StudentNotifier.register(this);
        CourseNotifier.register(this);
        TeacherNotifier.register(this);
        SemesterNotifier.register(this);

        // تنفيذ الأفعال عند الضغط على الأزرار
        addBtn.addActionListener(e -> {
            try {
                Enrollment en = buildFromFields();
                enrollmentService.add(en);
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            try {
                Enrollment en = buildFromFields();
                enrollmentService.update(en);
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            try {
                Student student = (Student) cbStudent.getSelectedItem();
                Course course = (Course) cbCourse.getSelectedItem();
                enrollmentService.delete(student.getId(), course.getId());
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        // ملء الحقول عند اختيار صف في الجدول
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                selectComboBoxByName(cbStudent, tableModel.getValueAt(row, 0).toString());
                selectComboBoxByName(cbCourse, tableModel.getValueAt(row, 1).toString());
                tfGrade.setText(tableModel.getValueAt(row, 2).toString());
                selectComboBoxByText(cbSemester, tableModel.getValueAt(row, 3).toString());
                selectComboBoxByName(cbTeacher, tableModel.getValueAt(row, 4).toString());
            }
        });
    }

    // --- تنفيذ واجهات المراقبة ---

    @Override
    public void onStudentListChanged() {
        loadStudents();
    }

    @Override
    public void onCourseListChanged() {
        loadCourses();
    }

    @Override
    public void onTeacherListChanged() {
        loadTeachers();
    }

    @Override
    public void onTeacherListChanged(List<Teacher> updatedList) {
        loadTeachers();
    }

    @Override
    public void onSemesterListChanged() {
        loadSemesters();
    }

    @Override
    public void onSemesterListChanged(List<Semester> updatedList) {
        loadSemesters();
    }

    // --- تحميل البيانات ---

    private void loadStudents() {
        try {
            cbStudent.removeAllItems();
            cbStudent.addItem(new Student(0, "Select a student", "na@example.com", "M", 20, "N/A", 0));
            for (Student s : studentService.getAll()) cbStudent.addItem(s);
            cbStudent.setSelectedIndex(0);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadCourses() {
        try {
            cbCourse.removeAllItems();
            cbCourse.addItem(new Course(0, "Select a course", "N/A", 1, 0, 0));
            for (Course c : courseService.getAllCourses()) cbCourse.addItem(c);
            cbCourse.setSelectedIndex(0);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadSemesters() {
        try {
            cbSemester.removeAllItems();
            cbSemester.addItem(new Semester(0, "Select", 2025, false));
            for (Semester s : semesterService.getAll()) cbSemester.addItem(s);
            cbSemester.setSelectedIndex(0);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadTeachers() {
        try {
            cbTeacher.removeAllItems();
            cbTeacher.addItem(new Teacher(0, "Select a teacher", "na@example.com", "M", 30, 0, 0));
            for (Teacher t : teacherService.getAll()) cbTeacher.addItem(t);
            cbTeacher.setSelectedIndex(0);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadData() {
        try {
            fillTable(enrollmentService.getAll());
        } catch (Exception e) {
            showError(e);
        }
    }

    private void fillTable(List<Enrollment> list) {
        tableModel.setRowCount(0);
        for (Enrollment e : list) {
            tableModel.addRow(new Object[]{
                    e.getStudent().getName(),
                    e.getCourse().getName(),
                    e.getGrade(),
                    e.getSemester().toString(),
                    e.getTeacher() != null ? e.getTeacher().getName() : ""
            });
        }
    }

    // --- بناء الكائن من الحقول مع تحقق ---

    private Enrollment buildFromFields() {
        Student student = (Student) cbStudent.getSelectedItem();
        Course course = (Course) cbCourse.getSelectedItem();
        Semester semester = (Semester) cbSemester.getSelectedItem();
        Teacher teacher = (Teacher) cbTeacher.getSelectedItem();
        String gradeText = tfGrade.getText().trim();

        if (student == null || student.getId() == 0)
            throw new IllegalArgumentException("Please select a valid student.");
        if (course == null || course.getId() == 0)
            throw new IllegalArgumentException("Please select a valid course.");
        if (semester == null || semester.getId() == 0)
            throw new IllegalArgumentException("Please select a semester.");
        if (teacher == null || teacher.getId() == 0)
            throw new IllegalArgumentException("Please select a valid teacher.");
        if (gradeText.isEmpty())
            throw new IllegalArgumentException("Please enter a grade.");

        double grade;
        try {
            grade = Double.parseDouble(gradeText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grade must be a valid number.");
        }

        return new Enrollment(student, course, teacher, grade, semester);
    }

    // --- مسح الحقول ---

    private void clearFields() {
        cbStudent.setSelectedIndex(0);
        cbCourse.setSelectedIndex(0);
        tfGrade.setText("");
        cbSemester.setSelectedIndex(0);
        cbTeacher.setSelectedIndex(0);
        table.clearSelection();
    }

    // --- مساعدة تحديد عناصر JComboBox بناءً على الاسم أو النص ---

    private void selectComboBoxByText(JComboBox<?> cb, String text) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).toString().equals(text)) {
                cb.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectComboBoxByName(JComboBox<?> cb, String name) {
        for (int i = 0; i < cb.getItemCount(); i++) {
            try {
                Object item = cb.getItemAt(i);
                String itemName = (String) item.getClass().getMethod("getName").invoke(item);
                if (itemName.equals(name)) {
                    cb.setSelectedIndex(i);
                    return;
                }
            } catch (Exception ignored) {}
        }
        cb.setSelectedIndex(0);
    }

    // --- عرض رسالة الخطأ ---

    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
