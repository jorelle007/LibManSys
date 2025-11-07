package libmansys.dao;

import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * DAO for borrowed books summary
 */
public class SummaryBorrowDAO {

    private Connection conn;

    public SummaryBorrowDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Load all borrowed books into the JTable
     */
    public void loadBorrowed(JTable table) throws SQLException {
        String sql = "SELECT btr_id, student_id, book_id, borrow_date, due_date, status, user_id FROM tbtr";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // clear existing rows

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("btr_id"),
                    rs.getInt("student_id"),
                    rs.getInt("book_id"),
                    rs.getDate("borrow_date"),
                    rs.getDate("due_date"),
                    rs.getString("status"),
                    rs.getInt("user_id")
                };
                model.addRow(row);
            }
        }
    }
}
