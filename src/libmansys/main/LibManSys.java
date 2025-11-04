/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package libmansys.main;

import libmansys.dao.*;
import libmansys.view.Login;
import java.sql.*;

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
            //BookDAO bookDao = new BookDAO(conn);
            //BorrowDAO borrowDao = new BorrowDAO(conn);

            new Login(conn).setVisible(true);            
            //login.setLocationRelativeTo(null); //center the frame
            //login.setVisible(true);            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
