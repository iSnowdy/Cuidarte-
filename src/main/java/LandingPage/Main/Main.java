package LandingPage.Main;

import LandingPage.Swing.HeaderPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("LandingPage.Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 150);
        frame.add(new HeaderPanel(), BorderLayout.NORTH);
        frame.setVisible(true);
    }
}
