package LandingPage.Swing;

import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class ImageCarousel extends JPanel {
    private List<ImageIcon> images;

    private JPanel imageContainer;
    private JPanel indicatorPanel;
    private JPanel navigationPanel;

    private JLabel
            imageLabel, previousImageLabel, nextImageLabel;

    private int currentIndex;

    private Timer autoSlideTimer;
    private final int AUTO_SLIDE_DELAY = 5000;

    private final int IMAGE_WIDTH = 600;
    private final int IMAGE_HEIGHT = 300;

    public ImageCarousel(List<ImageIcon> images) {
        setBackground(Color.WHITE);
        this.images = resizeImageList(images); // Resizes all images to a standard size
        initComponents();
        startAutoSlide();
    }

    private void initComponents() {
        ImageIconRedrawer iconRedrawer = new ImageIconRedrawer();

        setLayout(new BorderLayout());

        imageContainer = new JPanel(new BorderLayout());
        imageContainer.setOpaque(false);

        // Each instance of an image will be contained here
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Panel containing the indicators
        indicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorPanel.setOpaque(false);

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/previous.png")));
        ImageIcon prevIcon = iconRedrawer.redrawImageIcon(10, 10);
        previousImageLabel = new JLabel(prevIcon);
        previousImageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        previousImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPreviousImage();
            }
        });


        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/next.png")));
        ImageIcon nextIcon = iconRedrawer.redrawImageIcon(10, 10);
        nextImageLabel = new JLabel(nextIcon);
        nextImageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNextImage();
            }
        });

        setUpLayout();
        updateImage();
    }

    private void updateImage() {
        if (!images.isEmpty()) {
            imageLabel.setIcon(images.get(currentIndex));
            updateIndicators();
        }
    }

    // This method will show us in which image of the List are we in
    private void updateIndicators() {
        if (indicatorPanel != null) indicatorPanel.removeAll();

        for (int i = 0; i < images.size(); i++) {
            JLabel dotLabel = new JLabel("●");
            dotLabel.setForeground(
                    i == currentIndex ? MAIN_APP_COLOUR : Color.GRAY //
            );
            indicatorPanel.add(dotLabel);
        }
        indicatorPanel.revalidate();
        indicatorPanel.repaint();
    }

    private void showNextImage() {
        currentIndex = (currentIndex + 1) % images.size(); // To not go out of index
        updateImage();
    }

    private void showPreviousImage() {
        currentIndex = (currentIndex - 1 + images.size()) % images.size();
        updateImage();
    }

    private void setUpLayout() {
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.setOpaque(false);


        JPanel indicatorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorContainer.setOpaque(false);
        indicatorContainer.add(indicatorPanel);


        navigationPanel.add(previousImageLabel, BorderLayout.WEST);
        navigationPanel.add(indicatorContainer, BorderLayout.CENTER);
        navigationPanel.add(nextImageLabel, BorderLayout.EAST);

        add(imageContainer, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private void startAutoSlide() {
        autoSlideTimer = new Timer(AUTO_SLIDE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextImage();
            }
        });
        autoSlideTimer.start();
    }

    public void stopAutoSlide() {
        if (autoSlideTimer != null) {
            autoSlideTimer.stop();
        }
    }

    private List<ImageIcon> resizeImageList(List<ImageIcon> originalImages) {
        return originalImages.stream()
                .map(this::resizeImage)
                .toList();
    }

    private ImageIcon resizeImage(ImageIcon originalImage) {
        Image newImage = originalImage.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
