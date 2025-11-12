package libmansys.dao;

import java.sql.*;
import java.util.Date;
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

    public void loadBorrowed(JTable table) throws SQLException {
        loadBorrowed(table, null, null);
    }

    public void loadBorrowed(JTable table, Date fromDate, Date toDate) throws SQLException {
        String sql
                = "SELECT "
                + "b.btr_id, "
                + "bk.title AS book_title, "
                + "CONCAT(s.first_name, ' ', s.last_name) AS student_name, "
                + "b.book_id, "
                + "b.student_id, "
                + "b.borrow_date, "
                + "b.due_date, "
                + "b.status "
                + "FROM tbtr b "
                + "JOIN tstudent s ON b.student_id = s.student_id "
                + "JOIN tbook bk ON b.book_id = bk.book_id";

        if (fromDate != null || toDate != null) {
            sql += " WHERE 1=1";
            if (fromDate != null) {
                sql += " AND b.borrow_date >= ?";
            }
            if (toDate != null) {
                sql += " AND b.borrow_date <= ?";
            }
        }

        sql += " ORDER BY b.borrow_date DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (fromDate != null) {
                pstmt.setDate(paramIndex++, new java.sql.Date(fromDate.getTime()));
            }
            if (toDate != null) {
                pstmt.setDate(paramIndex++, new java.sql.Date(toDate.getTime()));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getString("book_title"),
                        rs.getInt("student_id"),
                        rs.getInt("book_id"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getString("status"),
                        rs.getString("student_name")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            System.err.println("SQL failed. Query:\n" + sql);
            System.err.println("SQLException message: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("ErrorCode: " + ex.getErrorCode());
            ex.printStackTrace();
            throw ex;
        }
    }
}
