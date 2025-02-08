package LandingPage.Swing;

import Authentication.Components.CustomizedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ImageCarousel extends JPanel {
    private List<ImageIcon> images;
    private JLabel imageLabel;
    private JPanel indicatorPanel;
    private JButton previousButton;
    private JButton nextButton;
    private int currentIndex;
    private Timer autoSlideTimer;



    public ImageCarousel(List<ImageIcon> images) {
        this.images = images;
        initComponents();
        startAutoSlide();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Each instance of an image will be contained here
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Panel containing the indicators
        indicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indicatorPanel.setOpaque(false);

        // TODO: Consider making this SVG or stylize it a bit
        previousButton = new JButton("<");
        previousButton.addActionListener(e -> showPreviousImage());

        nextButton = new JButton(">");
        nextButton.addActionListener(e -> showNextImage());

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
                    i == currentIndex ? Color.RED : Color.YELLOW // TODO: Adapt colours
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
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(previousButton, BorderLayout.WEST);
        buttonPanel.add(nextButton, BorderLayout.EAST);

        add(imageLabel, BorderLayout.CENTER);
        add(indicatorPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void startAutoSlide() {
        autoSlideTimer = new Timer(5000, new ActionListener() {
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
}
