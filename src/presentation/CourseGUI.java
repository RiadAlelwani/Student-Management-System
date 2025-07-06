package presentation;

import domain.Course;
import domain.Teacher;
import domain.Department;

import infrastructure.CourseNotifier;
import infrastructure.CourseObserver;
import infrastructure.TeacherNotifier;
import infrastructure.TeacherObserver;

import application.CourseService;
import application.TeacherService;
import application.DepartmentService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

public class CourseGUI extends JPanel implements CourseObserver, TeacherObserver {

    private static final long serialVersionUID = 1L;

    private JComboBox<Teacher> cbTeacher;
    private JComboBox<Department> cbDepartment;
    private JTextField tfName, tfDescription, tfCredits, tfSearch;

    private JTable table;
    private DefaultTableModel tableModel;

    private CourseService courseService;
    private TeacherService teacherService;
    private DepartmentService departmentService;

    private List<Teacher> cachedTeachers = new ArrayList<>();
    private List<Department> cachedDepartments = new ArrayList<>();

    public CourseGUI(Connection conn) {
        setLayout(new BorderLayout());

        TeacherNotifier.register(this);
        CourseNotifier.register(this);

        try {
            courseService = new CourseService(conn);
            teacherService = new TeacherService(conn);
            departmentService = new DepartmentService(conn);
        } catch (Exception e) {
            showError(e);
            return;
        }

        buildUI();
        loadTeachers();
        loadDepartments();
        loadCourses();
    }

