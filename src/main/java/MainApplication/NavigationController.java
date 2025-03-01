package MainApplication;

import UI.AboutUs.AboutUsPanel;
import UI.Calendar.CalendarCustom;
import UI.Calendar.ClinicSpecialitySelectionDialog;
import UI.Centres.CentresPanel;
import Components.NotificationPopUp;
import UI.LandingPage.LandingPage;
import Database.Models.Patient;
import UI.PortalPage.PatientPortalPanel;

import javax.swing.*;

public class NavigationController {
    private final JFrame mainFrame;
    private final LandingPage landingPage;

    private Patient loggedInPatient = null;
    private boolean isLoggedIn = false;

    public NavigationController(JFrame mainFrame, LandingPage landingPage) {
        this.mainFrame = mainFrame;
        this.landingPage = landingPage;
    }

    // Handles navigation between different panels based on the panel name
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
            case "Nuestros Centros" -> newPanel = new CentresPanel(mainFrame);
            case "Quiénes Somos" -> newPanel = new AboutUsPanel(mainFrame);
            default -> newPanel = landingPage.getMainPanel();
        }

        // Ensure the content updates only if it is different from the current panel
        switchToPanel(newPanel);
    }

    // Displays a warning message if the user tries to access a restricted section without logging in
    public void showLoginRequiredMessage() {
        NotificationPopUp.showWarningMessage(
                mainFrame,
                "Acceso restringido",
                "Debe iniciar sesión o registrarse antes de acceder.");
    }

    // Switches to a new panel if it is different from the current one
    public void switchToPanel(JPanel panel) {
        if (landingPage.getMainPanel() != panel) {
            landingPage.setMainContent(panel);
        }
    }

    // Handles redirection to the appointment booking process
    public void appointmentRedirect() {
        if (!isUserLoggedIn()) {
            showLoginRequiredMessage();
            return;
        }

        // Open clinic-specialty selection dialog
        ClinicSpecialitySelectionDialog selectionDialog = new ClinicSpecialitySelectionDialog(mainFrame);
        selectionDialog.setVisible(true);

        // Retrieve the selection
        String selectedClinic = selectionDialog.getSelectedClinic();
        String selectedSpeciality = selectionDialog.getSelectedSpeciality();

        // Prevents redirection if the user canceled the selection
        if (selectedClinic == null || selectedSpeciality == null) {
            NotificationPopUp.showInfoMessage(
                    mainFrame,
                    "Error",
                    "Ha de seleccionar una clínica y especialidad para continuar al calendario."
            );
            return;
        }

        proceedToCalendar(selectedClinic, selectedSpeciality);
    }

    // Opens the calendar for scheduling appointments based on the selected clinic and specialty
    private void proceedToCalendar(String selectedClinic, String selectedSpeciality) {
        JFrame calendarFrame = new JFrame("Agenda de Citas - " + selectedClinic);
        calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        calendarFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        calendarFrame.setLocationRelativeTo(mainFrame);

        CalendarCustom calendar = new CalendarCustom(loggedInPatient, selectedClinic, selectedSpeciality);
        calendarFrame.add(calendar);

        calendarFrame.setVisible(true);
    }

    // Sets the login state and assigns the authenticated patient to the session
    public void loginUser(Patient patient) {
        this.loggedInPatient = patient;
        this.isLoggedIn = true;
    }

    // Logs out the current user and resets the session
    public void logoutUser() {
        this.loggedInPatient = null;
        this.isLoggedIn = false;
    }

    // Returns whether a user is logged in
    public boolean isUserLoggedIn() {
        return isLoggedIn;
    }

    // Retrieves the currently logged-in patient
    public Patient getLoggedInPatient() {
        return loggedInPatient;
    }
}
