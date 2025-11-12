package libmansys.dao;

import java.sql.*;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * DAO for returned books summary
 */
public class SummaryReturnDAO {

    private Connection conn;

    public SummaryReturnDAO(Connection conn) {
        this.conn = conn;
    }

    public void loadReturned(JTable table) {
        loadReturned(table, null, null);
    }

    public void loadReturned(JTable table, Date fromDate, Date toDate) {
        String sql
                = "SELECT "
                + "r.return_id, "
                + "bk.title AS book_title, "
                + "r.btr_id, "
                + "CONCAT(s.first_name, ' ', s.last_name) AS student_name, "
                + "r.return_date, "
                + "r.condition_on_return, "
                + "r.days_overdue, "
                + "r.penalty "
                + "FROM treturn r "
                + "JOIN tbtr b ON r.btr_id = b.btr_id "
                + "JOIN tstudent s ON b.student_id = s.student_id "
                + "JOIN tbook bk ON b.book_id = bk.book_id";


        if (fromDate != null || toDate != null) {
            sql += " WHERE 1=1";
            if (fromDate != null) {
                sql += " AND r.return_date >= ?";
            }
            if (toDate != null) {
                sql += " AND r.return_date <= ?";
            }
        }

        sql += " ORDER BY r.return_date DESC";

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (fromDate != null) {
                pstmt.setDate(paramIndex++, new java.sql.Date(fromDate.getTime()));
            }
            if (toDate != null) {
                pstmt.setDate(paramIndex++, new java.sql.Date(toDate.getTime()));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("return_id"),
                        rs.getString("book_title"),
                        rs.getDate("return_date"),
                        rs.getString("condition_on_return"),
                        rs.getInt("days_overdue"),
                        rs.getBigDecimal("penalty"),
                        rs.getString("student_name"),
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
        }
    }

}
