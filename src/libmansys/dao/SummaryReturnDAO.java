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
    public void loadReturned(JTable table) throws SQLException {
        String sql = "SELECT r.return_id, r.btr_id, r.return_date, r.condition_on_return, r.days_overdue, r.penalty, r.user_id "
                   + "FROM tReturn r";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // clear existing rows

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("return_id"),
                    rs.getInt("btr_id"),
                    rs.getDate("return_date"),
                    rs.getString("condition_on_return"),
                    rs.getInt("days_overdue"),
                    rs.getBigDecimal("penalty"),
                    rs.getInt("user_id")
                };
                model.addRow(row);
            }
        }
    }
}