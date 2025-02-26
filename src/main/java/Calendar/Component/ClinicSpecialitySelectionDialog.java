package Calendar.Component;

import DAO.ClinicDAO;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import LandingPage.Components.NotificationPopUp;
import Models.Clinic;
import Utils.Utility.CustomLogger;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class ClinicSpecialitySelectionDialog extends JDialog {
    private final Logger LOGGER = CustomLogger.getLogger(ClinicSpecialitySelectionDialog.class);

    private JComboBox<String> clinicComboBox;
    private JComboBox<String> specialityComboBox;
    private JButton nextButton;
    private JButton cancelButton;

    private final Map<String, List<String>> clinicSpecialtyMap; // Stores clinics and their specialties
    private String selectedClinic;
    private String selectedSpecialty;

    public ClinicSpecialitySelectionDialog(JFrame parent) {
        super(parent, "Seleccione clínica y especialidad", true);
        this.clinicSpecialtyMap = new HashMap<>();

        initializeUI();
        setLocationRelativeTo(parent); // Centers the window
    }

    // Initializes UI components and layout
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 250);
        setResizable(false);

        JPanel contentPanel = createContentPanel();
        JPanel buttonPanel = createButtonPanel();

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Creates the content panel containing clinic and specialty selection
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        clinicComboBox = createClinicComboBox();
        specialityComboBox = createSpecialtyComboBox();

        JLabel clinicLabel = new JLabel("Seleccione una clínica: ");
        clinicLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel specialityLabel = new JLabel("Seleccione una especialidad: ");
        specialityLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        contentPanel.add(clinicLabel);
        contentPanel.add(clinicComboBox);
        contentPanel.add(specialityLabel);
        contentPanel.add(specialityComboBox);

        return contentPanel;
    }

    // Creates the button panel with "Next" and "Cancel" buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        nextButton = createNextButton();
        cancelButton = createCancelButton();

        buttonPanel.add(cancelButton);
        buttonPanel.add(nextButton);

        return buttonPanel;
    }

    // Creates the clinic selection combo box and loads clinics from the DB
    private JComboBox<String> createClinicComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> updateSpecialtyList());
        loadClinicsFromDB(); // Load clinics from DB
        return comboBox;
    }

    // Updates the specialty list when a clinic is selected
    private void updateSpecialtyList() {
        selectedClinic = (String) clinicComboBox.getSelectedItem();

        // Avoids running the query if no clinic hsa been selected yet
        if (selectedClinic == null || selectedClinic.isEmpty()) return;

        try {
            ClinicDAO clinicDAO = new ClinicDAO();
            Optional<Integer> optionalClinicID = clinicDAO.getClinicIDByName(selectedClinic);
            if (optionalClinicID.isEmpty()) {
                NotificationPopUp.showErrorMessage(
                        this, "Error", "No hay especialidades disponibles.");
                return;
            }

            List<String> clinicSpecialities = clinicDAO.getSpecialitiesByClinic(optionalClinicID.get());

            SwingUtilities.invokeLater(() -> {
                specialityComboBox.removeAllItems();
                clinicSpecialities.forEach(specialityComboBox::addItem);
                specialityComboBox.setEnabled(!clinicSpecialities.isEmpty());
            });

        } catch (DatabaseOpeningException | DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Could not load specialities from database for specialities ComboBox", e);
            NotificationPopUp.showErrorMessage(
                    this, "Error", "Error al cargar especialidades.");
        }

        List<String> specialties = clinicSpecialtyMap.get(selectedClinic);

        specialityComboBox.removeAllItems();
        if (specialties != null) {
            for (String specialty : specialties) {
                specialityComboBox.addItem(specialty);
            }
            specialityComboBox.setEnabled(true);
        }

        specialityComboBox.addActionListener(e -> checkSelection());
    }

    private void loadClinicsFromDB() {
        try {
            ClinicDAO clinicDAO = new ClinicDAO();
            List<Clinic> allClinics = clinicDAO.findAll();

            if (allClinics.isEmpty()) {
                NotificationPopUp.showErrorMessage(
                        this, "Error", "No hay clínicas disponibles."
                );
                return;
            }

            clinicSpecialtyMap.clear();
            // Iterates through the List of all clinics and places their name in the map while
            // also adding a placeholder for the specialities later on
            allClinics.forEach(clinic -> clinicSpecialtyMap.put(clinic.getName(), new ArrayList<>()));

            SwingUtilities.invokeLater(() -> {
                clinicComboBox.setModel(new DefaultComboBoxModel<>(clinicSpecialtyMap.keySet().toArray(new String[0])));
                // No clinic is selected per default
                clinicComboBox.setSelectedIndex(-1);
            });

        } catch (DatabaseOpeningException | DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Could not load clinics from database for specialities ComboBox", e);
            NotificationPopUp.showErrorMessage(
                    this, "Error", "No se pudo cargar la información de clínicas.");
        }
    }

    // Creates the specialty selection combo box
    private JComboBox<String> createSpecialtyComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setEnabled(false); // Disabled until a clinic is selected
        return comboBox;
    }

    // Creates the "Next" button
    private JButton createNextButton() {
        JButton button = new JButton("Siguiente");
        button.setEnabled(false);
        button.setBackground(MAIN_APP_COLOUR);
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> proceedToCalendar());
        return button;
    }

    // Creates the "Cancel" button
    private JButton createCancelButton() {
        JButton button = new JButton("Cancelar");
        button.addActionListener(e -> dispose());
        return button;
    }

    // Checks if both clinic and specialty are selected to enable the "Next" button
    private void checkSelection() {
        selectedClinic = (String) clinicComboBox.getSelectedItem();
        selectedSpecialty = (String) specialityComboBox.getSelectedItem();

        boolean isSelectionValid = selectedClinic != null && selectedSpecialty != null;

        SwingUtilities.invokeLater(() -> nextButton.setEnabled(isSelectionValid));
    }

    // Displays a confirmation popup and proceeds to the calendar
    private void proceedToCalendar() {
        NotificationPopUp.showCustomNotification(
                (JFrame) getParent(),
                "Selección de clínica y especialidad confirmada",
                "Clínica: " + selectedClinic + "\n\n Especialidad: " + selectedSpecialty,
                "Continuar",
                e -> dispose() // Closes the dialog when the button is clicked
        );
    }

    public String getSelectedClinic() {
        return selectedClinic;
    }

    public String getSelectedSpeciality() {
        return selectedSpecialty;
    }
}
