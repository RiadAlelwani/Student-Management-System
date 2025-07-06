package presentation;

import persistence.DepartmentDAO;
import domain.Department;
import domain.Teacher;
import infrastructure.TeacherObserver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import application.TeacherService;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

/**
 * واجهة المستخدم الرسومية لإدارة بيانات المعلمين.
 * تدعم إضافة، تعديل، حذف، والبحث عن المعلمين.
 * تقوم بتحديث العرض تلقائيًا عند تغير قائمة المعلمين عبر نمط Observer.
 */
public class TeacherGUI extends JPanel implements TeacherObserver {
    private static final long serialVersionUID = 1L;

    // حقول الإدخال للبيانات
    private JTextField tfName, tfEmail, tfAge, tfSalary, tfSearch;
    private JComboBox<String> cbGender;
    private JComboBox<Department> cbDepartment;
    private JTable table;
    private DefaultTableModel tableModel;

    // خدمات التعامل مع البيانات
    private TeacherService teacherService;
    private DepartmentDAO departmentDAO;

    /**
     * منشئ الواجهة يستقبل اتصال قاعدة البيانات.
     * يقوم بتهيئة كل المكونات وتحميل البيانات.
     * @param conn اتصال قاعدة البيانات
     */
    public TeacherGUI(Connection conn) {
        setLayout(new BorderLayout());

        try {
            teacherService = new TeacherService(conn);
            departmentDAO = new DepartmentDAO(conn);
            teacherService.addObserver(this);  // تسجيل كـ Observer لتحديث تلقائي
        } catch (Exception e) {
            showError(e);
            return;
        }

        initComponents();
        loadDepartments();
        loadData();
        setupListeners();
    }

    /**
     * تهيئة مكونات الواجهة الرسومية وترتيبها.
     */
    private void initComponents() {
        // حقول الإدخال
        tfName = new JTextField(15);
        tfEmail = new JTextField(15);
        cbGender = new JComboBox<>(new String[]{"Male", "Female"});
        tfAge = new JTextField(15);
        cbDepartment = new JComboBox<>();
        tfSalary = new JTextField(15);
        tfSearch = new JTextField(20);

        // لوحة الإدخال العلوية
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Teacher Info"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ترتيب الحقول في GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tfName, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        inputPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbGender, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfAge, gbc);

        gbc.gridx = 0; gbc.gridy++;
        inputPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbDepartment, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfSalary, gbc);

        // لوحة البحث
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(tfSearch);

        topPanel.add(inputPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // جدول عرض المعلمين
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Gender", "Age", "Department", "Salary"}, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);  // تعطيل التعديل المباشر
        // تلوين الصفوف بالتناوب لتحسين المظهر
        presentation.GUIUtils.configureTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // أزرار العمليات
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        add(buttonPanel, BorderLayout.PAGE_END);

        // ربط أزرار العمليات بالمعالجات
        addBtn.addActionListener(e -> handleAdd());
        updateBtn.addActionListener(e -> handleUpdate());
        deleteBtn.addActionListener(e -> handleDelete());
        clearBtn.addActionListener(e -> clearFields());
    }

