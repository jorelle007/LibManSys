package libmansys.dao;

import java.sql.*;
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

    /**
     * Load all returned books into the JTable
     */
    public void loadReturned(JTable table) {
        String sql
                = "SELECT "
                + "r.return_id, "
                + "r.btr_id, "
                + "CONCAT(s.first_name, ' ', s.last_name) AS student_name, "
                + "r.return_date, "
                + "r.condition_on_return, "
                + "r.days_overdue, "
                + "r.penalty, "
                + "r.user_id "
                + "FROM tReturn r "
                + "JOIN tBTR b ON r.btr_id = b.btr_id "
                + "JOIN tStudent s ON b.student_id = s.student_id";

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("return_id"),
                    rs.getInt("btr_id"),
                    rs.getDate("return_date"),
                    rs.getString("condition_on_return"),
                    rs.getInt("days_overdue"),
                    rs.getBigDecimal("penalty"),
                    rs.getString("student_name"),
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
