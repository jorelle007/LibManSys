package libmansys.utils;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.sql.Connection;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import libmansys.view.*;

public class Helper {

    public static void goBackToLogin(JFrame currentFrame) {
        new Login().setVisible(true);
        currentFrame.dispose();
    }

    public static void goBackToHome(JFrame currentFrame) {
        new Home().setVisible(true);
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

    public static void goBackToHome(NewBook This, Connection conn, String currentUsername) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
