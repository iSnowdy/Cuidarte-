package UI.LandingPage;

import Components.CustomScrollBar;
import MainApplication.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LandingPage extends JPanel {
    private final JFrame frame;
    private final NavigationController navigationController;
    private JPanel mainContent;
    private JScrollPane scrollPane;

    public LandingPage(JFrame frame) {
        this.frame = frame;
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full Screen
        this.navigationController = new NavigationController(frame, this);
        initializeUI();
    }

    // Initializes the main UI structure, including the header and content panels
    private void initializeUI() {
        frame.setTitle("Patient Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);

        // Main layout panel
        JPanel landingPanel = new JPanel(new BorderLayout());
        landingPanel.setBackground(Color.WHITE);

        // Header panel
        HeaderPanel headerPanel = new HeaderPanel(frame, navigationController);
        landingPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Add padding

        // Scroll pane to wrap the main content
        scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Apply custom scrollbar
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

        // Load default content (ImageCarousel + TextPanel)
        loadDefaultLandingPageContent();

        // Add scrollable content panel to the frame
        landingPanel.add(scrollPane, BorderLayout.CENTER);
        frame.setContentPane(landingPanel);
    }

    // Loads the default homepage content (ImageCarousel + TextPanel)
    public void loadDefaultLandingPageContent() {
        mainContent.removeAll(); // Clear previous content

        // Load images for the carousel
        List<ImageIcon> imageList = List.of(
                new ImageIcon(getClass().getResource("/CarouselImages/carousel1.jpeg")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel2.jpeg")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel3.png")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel4.jpeg")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel5.png"))
        );

        ImageCarousel carousel = new ImageCarousel(imageList);
        TextPanel textPanel = new TextPanel(navigationController);

        // Center align text panel
        JPanel textWrapper = new JPanel();
        textWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        textWrapper.add(textPanel);
        textWrapper.setOpaque(false);

        // Add components to mainContent
        mainContent.add(carousel);
        mainContent.add(Box.createVerticalStrut(30)); // Add padding
        mainContent.add(textWrapper);
        mainContent.add(Box.createVerticalStrut(30)); // Add padding

        mainContent.revalidate();
        mainContent.repaint();
    }

    // Displays the frame
    public void show() {
        frame.setVisible(true);
    }

    // Replaces the main content panel with a new one
    public void setMainContent(JPanel newContent) {
        if (newContent == mainContent || newContent == null) {
            return; // Prevent replacing with the same panel
        }
        mainContent.removeAll();
        mainContent.add(newContent);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // Returns the main content panel
    public JPanel getMainPanel() {
        return mainContent;
    }
}
