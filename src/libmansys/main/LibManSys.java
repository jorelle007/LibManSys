
package libmansys.main;

import libmansys.dao.*;
import libmansys.view.Login;
import java.sql.*;
import libmansys.view.Home;
import libmansys.view.SignUp;

//import libmansys.view.LoginFrame;
/**
 *
 * @author jorel
 */
public class LibManSys {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // Pass connection to DAOs
            UserDAO userDao = new UserDAO(conn);
            BookDAO bookDao = new BookDAO(conn);
            StudentDAO studentDao = new StudentDAO(conn);
            //BorrowDAO borrowDao = new BorrowDAO(conn);

            //new Login(conn).setVisible(true);     //enable login                   
            new Home(conn,"jorelle123", "JD Galam").setVisible(true); 
            
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
