package LandingPage.Swing;

import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

// TODO: Remove img arrows and make it an ActionListener clicking on the image

public class ImageCarousel extends JPanel {
    private List<ImageIcon> images;

    private JPanel imageContainer;
    private JPanel indicatorPanel;
    private JPanel navigationPanel;

    private JLabel imageLabel;
    private JLabel previousImageLabel;
    private JLabel nextImageLabel;

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

    /**
     * Initializes all components of the carousel.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        initImageContainer();
        initIndicatorPanel();
        initNavigationLabels();
        layoutComponents();
        updateImage();
    }

    /**
     * Initializes the container and label for displaying the image.
     */
    private void initImageContainer() {
        imageContainer = new JPanel(new BorderLayout());
        imageContainer.setOpaque(false);
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageContainer.add(imageLabel, BorderLayout.CENTER);
    }

    /**
     * Initializes the panel that holds the indicator dots.
     */
    private void initIndicatorPanel() {
        indicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorPanel.setOpaque(false);
    }

    /**
     * Creates and initializes the navigation labels (previous and next).
     */
    private void initNavigationLabels() {
        // Create previous and next buttons using helper method
        previousImageLabel = createNavigationLabel("/LandingPage/previous.png", 10, 10, this::showPreviousImage);
        nextImageLabel = createNavigationLabel("/LandingPage/next.png", 10, 10, this::showNextImage);
    }

    /**
     * Lays out all components in the main panel.
     */
    private void layoutComponents() {
        // Create navigation panel with previous button, indicator panel, and next button
        navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.setOpaque(false);

        // Container for the indicator dots to center them properly
        JPanel indicatorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorContainer.setOpaque(false);
        indicatorContainer.add(indicatorPanel);

        navigationPanel.add(previousImageLabel, BorderLayout.WEST);
        navigationPanel.add(indicatorContainer, BorderLayout.CENTER);
        navigationPanel.add(nextImageLabel, BorderLayout.EAST);

        // Add image container to center and navigation panel to the bottom
        add(imageContainer, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper method to create a navigation label with an icon and click action.
     *
     * @param resourcePath The path to the icon resource.
     * @param width        Desired width of the icon.
     * @param height       Desired height of the icon.
     * @param onClickAction The action to execute on click.
     * @return Configured JLabel acting as a navigation button.
     */
    private JLabel createNavigationLabel(String resourcePath, int width, int height, Runnable onClickAction) {
        ImageIconRedrawer iconRedrawer = new ImageIconRedrawer();
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(resourcePath)));
        JLabel label = new JLabel(iconRedrawer.redrawImageIcon(width, height));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run();
            }
        });
        return label;
    }

    /**
     * Updates the displayed image and refreshes the indicators.
     */
    private void updateImage() {
        if (!images.isEmpty()) {
            imageLabel.setIcon(images.get(currentIndex));
            updateIndicators();
        }
    }

    /**
     * Refreshes the indicator dots based on the current image index.
     */
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

    /**
     * Advances to the next image.
     */
    private void showNextImage() {
        currentIndex = (currentIndex + 1) % images.size();
        updateImage();
    }

    /**
     * Goes back to the previous image.
     */
    private void showPreviousImage() {
        currentIndex = (currentIndex - 1 + images.size()) % images.size();
        updateImage();
    }

    /**
     * Starts the automatic slideshow of images.
     */
    private void startAutoSlide() {
        autoSlideTimer = new Timer(AUTO_SLIDE_DELAY, e -> showNextImage());
        autoSlideTimer.start();
    }

    /**
     * Stops the automatic slideshow.
     */
    public void stopAutoSlide() {
        if (autoSlideTimer != null) {
            autoSlideTimer.stop();
        }
    }

    /**
     * Resizes a list of ImageIcons to the standard dimensions.
     *
     * @param originalImages List of original images.
     * @return List of resized ImageIcons.
     */
    private List<ImageIcon> resizeImageList(List<ImageIcon> originalImages) {
        return originalImages.stream()
                .map(this::resizeImage)
                .toList();
    }

    /**
     * Resizes a single ImageIcon to the standard dimensions.
     *
     * @param originalImage The original image.
     * @return Resized ImageIcon.
     */
    private ImageIcon resizeImage(ImageIcon originalImage) {
        Image scaledImage = originalImage.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
