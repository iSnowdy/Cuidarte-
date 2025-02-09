package AboutUs.Main;

import AboutUs.Swing.AboutUsPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quiénes Somos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            AboutUsPanel aboutPanel = new AboutUsPanel(frame);
            frame.add(aboutPanel);

            frame.setVisible(true);
        });
    }
}
