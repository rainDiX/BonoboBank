import gui.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        Logger rootLogr = Logger.getLogger("");
        /*
         * quasiment rien affiché dans le stderr
         * changer en INFO ou en FINE pour plus
         */
        rootLogr.setLevel(Level.SEVERE);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainWindow().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
