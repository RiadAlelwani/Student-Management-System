package presentation;

// استيراد الخدمات والكائنات المطلوبة من المشروع
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

// واجهة المستخدم لإدارة بيانات المعلمين + تطبيق واجهة المراقب لتحديث القائمة عند التغيير
public class TeacherGUI extends JPanel implements TeacherObserver {
    private static final long serialVersionUID = 1L;

    // الحقول المستخدمة في الواجهة
    private JTextField tfName, tfEmail, tfAge, tfSalary, tfSearch;
    private JComboBox<String> cbGender;
    private JComboBox<Department> cbDepartment;
    private JTable table;
    private DefaultTableModel tableModel;

    // الخدمات المستخدمة للتفاعل مع البيانات
    private TeacherService teacherService;
    private DepartmentDAO departmentDAO;

    // المُنشئ - يستقبل اتصال قاعدة البيانات، ويهيئ الواجهة والمكونات
    public TeacherGUI(Connection conn) {
        setLayout(new BorderLayout());

        try {
            teacherService = new TeacherService(conn);             // خدمة المعلمين
            departmentDAO = new DepartmentDAO(conn);              // الوصول إلى الأقسام
            teacherService.addObserver(this);                     // تسجيل كـ Observer لتحديث البيانات تلقائيًا
        } catch (Exception e) {
            showError(e);
            return;
        }

        // ----- قسم الإدخال العلوي -----
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Teacher Info"));  // عنوان في حافة الإدخال
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);   // هوامش
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // إنشاء الحقول
        tfName = new JTextField(15);
        tfEmail = new JTextField(15);
        cbGender = new JComboBox<>(new String[]{"Male", "Female"});
        tfAge = new JTextField(15);
        cbDepartment = new JComboBox<>();
        tfSalary = new JTextField(15);
        tfSearch = new JTextField(20);  // حقل البحث

        // تحميل الأقسام داخل القائمة المنسدلة
        try {
            List<Department> departments = departmentDAO.getAll();
            for (Department d : departments) {
                cbDepartment.addItem(d);
            }
        } catch (Exception e) {
            showError(e);
        }

        // ترتيب العناصر في النموذج باستخدام GridBagLayout
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

        // شريط البحث
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(tfSearch);

        topPanel.add(inputPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ----- الجدول لعرض المعلمين -----
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Gender", "Age", "Department", "Salary"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- أزرار التحكم -----
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

        // تحميل المعلمين إلى الجدول عند التشغيل
        loadData();

        // الزر: إضافة معلم
        addBtn.addActionListener(e -> {
            try {
                Department dept = (Department) cbDepartment.getSelectedItem();
                Teacher t = new Teacher(0, tfName.getText(), tfEmail.getText(), cbGender.getSelectedItem().toString(),
                        Integer.parseInt(tfAge.getText()), dept.getId(), Double.parseDouble(tfSalary.getText().replace(",", ".")));
                teacherService.add(t);
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // الزر: تحديث بيانات معلم
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                Department dept = (Department) cbDepartment.getSelectedItem();
                Teacher t = new Teacher(id, tfName.getText(), tfEmail.getText(), cbGender.getSelectedItem().toString(),
                        Integer.parseInt(tfAge.getText()), dept.getId(), Double.parseDouble(tfSalary.getText().replace(",", ".")));
                teacherService.update(t);
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // الزر: حذف معلم
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                teacherService.delete(id);
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        // الزر: مسح الحقول
        clearBtn.addActionListener(e -> clearFields());

        // البحث الحيّ أثناء الكتابة
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

        // عند النقر على صف في الجدول، تعبئة الحقول
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfEmail.setText(tableModel.getValueAt(row, 2).toString());
                cbGender.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                tfAge.setText(tableModel.getValueAt(row, 4).toString());
                tfSalary.setText(tableModel.getValueAt(row, 6).toString());

                // تحديد القسم حسب الاسم
                int deptId = getDepartmentIdByName(tableModel.getValueAt(row, 5).toString());
                for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                    if (cbDepartment.getItemAt(i).getId() == deptId) {
                        cbDepartment.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
    }

    // دالة للمطابقة بين اسم القسم ومعرفه
    private int getDepartmentIdByName(String name) {
        for (int i = 0; i < cbDepartment.getItemCount(); i++) {
            if (cbDepartment.getItemAt(i).getName().equals(name)) {
                return cbDepartment.getItemAt(i).getId();
            }
        }
        return 0;
    }

    // تحميل المعلمين من قاعدة البيانات
    private void loadData() {
        try {
            fillTable(teacherService.getAll());
        } catch (Exception e) {
            showError(e);
        }
    }

    // تعبئة الجدول بالمعلمين
    private void fillTable(List<Teacher> list) {
        tableModel.setRowCount(0);
        for (Teacher t : list) {
            String deptName = "";
            try {
                Department d = departmentDAO.getById(t.getDepartmentId());
                if (d != null) deptName = d.getName();
            } catch (Exception ignored) {}
            tableModel.addRow(new Object[]{
                    t.getId(), t.getName(), t.getEmail(), t.getGender(),
                    t.getAge(), deptName, String.format("%.2f", t.getSalary())
            });
        }
    }

    // مسح جميع الحقول
    private void clearFields() {
        tfName.setText("");
        tfEmail.setText("");
        tfAge.setText("");
        tfSalary.setText("");
        cbGender.setSelectedIndex(0);
        cbDepartment.setSelectedIndex(0);
        tfSearch.setText("");
        table.clearSelection();
    }

    // إظهار رسالة خطأ
    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    // عند تحديث بيانات المعلمين، يتم إعادة تحميل الجدول تلقائيًا
    @Override
    public void onTeacherListChanged() {
        SwingUtilities.invokeLater(this::loadData);
    }

    // عند وصول قائمة محدّثة من المعلمين، نستخدمها مباشرة
    @Override
    public void onTeacherListChanged(List<Teacher> updatedList) {
        SwingUtilities.invokeLater(() -> fillTable(updatedList));
    }
}
