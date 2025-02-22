package PortalPage.Main;

import PortalPage.Swing.PatientPortalPanel;
import PortalPage.TempModels.TestPatient;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set Nimbus LookAndFeel if available
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
            // Create a test patient instance for testing purposes
            TestPatient testPatient = new TestPatient(1, "Juan Pérez", 45, "Calle Falsa 123", "600123456");

            // Pass the test patient to the portal panel
            JFrame frame = new JFrame("Portal del Paciente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            PatientPortalPanel portalPanel = new PatientPortalPanel(frame, testPatient);
            frame.add(portalPanel);
            frame.setVisible(true);
        });
    }
}
