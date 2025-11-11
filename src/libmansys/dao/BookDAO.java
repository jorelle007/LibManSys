package libmansys.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import libmansys.model.Book;

/**
 * DAO for Book-related DB calls
 *
 * @author jorel
 */
public class BookDAO {

    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    public int getNextBookId() { // pangkuha ng auto_increment id sa database 
        int nextId = 1;
        String sql = "SHOW TABLE STATUS LIKE 'tbook'";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                nextId = rs.getInt("Auto_increment");
            }

        } catch (SQLException e) {
            System.out.println("Cannot add book: " + e.getMessage());
        }

        return nextId;
    }

    public int addNewBook(String title, String author, String publisher, // pag add sa books
            String category, int year, int quantity, double price) {

        String sql = "INSERT INTO tbook (title, author, publisher, category, year_published, quantity, price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int generatedId = -1;

        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, title);
            pst.setString(2, author);
            pst.setString(3, publisher);
            pst.setString(4, category);
            pst.setInt(5, year);
            pst.setInt(6, quantity);
            pst.setDouble(7, price);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("New book added successfully!");

                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            } else {
                System.out.println("Failed to add new book");
            }

        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
        return generatedId;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM tbook WHERE isDeleted = FALSE";

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

    //Add Book
    public boolean addBook(Book book) throws SQLException {
        String sql = "INSERT INTO tbook (title, author, publisher, category, year_published, quantity, price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getYear());
            stmt.setInt(6, book.getQuantity());
            stmt.setDouble(7, book.getBookPrice());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // true if the insert was successful
        }
    }

    //Update Book
    public boolean updateBook(Book book) throws SQLException {
        // get the current book from database
        Book current = getBookById(book.getBookId());
        if (current != null) {
            boolean isSame
                    = current.getTitle().equals(book.getTitle())
                    && current.getAuthor().equals(book.getAuthor())
                    && current.getPublisher().equals(book.getPublisher())
                    && current.getCategory().equals(book.getCategory())
                    && current.getYear() == book.getYear()
                    && current.getQuantity() == book.getQuantity()
                    && current.getBookPrice() == book.getBookPrice();

            if (isSame) {
                return false; // no changes
            }
        }

        String sql = "UPDATE tbook SET title = ?, author = ?, publisher = ?, "
                + "category = ?, year_published = ?, quantity = ?, price = ? WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getYear());
            stmt.setInt(6, book.getQuantity());
            stmt.setDouble(7, book.getBookPrice());
            stmt.setInt(8, book.getBookId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    //Delete Book
    public boolean deleteBook(int bookID) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM tbtr b "
                + "LEFT JOIN tReturn r ON b.btr_id = r.btr_id "
                + "WHERE b.book_id = ? AND (r.return_date IS NULL OR r.return_date = '00-00-0000')";

        try (PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setInt(1, bookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Safe to soft-delete
                String deleteQuery = "UPDATE tbook SET is_deleted = TRUE, deleted_at = NOW() WHERE book_id = ?";
                try (PreparedStatement psDel = conn.prepareStatement(deleteQuery)) {
                    psDel.setInt(1, bookID);
                    psDel.executeUpdate();
                }
                return true; // deleted successfully
            } else {
                return false; // cannot delete, book is borrowed
            }
        }
    }

    //String sql = "DELETE FROM tbook WHERE book_id = ?";
//        String sql = "UPDATE tBook SET isDeleted = TRUE, "
//                + "deletedAt = CURRENT_TIMESTAMP "
//                + "WHERE book_id = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, bookID);
//            int rowsAffected = stmt.executeUpdate();
//            return rowsAffected > 0;
//        }
    //helper method to get a book record by Id
    public Book getBookById(int bookId) throws SQLException {
        String sql = "SELECT * FROM tbook WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Book b = new Book();
                b.setBookId(rs.getInt("book_Id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setPublisher(rs.getString("publisher"));
                b.setCategory(rs.getString("category"));
                b.setYear(rs.getInt("year_published"));
                b.setQuantity(rs.getInt("quantity"));
                b.setBookPrice(rs.getDouble("price"));
                return b;
            }
        }
        return null;
    }

}
