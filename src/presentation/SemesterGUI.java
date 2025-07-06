package presentation;

import domain.Semester;
import infrastructure.SemesterNotifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import application.SemesterService;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class SemesterGUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private final SemesterService semesterService;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField tfYear;
    private final JComboBox<String> cbTerm;
    private final JCheckBox cbIsOpen;

    public SemesterGUI(Connection conn) {
        setLayout(new BorderLayout());

        try {
            semesterService = new SemesterService(conn);
        } catch (Exception e) {
            showError(e);
            throw new RuntimeException(e);
        }

        cbTerm = new JComboBox<>(new String[]{"Fall", "Spring", "Summer"});
        tfYear = new JTextField(15);
        cbIsOpen = new JCheckBox("Is Open");

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Semester Info"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Term:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbTerm, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(tfYear, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cbIsOpen, gbc);

        topPanel.add(inputPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Term", "Year", "Is Open"}, 0);
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

        addBtn.addActionListener(e -> {
            try {
                Semester s = new Semester(
                        0,
                        cbTerm.getSelectedItem().toString(),
                        Integer.parseInt(tfYear.getText()),
                        cbIsOpen.isSelected()
                );
                semesterService.add(s);
                SemesterNotifier.notifyAllObservers();  // notify observers here
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
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                Semester s = new Semester(
                        id,
                        cbTerm.getSelectedItem().toString(),
                        Integer.parseInt(tfYear.getText()),
                        cbIsOpen.isSelected()
                );
                semesterService.update(s);
                SemesterNotifier.notifyAllObservers();  // notify observers here
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
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                semesterService.delete(id);
                SemesterNotifier.notifyAllObservers();  // notify observers here
                loadData();
                clearFields();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                cbTerm.setSelectedItem(tableModel.getValueAt(row, 1).toString());
                tfYear.setText(tableModel.getValueAt(row, 2).toString());
                cbIsOpen.setSelected((Boolean) tableModel.getValueAt(row, 3));
            }
        });
    }

    private void loadData() {
        try {
            List<Semester> list = semesterService.getAll();
            fillTable(list);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void fillTable(List<Semester> list) {
        tableModel.setRowCount(0);
        for (Semester s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getSeason(),
                    s.getYear(),
                    s.isOpen()
            });
        }
    }

    private void clearFields() {
        cbTerm.setSelectedIndex(0);
        tfYear.setText("");
        cbIsOpen.setSelected(false);
        table.clearSelection();
    }

    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
