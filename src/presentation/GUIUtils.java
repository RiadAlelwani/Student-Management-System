package presentation;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class GUIUtils {
    public static void configureTable(JTable table) {
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void setupSearchField(JTextField searchField, Runnable searchAction) {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchAction.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchAction.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchAction.run(); }
        });
    }

    public static void addTableSelectionListener(JTable table, Runnable action) {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
    }

    public static GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
}
