
package libmansys.dao;

import java.sql.*;

/**
 *
 * @author jorel
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_libraryms";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        //return DriverManager.getConnection(URL, USER, PASSWORD);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // load MySQL driver
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.", e);
        }
    }
}
