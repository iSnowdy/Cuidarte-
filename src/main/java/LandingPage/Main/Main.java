package LandingPage.Main;

import LandingPage.Swing.HeaderPanel;
import LandingPage.Swing.ImageCarousel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Landing Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            // HeaderPanel
            HeaderPanel headerPanel = new HeaderPanel();
            frame.add(headerPanel, BorderLayout.NORTH);

            // Load images into the carousel
            List<ImageIcon> imageList = List.of(
                    new ImageIcon(Main.class.getResource("/CarouselImages/carousel1.jpeg")),
                    new ImageIcon(Main.class.getResource("/CarouselImages/carousel2.jpeg")),
                    new ImageIcon(Main.class.getResource("/CarouselImages/carousel3.png")),
                    new ImageIcon(Main.class.getResource("/CarouselImages/carousel4.jpeg")),
                    new ImageIcon(Main.class.getResource("/CarouselImages/carousel5.png"))
            );

            // Carousel
            ImageCarousel carousel = new ImageCarousel(imageList);
            frame.add(carousel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
