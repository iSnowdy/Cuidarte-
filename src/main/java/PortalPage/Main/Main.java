package PortalPage.Main;

import DatabaseConfig.ConnectionManager;
import LandingPage.Main.LandingPage;
import javax.swing.*;

/**
 * Entry point for the application.
 */
public class Main {
    public static void main(String[] args) {
        // Establish the database connection at startup
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

        // Initialize and display the LandingPage UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame();
            LandingPage landingPage = new LandingPage(mainFrame);
            landingPage.show();
        });

        // Add shutdown hook to close the database connection when the application exits
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
