package LandingPage.Swing;

import Authentication.Components.CustomizedButton;

import javax.swing.*;
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
        setUpLayout();
        startAutoSlide();
    }

    private void initComponents() {

    }



    private void setUpLayout() {

    }

    private void startAutoSlide() {

    }
}
