package Centres.Main;

import Centres.Swing.CentresPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nuestros Centros");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            CentresPanel centersPanel = new CentresPanel(frame);
            frame.add(centersPanel);

            frame.setVisible(true);
        });
    }
}
