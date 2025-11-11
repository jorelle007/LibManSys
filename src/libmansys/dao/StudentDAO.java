package libmansys.dao;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author liz
 */
public class StudentDAO {

    private Connection conn;

    public StudentDAO(Connection conn) {
        this.conn = conn; // optimize database para magamit lahat ng methods
    }

    public int getNextStudentId() { //optional.. kunin yung id na naka auto-increment
        int nextId = 1;
        String sql = "SHOW TABLE STATUS LIKE 'tstudent'"; //kukunin metadata at yung current auto-increment

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                nextId = rs.getInt("Auto_increment");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching next student ID: " + e.getMessage());
        }
        return nextId;
    }

    public void loadStudents(javax.swing.JTable table) { //load sa jtable yung list ng students
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM tStudent WHERE isDeleted = FALSE")) {

            while (rs.next()) {
                Object[] row = new Object[10]; // increase size for penalty + isPaid
                int studentId = rs.getInt("student_id");

                row[0] = studentId;
                row[1] = rs.getString("first_name");
                row[2] = rs.getString("last_name");
                row[3] = rs.getString("course");
                row[4] = rs.getString("contact_no");
                row[5] = rs.getString("email_address");
                //row[6] = rs.getDate("date_registered");

                // Compute total penalty dynamically
                String penaltyQuery = "SELECT COALESCE(SUM(CASE WHEN r.isPaid = 0 THEN r.penalty ELSE 0 END), 0) AS total_penalty "
                    + "FROM tReturn r "
                    + "JOIN tBTR b ON r.btr_id = b.btr_id "
                    + "WHERE b.student_id = ?";

                try (PreparedStatement ps = conn.prepareStatement(penaltyQuery)) {
                    ps.setInt(1, studentId);
                    try (ResultSet rs2 = ps.executeQuery()) {
                        double totalPenalty = 0;
                        if (rs2.next()) {
                            totalPenalty = rs2.getDouble("total_penalty");
                        }

                        row[6] = totalPenalty;      // Total Penalty column
                        row[7] = totalPenalty == 0; // isPaid column: true if nothing unpaid
                    }
                }

                model.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

//        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM tstudent WHERE isDeleted = FALSE")) {
//
//            while (rs.next()) {
//                Object[] row = new Object[8];
//                row[0] = rs.getInt("student_id");
//                row[1] = rs.getString("first_name");
//                row[2] = rs.getString("last_name");
//                row[3] = rs.getString("course");
//                row[4] = rs.getString("contact_no");
//                row[5] = rs.getString("email_address");
//                row[6] = rs.getDate("date_registered");
//
//                model.addRow(row);
//            }
    
    public boolean updateStudent(int studentId, String firstName, String lastName, String course,
                             String contactNo, String emailAddress, boolean isPaid) throws SQLException {

    String sqlStudent = "UPDATE tStudent SET first_name = ?, last_name = ?, course = ?, contact_no = ?, email_address = ? "
                      + "WHERE student_id = ?";

    String sqlPenalties = "UPDATE tReturn r "
                        + "JOIN tBTR b ON r.btr_id = b.btr_id "
                        + "SET r.isPaid = ? "
                        + "WHERE b.student_id = ? AND r.isPaid = 0"; // only update unpaid penalties

    boolean success = false;

    // Start transaction
    try {
        conn.setAutoCommit(false);

        try (PreparedStatement pstStudent = conn.prepareStatement(sqlStudent);
             PreparedStatement pstPenalties = conn.prepareStatement(sqlPenalties)) {

            // Update student info
            pstStudent.setString(1, firstName);
            pstStudent.setString(2, lastName);
            pstStudent.setString(3, course);
            pstStudent.setString(4, contactNo);
            pstStudent.setString(5, emailAddress);
            pstStudent.setInt(6, studentId);
            pstStudent.executeUpdate();

            // Update unpaid penalties
            pstPenalties.setBoolean(1, isPaid);
            pstPenalties.setInt(2, studentId);
            pstPenalties.executeUpdate();

            // Commit if both updates succeed
            conn.commit();
            success = true;

        } catch (SQLException ex) {
            conn.rollback(); // rollback if any update fails
            throw ex; // rethrow exception to handle in calling code
        } finally {
            conn.setAutoCommit(true); // restore default
        }

    } catch (SQLException e) {
        throw e;
    }

    return success;
}
//
//    
//    public boolean updateStudent(int studentId, String firstName, String lastName, String course,
//            String contactNo, String emailAddress, boolean isPaid) {
//
//        String sql = "UPDATE tStudent SET first_name = ?, last_name = ?, course = ?, "
//                + "contact_no = ?, email_address = ? WHERE student_id = ?";
//
//        try (PreparedStatement pst = conn.prepareStatement(sql)) {
//            pst.setString(1, firstName);
//            pst.setString(2, lastName);
//            pst.setString(3, course);
//            pst.setString(4, contactNo);
//            pst.setString(5, emailAddress);
//            pst.setInt(6, studentId);
//
//            pst.executeUpdate();
//
//            // ✅ If checkbox is checked, mark all unpaid penalties as paid
//            if (isPaid) {
//                String paySql = """
//                UPDATE tReturn r
//                JOIN tBTR b ON r.btr_id = b.btr_id
//                SET r.isPaid = TRUE
//                WHERE b.student_id = ? AND r.isPaid = FALSE
//            """;
//
//                try (PreparedStatement payStmt = conn.prepareStatement(paySql)) {
//                    payStmt.setInt(1, studentId);
//                    payStmt.executeUpdate();
//                }
//            }
//
//            return true;
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Error updating student: " + e.getMessage(),
//                    "Database Error",
//                    JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//    }

    public void addStudent(String firstName, String lastName, String course,
            String contactNo, String emailAddress, java.sql.Date dateRegistered) { //pag add ng new student

        String sql = "INSERT INTO tstudent (first_name, last_name, course, contact_no, email_address, date_registered) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, course);
            pst.setString(4, contactNo);
            pst.setString(5, emailAddress);
            pst.setDate(6, dateRegistered);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("New student added successfully!");
            } else {
                System.out.println("Failed to add new student");
            }

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    public boolean deleteStudent(int studentId) throws SQLException {
        String checkQuery = """
        SELECT COUNT(*) 
        FROM tBTR b
        LEFT JOIN tReturn r ON b.btr_id = r.btr_id
        WHERE b.student_id = ?
          AND (r.return_date IS NULL 
               OR (r.penalty > 0 AND r.isPaid = FALSE))
        """;


        try (PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Safe to soft-delete
                String deleteQuery = "UPDATE tstudent SET is_deleted = TRUE, deleted_at = NOW() WHERE student_id = ?";
                try (PreparedStatement psDel = conn.prepareStatement(deleteQuery)) {
                    psDel.setInt(1, studentId);
                    psDel.executeUpdate();
                }
                return true; // deleted successfully
            } else {
                return false; // cannot delete, book is borrowed
            }
        }
    }

    public boolean studentExists(String keyword) {
        String sql = "SELECT COUNT(*) FROM tstudent "
                + "WHERE student_id = ? "
                + "OR first_name LIKE ? "
                + "OR last_name LIKE ?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            int studentId = -1;
            try {
                studentId = Integer.parseInt(keyword); 
            } catch (NumberFormatException e) {
                studentId = -1; 
            }

            pst.setInt(1, studentId);          // student_id = ?
            pst.setString(2, "%" + keyword + "%"); // first_name LIKE ?
            pst.setString(3, "%" + keyword + "%"); // last_name LIKE ?

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if student exists
            }

        } catch (SQLException e) {
            System.out.println("Error checking student existence: " + e.getMessage());
        }

        return false; // default → not found
    }
//        String sql = "DELETE FROM tstudent WHERE student_id = ?"; // pagdelete sa student
//
//        try (PreparedStatement pst = conn.prepareStatement(sql)) {
//
//            pst.setInt(1, studentId);
//            int rows = pst.executeUpdate();
//
//            if (rows > 0) {
//                System.out.println("Student deleted successfully!");
//            } else {
//                System.out.println("No student found with ID: " + studentId);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error deleting student: " + e.getMessage());
//        }
}
