package MainApplication;

import AboutUs.Swing.AboutUsPanel;
import Calendar.Component.CalendarCustom;
import Calendar.Component.ClinicSpecialitySelectionDialog;
import Centres.Swing.CentresPanel;
import LandingPage.Components.NotificationPopUp;
import LandingPage.Main.LandingPage;
import Models.Patient;
import PortalPage.Swing.PatientPortalPanel;

import javax.swing.*;
import java.sql.Date;
import java.util.Optional;

/**
 * Handles navigation between different sections of the application.
 */
public class NavigationController {
    private final JFrame mainFrame;
    private final LandingPage landingPage;

    private Patient loggedInPatient = null;
    private boolean isLoggedIn = false;

    /**
     * Constructor for NavigationController.
     *
     * @param mainFrame   The main application frame.
     * @param landingPage The landing page instance.
     */
    public NavigationController(JFrame mainFrame, LandingPage landingPage) {
        this.mainFrame = mainFrame;
        this.landingPage = landingPage;
        Date sqlDate = new Date(System.currentTimeMillis());
        this.loggedInPatient = new Patient(
                "123456789B", "Juanito", "Testeo",
                "681269245", "andylopezrey@hotmail.com",
                Optional.of(sqlDate), 54, "Cuidarte0");
    }

    /**
     * Handles panel switching with login validation.
     *
     * @param panelName The name of the panel to switch to.
     */
    public void switchPanel(String panelName) {
        JPanel newPanel;

        switch (panelName) {
            case "Página Principal" -> {
                landingPage.loadDefaultLandingPageContent();
                newPanel = landingPage.getMainPanel();
            }
            case "Mi Portal" -> {
                if (!isLoggedIn) {
                    showLoginRequiredMessage();
                    return;
                }
                newPanel = new PatientPortalPanel(loggedInPatient);
            }
            case "Nuestros Centros" -> newPanel = new CentresPanel(mainFrame, this);
            case "Quiénes Somos" -> newPanel = new AboutUsPanel(mainFrame, this);
            default -> newPanel = landingPage.getMainPanel();
        }

        // Ensure the content updates only if different
        switchToPanel(newPanel);
    }


    public void showLoginRequiredMessage() {
        NotificationPopUp.showWarningMessage(
                mainFrame,
                "Acceso restringido",
                "Debe iniciar sesión o registrarse antes de acceder.");
    }

    public void switchToPanel(JPanel panel) {
        if (landingPage.getMainPanel() != panel) {
            landingPage.setMainContent(panel);
        }
    }

    public void appointmentRedirect() {
        if (!isUserLoggedIn()) {
            showLoginRequiredMessage();
            return;
        }

        // Open clinic-speciality selection dialog
        ClinicSpecialitySelectionDialog selectionDialog = new ClinicSpecialitySelectionDialog(mainFrame);
        selectionDialog.setVisible(true);

        // Retrieve the selection
        String selectedClinic = selectionDialog.getSelectedClinic();
        String selectedSpeciality = selectionDialog.getSelectedSpeciality();

        if (selectedClinic != null && selectedSpeciality != null) {
            proceedToCalendar(selectedClinic, selectedSpeciality);
        } else {
            NotificationPopUp.showErrorMessage(
                    mainFrame,
                    "Error",
                    "Hubo un error durante la selección de clínica y especialidad."
            );
        }
    }

    private void proceedToCalendar(String selectedClinic, String selectedSpeciality) {
        JFrame calendarFrame = new JFrame("Agenda de Citas - " + selectedClinic);
        calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        calendarFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        calendarFrame.setLocationRelativeTo(mainFrame);

        CalendarCustom calendar = new CalendarCustom(loggedInPatient, selectedClinic, selectedSpeciality);
        calendarFrame.add(calendar);

        calendarFrame.setVisible(true);
    }


    /**
     * Sets the login state and user session.
     *
     * @param patient The authenticated user.
     */

    public void loginUser(Patient patient) {
        this.loggedInPatient = patient;
        this.isLoggedIn = true;
    }

    /**
     * Logs out the current user.
     */

    public void logoutUser() {
        this.loggedInPatient = null;
        this.isLoggedIn = false;
    }

    public boolean isUserLoggedIn() {
        return isLoggedIn;
    }

    public Patient getLoggedInPatient() {
        return loggedInPatient;
    }
}