    private void buildUI() {
        tfName = new JTextField(15);
        tfDescription = new JTextField(15);
        tfCredits = new JTextField(5);
        cbTeacher = new JComboBox<>();
        cbDepartment = new JComboBox<>();
        tfSearch = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Course Info"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfName, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Teacher:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(cbTeacher, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfDescription, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfCredits, gbc);

        gbc.gridx = 4;
        inputPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 5;
        inputPanel.add(cbDepartment, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Course Name:"));
        searchPanel.add(tfSearch);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Description", "Credits", "Teacher", "Department"}, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

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

        addBtn.addActionListener(e -> addCourse());
        updateBtn.addActionListener(e -> updateCourse());
        deleteBtn.addActionListener(e -> deleteCourse());
        clearBtn.addActionListener(e -> clearFields());

        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                tfName.setText((String) tableModel.getValueAt(row, 1));
                tfDescription.setText((String) tableModel.getValueAt(row, 2));
                tfCredits.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                selectComboBoxByName((String) tableModel.getValueAt(row, 4));
                selectComboBoxDepartmentByName((String) tableModel.getValueAt(row, 5));
            }
        });
    }

    private void loadCourses() {
        try {
            tableModel.setRowCount(0);
            List<Course> list = courseService.getAllCourses();
            for (Course c : list) {
                tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getDescription(),
                    c.getCredits(),
                    getTeacherNameById(c.getTeacherId()),
                    getDepartmentNameById(c.getDepartmentId())
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadTeachers() {
        try {
            cbTeacher.removeAllItems();
            cachedTeachers = teacherService.getAll();
            for (Teacher t : cachedTeachers) cbTeacher.addItem(t);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadDepartments() {
        try {
            cbDepartment.removeAllItems();
            cachedDepartments = departmentService.getAll();
            for (Department d : cachedDepartments) cbDepartment.addItem(d);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void addCourse() {
        try {
            if (!validateInput()) return;

            Teacher selectedTeacher = (Teacher) cbTeacher.getSelectedItem();
            Department selectedDepartment = (Department) cbDepartment.getSelectedItem();

            if (selectedTeacher == null) {
                showError("Please select a teacher");
                return;
            }
            if (selectedDepartment == null) {
                showError("Please select a department");
                return;
            }

            Course course = new Course(
                tfName.getText().trim(),
                tfDescription.getText().trim(),
                Integer.parseInt(tfCredits.getText().trim()),
                selectedTeacher.getId(),
                selectedDepartment.getId()
            );
            courseService.add(course);
            loadCourses();
            clearFields();
            CourseNotifier.notifyAllObservers();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void updateCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a course to update");
            return;
        }
        try {
            if (!validateInput()) return;

            int id = (int) tableModel.getValueAt(row, 0);
            Teacher selectedTeacher = (Teacher) cbTeacher.getSelectedItem();
            Department selectedDepartment = (Department) cbDepartment.getSelectedItem();

            if (selectedTeacher == null) {
                showError("Please select a teacher");
                return;
            }
            if (selectedDepartment == null) {
                showError("Please select a department");
                return;
            }

            Course course = new Course(
                id,
                tfName.getText().trim(),
                tfDescription.getText().trim(),
                Integer.parseInt(tfCredits.getText().trim()),
                selectedTeacher.getId(),
                selectedDepartment.getId()
            );
            courseService.update(course);
            loadCourses();
            clearFields();
            CourseNotifier.notifyAllObservers();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a course to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this course?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            courseService.delete(id);
            loadCourses();
            clearFields();
            CourseNotifier.notifyAllObservers();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void search() {
        try {
            String keyword = tfSearch.getText().toLowerCase();
            List<Course> list = courseService.getAllCourses();

            tableModel.setRowCount(0);
            for (Course c : list) {
                if (c.getName().toLowerCase().contains(keyword)) {
                    tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getName(),
                        c.getDescription(),
                        c.getCredits(),
                        getTeacherNameById(c.getTeacherId()),
                        getDepartmentNameById(c.getDepartmentId())
                    });
                }
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void clearFields() {
        tfName.setText("");
        tfDescription.setText("");
        tfCredits.setText("");
        tfSearch.setText("");
        if (cbTeacher.getItemCount() > 0) cbTeacher.setSelectedIndex(0);
        if (cbDepartment.getItemCount() > 0) cbDepartment.setSelectedIndex(0);
        table.clearSelection();
    }

    private String getTeacherNameById(int id) {
        return cachedTeachers.stream()
            .filter(t -> t.getId() == id)
            .map(Teacher::getName)
            .findFirst()
            .orElse("Unknown");
    }

    private String getDepartmentNameById(int id) {
        return cachedDepartments.stream()
            .filter(d -> d.getId() == id)
            .map(Department::getName)
            .findFirst()
            .orElse("Unknown");
    }

    private void selectComboBoxByName(String name) {
        if (name == null) return;
        for (int i = 0; i < cbTeacher.getItemCount(); i++) {
            Teacher t = cbTeacher.getItemAt(i);
            if (name.equals(t.getName())) {
                cbTeacher.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectComboBoxDepartmentByName(String name) {
        if (name == null) return;
        for (int i = 0; i < cbDepartment.getItemCount(); i++) {
            Department d = cbDepartment.getItemAt(i);
            if (name.equals(d.getName())) {
                cbDepartment.setSelectedIndex(i);
                return;
            }
        }
    }

    private boolean validateInput() {
        String name = tfName.getText().trim();
        String desc = tfDescription.getText().trim();
        String creditsStr = tfCredits.getText().trim();

        if (name.isEmpty()) {
            showError("Course name is required");
            return false;
        }
        if (desc.isEmpty()) {
            showError("Description is required");
            return false;
        }
        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
            if (credits < 1 || credits > 10) {
                showError("Credits must be between 1 and 10");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid credits number");
            return false;
        }
        return true;
    }

    private void showError(Exception e) {
        e.printStackTrace();
        showError(e.getMessage());
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onCourseListChanged() {
        loadCourses();
    }

    @Override
    public void onTeacherListChanged() {
        loadTeachers();
        loadCourses();
    }

    @Override
    public void onTeacherListChanged(List<Teacher> updatedList) {
        cachedTeachers = updatedList;
        loadTeachers();
        loadCourses();
    }
}
