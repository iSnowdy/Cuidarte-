package MainApplication;

import DatabaseConfig.ConnectionManager;
import LandingPage.Main.LandingPage;
import MainApplication.NavigationController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            ConnectionManager.getInstance().connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

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

        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Patient Portal");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 800);
            mainFrame.setLocationRelativeTo(null);

            LandingPage landingPage = new LandingPage(mainFrame);
            landingPage.show();

            mainFrame.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ConnectionManager.getInstance().disconnectFromDatabase();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
