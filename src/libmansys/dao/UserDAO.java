package libmansys.dao;

import java.sql.*;
import libmansys.model.UserModel;

/**
 * DAO for Book-related DB calls
 *
 * @author jorel
 */
public class UserDAO {

    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public UserModel login(String username, String password) throws SQLException {
        String sql = "{CALL sp_loginUser(?, ?)}"; //call SP
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            boolean hasResult = stmt.execute();
            if (hasResult) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        // ✅ user found
                        UserModel user = new UserModel();
                        user.setUser_id(rs.getInt("user_id"));
                        user.setFull_name(rs.getString("full_name"));
                        user.setRole(rs.getString("role"));
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
//        UserModel user = null;
//        if (rs.next()) {
//            user = new UserModel();
//            user.setUser_id(rs.getInt("user_id"));
//            user.setFull_name(rs.getString("full_name"));
//            user.setRole(rs.getString("role"));
//        }
//        return user;
    
    //SignUp
    public boolean signup(UserModel user) throws SQLException {
        String sql = "INSERT INTO tusers (full_name, username, password, security_question, security_answer) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFull_name());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getSecret_question());
            stmt.setString(5, user.getSecret_answer());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
//        }catch (Exception e) {
//            e.printStackTrace();
//            return false;
        }        
    }
}
