package libmansys.dao;

import java.sql.*;
import libmansys.model.User;

/**
 * DAO for User-related DB calls
 *
 * @author jorel
 */
public class UserDAO {

    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public User login(String username, String password) throws SQLException {
        String sql = "{CALL sp_loginUser(?, ?)}"; //call SP
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            boolean hasResult = stmt.execute();
            if (hasResult) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        // ✅ user found

                        User user = new User();

                        user.setFull_name(rs.getString("full_name"));
                        user.setUsername(username);
                        return user;
                    }
                }
            }
            // ❌ No user found
            return null;
        }
    }
    //) {

//        stmt.setString(1, username);
//        stmt.setString(2, password);
//
//        ResultSet rs = stmt.executeQuery();
//
//        User user = null;
//        if (rs.next()) {
//            user = new User();
//            user.setUser_id(rs.getInt("user_id"));
//            user.setFull_name(rs.getString("full_name"));
//            user.setRole(rs.getString("role"));
//        }
//        return user;
    //SignUp
    public boolean signUp(User user) throws SQLException {
        String sql = "INSERT INTO tusers (full_name, username, password, security_question, security_answer) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFull_name());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getSecurity_question());
            stmt.setString(5, user.getSecurity_answer());

            int rows = stmt.executeUpdate();
            return rows > 0;
//        }catch (Exception e) {
//            e.printStackTrace();
//            return false;
        }
    }

    // SEARCH USERNAME
    public User searchUser(String username) throws SQLException {
        String sql = "SELECT full_name, security_question FROM tusers WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setFull_name(rs.getString("full_name"));
                    user.setSecurity_question(rs.getString("security_question"));
                    return user;
                }
            }
        }
        return null; // user not found
    }

    public boolean verifySecurityAnswer(String username, String security_answer) throws SQLException {
        String sql = "SELECT * FROM tusers WHERE username = ? AND security_answer = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, security_answer);
        ResultSet rs = stmt.executeQuery();

        return rs.next();
    }

    // FORGOT PASSWORD
    public boolean resetPassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE tusers SET password = ? WHERE username  = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newPassword);
        stmt.setString(2, username);
        int rows = stmt.executeUpdate();
        return rows > 0;
    }
}
