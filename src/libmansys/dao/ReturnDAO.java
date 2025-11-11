package libmansys.dao;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author liz
 */
public class ReturnDAO {

    private Connection conn;

    public ReturnDAO(Connection conn) {
        this.conn = conn; // optimize database para magamit lahat ng methods
    }

    public ArrayList<ArrayList<String>> searchBorrow(String studentKeyword) {
        ArrayList<ArrayList<String>> borrows = new ArrayList<>();

        String sql = "SELECT t.btr_id, t.book_id, b.title AS book_title, t.borrow_date, t.due_date, t.status, "
                + "s.student_id, s.first_name, s.last_name, s.course, s.email_address "
                + "FROM tbtr t "
                + "JOIN tstudent s ON t.student_id = s.student_id "
                + "JOIN tbook b ON t.book_id = b.book_id "
                + "WHERE s.student_id = ? AND t.status = 'Borrowed'";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            int studentId = -1;
            try {
                studentId = Integer.parseInt(studentKeyword);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return borrows; // invalid student ID
            }

            pst.setInt(1, studentId);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) { // loop through all borrowed books
                ArrayList<String> borrow = new ArrayList<>();
                borrow.add(rs.getString("btr_id"));
                borrow.add(rs.getString("book_id"));
                borrow.add(rs.getString("book_title"));
                borrow.add(rs.getString("borrow_date"));
                borrow.add(rs.getString("due_date"));
                borrow.add(rs.getString("status"));
                borrow.add(rs.getString("student_id"));
                borrow.add(rs.getString("first_name") + " " + rs.getString("last_name")); // full name
                borrow.add(rs.getString("course"));
                borrow.add(rs.getString("email_address"));

                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrows;
    }

    public ResultSet getBorrowedBook(String bookId) throws SQLException {
        String sql
                = "SELECT t.btr_id, t.book_id, b.title AS book_title, t.borrow_date, "
                + "t.due_date, t.status, s.student_id, "
                + "CONCAT(s.first_name, ' ', s.last_name) AS full_name, "
                + "s.course, s.email_address "
                + "FROM tbtr t "
                + "JOIN tstudent s ON t.student_id = s.student_id "
                + "JOIN tbook b ON t.book_id = b.book_id "
                + "WHERE t.book_id = ? AND t.status = 'Borrowed'";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, bookId);
        return pst.executeQuery();
    }

    public Date getDueDate(int btrId) {
        Date dueDate = null;
        String sql = "SELECT due_date FROM tbtr WHERE btr_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, btrId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dueDate = rs.getDate("due_date");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dueDate;
    }

    public java.sql.Date getBorrowDate(int btrId) {
        String sql = "SELECT borrow_date FROM tbtr WHERE btr_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, btrId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDate("borrow_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // if not found
    }

    public double getBookPrice(String bookId) throws SQLException {
        String sql = "SELECT price FROM tbook WHERE book_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, bookId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean returnBook(int btrId, Date returnDate, String condition, int overdue, double penalty, String username) {
        //Insert return record into treturn
        String insertReturnSql
                = "INSERT INTO treturn (btr_id, return_date, condition_on_return, days_overdue, penalty, username) VALUES (?, ?, ?, ?, ?, ?)";

        //Update tbtr status
        String updateBtrSql
                = "UPDATE tbtr SET status = ? WHERE btr_id = ?";

        try {
            conn.setAutoCommit(false); // begin transaction

            // Insert into treturn
            try (PreparedStatement pst = conn.prepareStatement(insertReturnSql)) {
                pst.setInt(1, btrId);
                pst.setDate(2, returnDate);
                pst.setString(3, condition);
                pst.setInt(4, overdue);
                pst.setDouble(5, penalty);
                pst.setString(6, username);
                pst.executeUpdate();
            }

            // Determine tbtr status based on condition
            String status = "Returned";

            if ("Minor Damage".equals(condition)) {
                status = "Returned - Minor Damage";
            } else if ("Major Damage".equals(condition)) {
                status = "Returned - Major Damage";
            } else if ("Lost".equals(condition)) {
                status = "Returned - Lost";
            }

            // Update tbtr
            try (PreparedStatement pst2 = conn.prepareStatement(updateBtrSql)) {
                pst2.setString(1, status);
                pst2.setInt(2, btrId);
                pst2.executeUpdate();
            }

            conn.commit(); // success
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
            }
        }
    }

    public boolean increaseQty(String bookId) {
        String sql = "UPDATE tbook SET quantity = quantity + 1 WHERE book_id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, bookId);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
