package MainApplication;

import AboutUs.Swing.AboutUsPanel;
import Centres.Swing.CentresPanel;
import LandingPage.Main.LandingPage;
import PortalPage.Swing.PatientPortalPanel;
import PortalPage.TempModels.TestPatient;

import javax.swing.*;

/**
 * Handles navigation between different sections of the application.
 */
public class NavigationController {
    private final JFrame mainFrame;
    private final LandingPage landingPage;
    private final TestPatient testPatient;

    /**
     * Constructor for NavigationController.
     *
     * @param mainFrame   The main application frame.
     * @param landingPage The landing page instance.
     */
    public NavigationController(JFrame mainFrame, LandingPage landingPage) {
        this.mainFrame = mainFrame;
        this.landingPage = landingPage;
        this.testPatient = new TestPatient(1, "Juan Pérez", 45, "Calle Falsa 123", "600123456");
    }

    /**
     * Switches the main content panel based on the selected option.
     *
     * @param panelName The name of the panel to switch to.
     */
    public void switchPanel(String panelName) {
        JPanel newPanel = switch (panelName) {
            case "Página Principal" -> {
                // Reload the landing page content instead of reusing the same panel
                landingPage.loadDefaultLandingPageContent();
                yield landingPage.getMainPanel();
            }
            case "Mi Portal" -> new PatientPortalPanel(mainFrame, this, testPatient);
            case "Nuestros Centros" -> new CentresPanel(mainFrame, this);
            case "Quiénes Somos" -> new AboutUsPanel(mainFrame, this);
            default -> landingPage.getMainPanel();
        };

        // ✅ Ensure the content is only updated if different
        if (landingPage.getMainPanel() != newPanel) {
            landingPage.setMainContent(newPanel);
        }
    }
}
