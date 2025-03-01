package MainApplication;

import Components.ShadowedLabel;

import javax.swing.*;
import java.awt.*;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

public class Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shadowed Label Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);
            frame.setLayout(new FlowLayout());

            ShadowedLabel label = new ShadowedLabel("Registrarse", MAIN_FONT, MAIN_APP_COLOUR);
            label.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));

            frame.add(label);
            frame.setVisible(true);
        });
    }
}
