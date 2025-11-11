package libmansys.dao;

import java.sql.*;
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

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM tstudent WHERE is_deleted = FALSE")) {

            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("student_id");
                row[1] = rs.getString("first_name");
                row[2] = rs.getString("last_name");
                row[3] = rs.getString("course");
                row[4] = rs.getString("contact_no");
                row[5] = rs.getString("email_address");
                row[6] = rs.getDate("date_registered");

                model.addRow(row);
            }

        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    public void updateStudent(int studentId, String firstName, String lastName, String course,
            String contactNo, String emailAddress) { // pag update ng student

        String sql = "UPDATE tstudent SET first_name = ?, last_name = ?, course = ?, "
                + "contact_no = ?, email_address = ? WHERE student_id = ?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, course);
            pst.setString(4, contactNo);
            pst.setString(5, emailAddress);
            pst.setInt(6, studentId);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("No changes made for student ID: " + studentId);
            }

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

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
        String checkQuery = "SELECT COUNT(*) FROM tbtr b "
                + "LEFT JOIN tReturn r ON b.btr_id = r.btr_id "
                + "WHERE b.student_id = ? AND (r.return_date IS NULL OR r.return_date = '00-00-0000')";

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
