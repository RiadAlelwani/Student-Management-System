package presentation;

import domain.Enrollment;
import domain.Student;
import infrastructure.GradeCalculator;
import infrastructure.StudentNotifier;
import infrastructure.StudentObserver;
import infrastructure.WeightedGradeCalculator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import application.CourseService;
import application.EnrollmentService;
import application.StudentService;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class StudentGUI extends JPanel implements StudentObserver {
    private static final long serialVersionUID = 1L;

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final GradeCalculator gradeCalculator;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField tfName, tfEmail, tfAge, tfMajor, tfSearch;
    private final JComboBox<String> cbGender;

    public StudentGUI(Connection conn) {
        setLayout(new BorderLayout());
        StudentNotifier.register(this);

        try {
            studentService = new StudentService(conn);
            enrollmentService = new EnrollmentService(conn);
            CourseService courseService = new CourseService(conn);
            gradeCalculator = new WeightedGradeCalculator(courseService);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfName = new JTextField(15);
        tfEmail = new JTextField(15);
        cbGender = new JComboBox<>(new String[]{"Male", "Female"});
        tfAge = new JTextField(15);
        tfMajor = new JTextField(15);
        tfSearch = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; inputPanel.add(tfName, gbc);
        gbc.gridx = 2; inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; inputPanel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        inputPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; inputPanel.add(cbGender, gbc);
        gbc.gridx = 2; inputPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 3; inputPanel.add(tfAge, gbc);

        gbc.gridx = 0; gbc.gridy++;
        inputPanel.add(new JLabel("Major:"), gbc);
        gbc.gridx = 1; inputPanel.add(tfMajor, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(tfSearch);

        topPanel.add(inputPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Gender", "Age", "Major", "GPA"}, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
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

        loadData();

        addBtn.addActionListener(e -> handleAdd());
        updateBtn.addActionListener(e -> handleUpdate());
        deleteBtn.addActionListener(e -> handleDelete());
        clearBtn.addActionListener(e -> clearFields());

        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }

            private void search() {
                try {
                    fillTable(studentService.searchByName(tfSearch.getText()));
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfEmail.setText(tableModel.getValueAt(row, 2).toString());
                cbGender.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                tfAge.setText(tableModel.getValueAt(row, 4).toString());
                tfMajor.setText(tableModel.getValueAt(row, 5).toString());
            }
        });
    }

    private void handleAdd() {
        try {
            Student s = new Student(0, tfName.getText(), tfEmail.getText(), cbGender.getSelectedItem().toString(),
                    Integer.parseInt(tfAge.getText()), tfMajor.getText(), 0.0);
            studentService.add(s);
            StudentNotifier.notifyAllObservers();  // notify observers here
            loadData();
            clearFields();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void handleUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        try {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            Student s = new Student(id, tfName.getText(), tfEmail.getText(), cbGender.getSelectedItem().toString(),
                    Integer.parseInt(tfAge.getText()), tfMajor.getText(), 0.0);
            studentService.update(s);
            StudentNotifier.notifyAllObservers();  // notify observers here
            loadData();
            clearFields();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                studentService.delete(id);
                StudentNotifier.notifyAllObservers();  // notify observers here
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    private void loadData() {
        try {
            fillTable(studentService.getAll());
        } catch (Exception e) {
            showError(e);
        }
    }

    private void fillTable(List<Student> students) {
        tableModel.setRowCount(0);
        try {
            List<Enrollment> enrollments = enrollmentService.getAll();
            for (Student s : students) {
                List<Enrollment> studentEnrollments = enrollments.stream()
                        .filter(e -> e.getStudentId() == s.getId())
                        .collect(Collectors.toList());
                s.setEnrollments(studentEnrollments);
                double gpa = gradeCalculator.calculateGPA(s);
                tableModel.addRow(new Object[]{
                        s.getId(), s.getName(), s.getEmail(), s.getGender(), s.getAge(), s.getMajor(), String.format("%.2f", gpa)
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        cbGender.setSelectedIndex(0);
        tfAge.setText("");
        tfMajor.setText("");
        tfSearch.setText("");
        table.clearSelection();
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onStudentListChanged() {
        loadData();
    }
}
