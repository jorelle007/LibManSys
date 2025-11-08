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
        String sql
                = "SELECT b.btr_id, "
                + "CONCAT(s.first_name, ' ', s.last_name) AS student_name, "
                + "b.book_id, "
                + "b.student_id, "
                + "b.borrow_date, "
                + "b.due_date, "
                + "b.status, "
                + "b.user_id "
                + "FROM tBTR b "
                + "JOIN tStudent s ON b.student_id = s.student_id";

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
                    rs.getString("student_name")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            System.err.println("SQL failed. Query:\n" + sql);
            System.err.println("SQLException message: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("ErrorCode: " + ex.getErrorCode());
            ex.printStackTrace();
        }
    }
}
