package libmansys.dao;

import java.sql.*;
//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import libmansys.model.Book;
import libmansys.view.Student;

public class IssueDAO {

    private Connection conn;

    public IssueDAO(Connection conn) {
        this.conn = conn;
    }

    // Get all available books
    public List<Book> getAllAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM tbook WHERE quantity > 0";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getString("category"),
                        rs.getInt("year_published"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Object[]> searchStudents(String searchKeyword) throws SQLException {
        List<Object[]> students = new ArrayList<>();

        String sql = "SELECT student_id, first_name, last_name, "
                + "course, email_address "
                + "FROM tStudent "
                + "WHERE student_id = ? "
                + "OR first_name LIKE ? "
                + "OR last_name LIKE ? "
                + "OR CONCAT(first_name, ' ', last_name) LIKE ? "
                + "OR CONCAT(last_name, ' ', first_name) LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String search = "%" + searchKeyword.trim() + "%";

            stmt.setString(1, searchKeyword); //student_id
            for (int i = 2; i <= 5; i++) {
                stmt.setString(i, search);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Object[]{
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("course"),
                        rs.getString("email_address")
                    });
                }
            }
        }

        return students;
    }

    public List<Object[]> getUnreturnedBooksByStudent(String studentIDTxt) throws SQLException {
        List<Object[]> unreturnedBooks = new ArrayList<>();

        int studentID = Integer.parseInt(studentIDTxt.trim());

        String sql = "SELECT btr.btr_id, btr.student_id, btr.book_id, b.title, "
                + "btr.borrow_date, btr.due_date "
                + "FROM tbtr btr "
                + "JOIN tbook b ON btr.book_id = b.book_id "
                + "LEFT JOIN treturn r ON btr.btr_id = r.btr_id "
                + "WHERE btr.student_id = ? AND r.btr_id IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    unreturnedBooks.add(new Object[]{
                        //rs.getString("btr_id"),
                        rs.getString("book_id"),
                        rs.getString("title"),
                        rs.getDate("due_date")
                    });
                }
            }
        }

        return unreturnedBooks;
    }

    public boolean saveBTR(int studentId,
            int bookId,
            java.sql.Date borrowDate,
            java.sql.Date dueDate,
            String status,
            String username) {
        String insertBtrSQL = "INSERT INTO tbtr (student_id, book_id, borrow_date, due_date, status, username) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String updateBookSQL = "UPDATE tbook SET quantity = quantity - 1 WHERE book_id = ?";

        try {
            conn.setAutoCommit(false); // start transaction

            // Insert new borrow transaction
            try (PreparedStatement psInsert = conn.prepareStatement(insertBtrSQL)) {
                psInsert.setInt(1, studentId);
                psInsert.setInt(2, bookId);
                psInsert.setDate(3, borrowDate);
                psInsert.setDate(4, dueDate);
                psInsert.setString(5, status);
                psInsert.setString(6, username);
                psInsert.executeUpdate();
            }

            // Update book quantity (-1)
            try (PreparedStatement psUpdate = conn.prepareStatement(updateBookSQL)) {
                psUpdate.setInt(1, bookId);
                psUpdate.executeUpdate();
            }

            conn.commit(); // ✅ commit both operations
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback(); // ❌ rollback if any fails
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true); // restore default behavior
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasOverdueBooks(int studentID) throws SQLException {
        String query = "SELECT COUNT(*) AS overdueCount "
                + "FROM tbtr b "
                + "LEFT JOIN treturn r ON b.btr_id = r.btr_id "
                + "WHERE b.student_id = ? "
                + "AND r.return_date IS NULL "
                + "AND b.due_date < CURDATE()";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, studentID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("overdueCount") > 0;
            }
        }
        return false;
    }
}
