
package libmansys.utils;

import javax.swing.JFrame;
import libmansys.view.*;

public class Helper {
    
    public static void goBackToLogin(JFrame currentFrame) {
        new Login().setVisible(true);
        currentFrame.dispose();
    }
    
    public static void goBackToHome(JFrame currentFrame) {
        new Home().setVisible(true);
        currentFrame.dispose();
    }
    
    
}
