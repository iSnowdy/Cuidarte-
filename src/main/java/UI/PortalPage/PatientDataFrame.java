package UI.PortalPage;

import Database.DAO.PatientDAO;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Components.NotificationPopUp;
import Database.AaModels.Patient;
import Utils.Swing.Colors;
import Utils.Utility.CustomLogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientDataFrame extends JFrame {
    private static final Logger LOGGER = CustomLogger.getLogger(PatientDataFrame.class);

    private final Patient patient;
    private final PatientDAO patientDAO;

    private JTextField txtDNI, txtName, txtSurname, txtPhone, txtEmail, txtBirthdate, txtAge;
    private JButton btnEditSave, btnClose;
    private boolean isEditing = false;

    public PatientDataFrame(Patient patient) {
        this.patient = patient;
        this.patientDAO = initializePatientDAO();
        initializeFrame();
        addComponents();
        loadPatientData();
        setVisible(true);
    }

    // Initializes the PatientDAO while handling potential database errors
    private PatientDAO initializePatientDAO() {
        try {
            return new PatientDAO();
        } catch (DatabaseQueryException e) {
            throw new RuntimeException("Error initializing PatientDAO", e);
        }
    }

    // Configures the frame settings
    private void initializeFrame() {
        setTitle("Datos del Paciente");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Adds all UI components to the frame
    private void addComponents() {
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
    }

    // Creates the main panel with proper padding and layout
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        return panel;
    }

    // Creates the title panel with a header and a separator
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Datos del Paciente", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2));

        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15));

        return titlePanel;
    }

    // Creates the form panel with all patient details
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createGridBagConstraints();

        addField(formPanel, "DNI:", txtDNI = createTextField(false), gbc, 0);
        addField(formPanel, "Nombre:", txtName = createTextField(false), gbc, 1);
        addField(formPanel, "Apellidos:", txtSurname = createTextField(false), gbc, 2);
        addField(formPanel, "Fecha de Nacimiento:", txtBirthdate = createTextField(false), gbc, 3);
        addField(formPanel, "Edad:", txtAge = createTextField(false), gbc, 4);
        addField(formPanel, "Teléfono:", txtPhone = createTextField(false), gbc, 5);
        addField(formPanel, "Email:", txtEmail = createTextField(false), gbc, 6);

        return formPanel;
    }

    // Configures GridBagConstraints for uniform field alignment
    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    // Adds a field with label and text box
    private void addField(JPanel panel, String label, JTextField textField, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createFormLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    // Creates labels for form fields
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    // Creates text fields with a standard design
    private JTextField createTextField(boolean isEditable) {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(isEditable ? Color.WHITE : Colors.TEXTFIELD_BACKGROUND_COLOUR);
        textField.setEditable(isEditable);
        textField.setPreferredSize(new Dimension(250, 30));
        return textField;
    }

    // Creates the button panel with edit/save and close buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        btnEditSave = createButton("Editar", Colors.MAIN_APP_COLOUR);
        btnEditSave.addActionListener(e -> toggleEditMode());
        buttonPanel.add(btnEditSave);

        buttonPanel.add(Box.createHorizontalStrut(20));

        btnClose = createButton("Cerrar", Colors.MY_RED);
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);

        return buttonPanel;
    }

    // Creates a standard button with defined styles
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    // Loads patient data into the text fields
    private void loadPatientData() {
        txtDNI.setText(patient.getDNI());
        txtName.setText(patient.getFirstName());
        txtSurname.setText(patient.getSurname());
        txtBirthdate.setText(patient.getDateOfBirth());
        txtAge.setText(String.valueOf(patient.getAge()));
        txtPhone.setText(patient.getPhoneNumber());
        txtEmail.setText(patient.getEmail());
    }

    // Toggles between edit and save modes
    private void toggleEditMode() {
        if (!isEditing) {
            enterEditMode();
        } else {
            savePatientData();
        }
    }

    // Enables edit mode for phone and email fields
    private void enterEditMode() {
        txtPhone.setEditable(true);
        txtEmail.setEditable(true);
        txtPhone.setBackground(Color.WHITE);
        txtEmail.setBackground(Color.WHITE);
        btnEditSave.setText("Guardar");
        isEditing = true;
    }

    // Saves patient data and updates the database
    private void savePatientData() {
        String newPhone = txtPhone.getText().trim();
        String newEmail = txtEmail.getText().trim();

        try {
            patient.setPhoneNumber(newPhone);
            patient.setEmail(newEmail);
            patientDAO.update(patient);

            txtPhone.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
            txtEmail.setBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
            btnEditSave.setText("Editar");
            isEditing = false;

            NotificationPopUp.showInfoMessage(this, "Datos actualizados correctamente.", "Éxito");
        } catch (DatabaseUpdateException e) {
            LOGGER.log(Level.SEVERE, "Error updating patient data", e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudieron actualizar los datos.");
        }
    }
}
