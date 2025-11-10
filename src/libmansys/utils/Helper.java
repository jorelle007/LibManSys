package libmansys.utils;

import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import libmansys.view.*;
import libmansys.dao.UserDAO;
import libmansys.model.User;

public class Helper {

    private Connection conn;

    public static void goBackToLogin(JFrame currentFrame, Connection conn) {
        new Login(conn).setVisible(true);
        currentFrame.dispose();
    }

    public static void goBackToHome(JFrame currentFrame, Connection conn) {
        goBackToHome(currentFrame, conn, null);
    }
    
    public static void goBackToHome(JFrame currentFrame, Connection conn, String username) {
        String fullName = null;
        
        if (username != null && !username.isEmpty() && conn != null) {
            try {
                UserDAO userDAO = new UserDAO(conn);
                User user = userDAO.searchUser(username);
                if (user != null) {
                    fullName = user.getFull_name();
                }
            } catch (SQLException ex) {
                System.err.println("Error fetching user name: " + ex.getMessage());
            }
        }
        
        new Home(conn, username, fullName).setVisible(true);
        currentFrame.dispose();
    }

    // Auto-adjust column widths based on content
    public static void autoResizeColumns(JTable table) {
        final int margin = 5; // extra space
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            int width = 0;

            TableColumn column = table.getColumnModel().getColumn(columnIndex);

            // header width
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, column.getHeaderValue(), false, false, 0, 0);
            width = headerComp.getPreferredSize().width;

            // cell width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, columnIndex);
                Component c = cellRenderer.getTableCellRendererComponent(
                        table, table.getValueAt(row, columnIndex), false, false, row, columnIndex);
                width = Math.max(width, c.getPreferredSize().width);
            }

            column.setPreferredWidth(width + margin);
        }
    }
    
    public static void addTableFilter(JTable table, JTextField textField, int... filterColumns) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }

            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }

            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = textField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    List<RowFilter<Object,Object>> filters = new ArrayList<>();
                    for (int col : filterColumns) {
                        filters.add(RowFilter.regexFilter("(?i)" + text, col));
                    }
                    sorter.setRowFilter(RowFilter.orFilter(filters));
                }
            }
        });
    }
}
