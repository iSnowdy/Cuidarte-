package LandingPage.Main;

import LandingPage.Swing.HeaderPanel;
import LandingPage.Swing.ImageCarousel;
import LandingPage.Swing.TextPanel;
import MainApplication.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * LandingPage acts as the main container that manages navigation between different sections.
 */
public class LandingPage extends JPanel {
    private final JFrame frame;
    private final NavigationController navigationController;
    private JPanel mainContent;

    public LandingPage(JFrame frame) {
        this.frame = frame;
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full Screen
        this.navigationController = new NavigationController(frame, this);
        initializeUI();
    }

    /**
     * Initializes the main UI structure, including the header and content panels.
     */
    private void initializeUI() {
        frame.setTitle("Patient Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);

        // ✅ Create main panel layout
        JPanel landingPanel = new JPanel(new BorderLayout());
        landingPanel.setBackground(Color.WHITE);

        // ✅ Initialize and add the HeaderPanel (always present)
        HeaderPanel headerPanel = new HeaderPanel(frame, navigationController);
        landingPanel.add(headerPanel, BorderLayout.NORTH);

        // ✅ Initialize the content panel
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // 🌟 Load default content: ImageCarousel and TextPanel
        loadDefaultLandingPageContent();

        // ✅ Add content panel to the frame
        landingPanel.add(mainContent, BorderLayout.CENTER);
        frame.setContentPane(landingPanel);
    }

    /**
     * Loads the default homepage content (ImageCarousel + TextPanel).
     */
    public void loadDefaultLandingPageContent() {
        mainContent.removeAll(); // Clear previous content

        // ✅ Create a content container (BoxLayout to arrange components vertically)
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));

        // ✅ Load images for the carousel
        List<ImageIcon> imageList = List.of(
                new ImageIcon(getClass().getResource("/CarouselImages/carousel1.jpeg")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel2.jpeg")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel3.png")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel4.jpeg")),
                new ImageIcon(getClass().getResource("/CarouselImages/carousel5.png"))
        );
        ImageCarousel carousel = new ImageCarousel(imageList);
        TextPanel textPanel = new TextPanel();

        // ✅ Add the carousel and text panel
        contentContainer.add(carousel);
        contentContainer.add(textPanel);

        // ✅ Set default content to mainContent
        mainContent.add(contentContainer, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    /**
     * Displays the frame.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Replaces the main content panel with a new one.
     * @param newContent The new panel to be displayed.
     */
    public void setMainContent(JPanel newContent) {
        if (newContent == mainContent || newContent == null) {
            return; // Prevent replacing with the same panel
        }
        mainContent.removeAll();
        mainContent.add(newContent, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    /**
     * Returns the main content panel.
     * @return The current main panel.
     */
    public JPanel getMainPanel() {
        return mainContent;
    }
}
