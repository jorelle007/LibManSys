
package libmansys.utils;

import javax.swing.JFrame;
import libmansys.view.Login;

public class Helper {
    public static void goBackToLogin(JFrame currentFrame) {
        new Login().setVisible(true);
        currentFrame.dispose();
    }
}
