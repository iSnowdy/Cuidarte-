package UI.LandingPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class ImageCarousel extends JPanel {
    private List<ImageIcon> images;

    private JPanel imageContainer;
    private JPanel indicatorPanel;

    private JLabel imageLabel;

    private int currentIndex = 0;

    private Timer autoSlideTimer;
    private final int AUTO_SLIDE_DELAY = 5000;

    private final int IMAGE_WIDTH = 600;
    private final int IMAGE_HEIGHT = 300;

    public ImageCarousel(List<ImageIcon> images) {
        setBackground(Color.WHITE);
        // Resize all images to a standard size
        this.images = resizeImageList(images);
        initComponents();
        startAutoSlide();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        initImageContainer();
        initIndicatorPanel();
        layoutComponents();
        updateImage();
    }

    // Container for the image carousel
    private void initImageContainer() {
        imageContainer = new JPanel(new BorderLayout());
        imageContainer.setOpaque(false);
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextImage();
            }
        });
        imageContainer.add(imageLabel, BorderLayout.CENTER);
    }

    private void initIndicatorPanel() {
        indicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorPanel.setOpaque(false);
    }

    private void layoutComponents() {
        // Container for the indicator dots to center them properly
        JPanel indicatorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorContainer.setOpaque(false);
        indicatorContainer.add(indicatorPanel);

        // Add image container to center and navigation panel to the bottom
        add(imageContainer, BorderLayout.CENTER);
        add(indicatorContainer, BorderLayout.SOUTH);
    }

    // Updates the image displayed
    private void updateImage() {
        if (!images.isEmpty()) {
            imageLabel.setIcon(images.get(currentIndex));
            updateIndicators();
        }
    }

    // Refreshes the dot indicator for the new index
    private void updateIndicators() {
        indicatorPanel.removeAll();
        for (int i = 0; i < images.size(); i++) {
            JLabel dotLabel = new JLabel("●");
            dotLabel.setForeground(i == currentIndex ? MAIN_APP_COLOUR : Color.GRAY);
            indicatorPanel.add(dotLabel);
        }
        indicatorPanel.revalidate();
        indicatorPanel.repaint();
    }

    private void showNextImage() {
        currentIndex = (currentIndex + 1) % images.size();
        updateImage();
    }

    private void startAutoSlide() {
        autoSlideTimer = new Timer(AUTO_SLIDE_DELAY, e -> showNextImage());
        autoSlideTimer.start();
    }

    private List<ImageIcon> resizeImageList(List<ImageIcon> originalImages) {
        return originalImages.stream()
                .map(this::resizeImage)
                .toList();
    }

    private ImageIcon resizeImage(ImageIcon originalImage) {
        Image scaledImage = originalImage.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
