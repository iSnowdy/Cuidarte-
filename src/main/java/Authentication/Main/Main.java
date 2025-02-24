package Authentication.Main;

import DatabaseConfig.ConnectionManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            ConnectionManager.getInstance().connectToDatabase();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Set Nimbus Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Authenticator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(null);

            //Authenticator authenticator = new Authenticator();
            //frame.add(authenticator);

            frame.setVisible(true);
        });
    }
}
