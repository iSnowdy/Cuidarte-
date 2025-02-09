package PortalPage.Main;

import PortalPage.Swing.PatientPortalPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Portal del Paciente");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            PatientPortalPanel portalPanel = new PatientPortalPanel(frame);
            frame.add(portalPanel);

            frame.setVisible(true);
        });
    }
}