    /**
     * تحميل الأقسام من قاعدة البيانات إلى القائمة المنسدلة.
     */
    private void loadDepartments() {
        try {
            List<Department> departments = departmentDAO.getAll();
            cbDepartment.removeAllItems();
            for (Department d : departments) {
                cbDepartment.addItem(d);
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * تحميل جميع المعلمين وعرضهم في الجدول.
     */
    private void loadData() {
        try {
            List<Teacher> list = teacherService.getAll();
            fillTable(list);
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * ملء الجدول بقائمة المعلمين المعطاة.
     * @param list قائمة المعلمين
     */
    private void fillTable(List<Teacher> list) {
        tableModel.setRowCount(0);
        for (Teacher t : list) {
            String deptName = "";
            try {
                Department d = departmentDAO.getById(t.getDepartmentId());
                if (d != null) deptName = d.getName();
            } catch (Exception ignored) {
            }
            tableModel.addRow(new Object[]{
                    t.getId(), t.getName(), t.getEmail(), t.getGender(),
                    t.getAge(), deptName, String.format("%.2f", t.getSalary())
            });
        }
    }

    /**
     * إعداد المستمعين لحقل البحث والنقر على الجدول.
     */
    private void setupListeners() {
        // البحث الحي عند الكتابة
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }

            private void search() {
                try {
                    List<Teacher> list = teacherService.searchByName(tfSearch.getText());
                    fillTable(list);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        // تعبئة الحقول عند اختيار صف من الجدول
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;

                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfEmail.setText(tableModel.getValueAt(row, 2).toString());
                cbGender.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                tfAge.setText(tableModel.getValueAt(row, 4).toString());
                tfSalary.setText(tableModel.getValueAt(row, 6).toString());

                // اختيار القسم المناسب
                String deptName = tableModel.getValueAt(row, 5).toString();
                for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                    if (cbDepartment.getItemAt(i).getName().equals(deptName)) {
                        cbDepartment.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
    }

    /**
     * إضافة معلم جديد بناءً على بيانات الحقول.
     * يعرض رسالة خطأ في حال وجود مشكلة.
     */
    private void handleAdd() {
        try {
            Department dept = (Department) cbDepartment.getSelectedItem();
            if (dept == null) {
                JOptionPane.showMessageDialog(this, "Please select a department.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = Integer.parseInt(tfAge.getText().trim());
            double salary = Double.parseDouble(tfSalary.getText().replace(",", ".").trim());

            Teacher t = new Teacher(0,
                    tfName.getText().trim(),
                    tfEmail.getText().trim(),
                    cbGender.getSelectedItem().toString(),
                    age,
                    dept.getId(),
                    salary);

            teacherService.add(t);
            clearFields();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Age and Salary must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /**
     * تحديث بيانات المعلم المحدد في الجدول.
     */
    private void handleUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            Department dept = (Department) cbDepartment.getSelectedItem();
            if (dept == null) {
                JOptionPane.showMessageDialog(this, "Please select a department.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = Integer.parseInt(tfAge.getText().trim());
            double salary = Double.parseDouble(tfSalary.getText().replace(",", ".").trim());

            Teacher t = new Teacher(id,
                    tfName.getText().trim(),
                    tfEmail.getText().trim(),
                    cbGender.getSelectedItem().toString(),
                    age,
                    dept.getId(),
                    salary);

            teacherService.update(t);
            clearFields();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Age and Salary must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /**
     * حذف المعلم المحدد في الجدول بعد تأكيد المستخدم.
     */
    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this teacher?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                teacherService.delete(id);
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    /**
     * مسح جميع حقول الإدخال وإلغاء تحديد الصف في الجدول.
     */
    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        tfAge.setText("");
        tfSalary.setText("");
        cbGender.setSelectedIndex(0);
        if (cbDepartment.getItemCount() > 0)
            cbDepartment.setSelectedIndex(0);
        tfSearch.setText("");
        table.clearSelection();
    }

    /**
     * إظهار رسالة خطأ باستخدام JOptionPane.
     * @param e الاستثناء الذي حدث
     */
    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * تنفيذ إعادة تحميل البيانات تلقائياً عند تغير قائمة المعلمين.
     * هذا المنهج جزء من واجهة TeacherObserver.
     */
    @Override
    public void onTeacherListChanged() {
        SwingUtilities.invokeLater(this::loadData);
    }

    /**
     * تحديث الجدول مباشرة بالقائمة المحدثة من المعلمين.
     * هذا المنهج جزء من واجهة TeacherObserver.
     * @param updatedList قائمة المعلمين المحدثة
     */
    @Override
    public void onTeacherListChanged(List<Teacher> updatedList) {
        SwingUtilities.invokeLater(() -> fillTable(updatedList));
    }
}
