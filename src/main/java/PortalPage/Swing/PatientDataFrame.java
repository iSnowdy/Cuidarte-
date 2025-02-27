package PortalPage.Swing;

import DAO.PatientDAO;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import LandingPage.Components.NotificationPopUp;
import Models.Patient;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientDataFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(PatientDataFrame.class.getName());

    private final Patient patient;
    private final PatientDAO patientDAO;

    private JTextField txtDNI, txtName, txtSurname, txtPhone, txtEmail, txtBirthdate, txtAge;
    private JButton btnEditSave, btnClose;
    private boolean isEditing = false;

    public PatientDataFrame(Patient patient) {
        this.patient = patient;
        try {
            this.patientDAO = new PatientDAO();
        } catch (DatabaseQueryException e) {
            throw new RuntimeException("Error initializing PatientDAO", e);
        }

        initializeFrame();
        addComponents();
        loadPatientData();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Datos del Paciente");
        setSize(700, 500);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Title Section with Spacing
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Datos del Paciente", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding around the title

        // Separator for better visual organization
        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2)); // Full-width

        // Adding spacing using vertical struts
        titlePanel.add(Box.createVerticalStrut(10)); // Space above title
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10)); // Space between title and separator
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15)); // Space between separator and form

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        addField(formPanel, "DNI:", txtDNI = createTextField(false), gbc, 0);
        addField(formPanel, "Nombre:", txtName = createTextField(false), gbc, 1);
        addField(formPanel, "Apellidos:", txtSurname = createTextField(false), gbc, 2);
        addField(formPanel, "Fecha de Nacimiento:", txtBirthdate = createTextField(false), gbc, 3);
        addField(formPanel, "Edad:", txtAge = createTextField(false), gbc, 4);
        addField(formPanel, "Teléfono:", txtPhone = createTextField(false), gbc, 5);
        addField(formPanel, "Email:", txtEmail = createTextField(false), gbc, 6);

        return formPanel;
    }

    private void addField(JPanel panel, String label, JTextField textField, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createFormLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    private JTextField createTextField(boolean isEditable) {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(isEditable ? Color.WHITE : Colors.TEXTFIELD_BACKGROUND_COLOUR);
        textField.setEditable(isEditable);
        textField.setPreferredSize(new Dimension(250, 30));
        return textField;
    }

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

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private void loadPatientData() {
        txtDNI.setText(patient.getDNI());
        txtName.setText(patient.getFirstName());
        txtSurname.setText(patient.getSurname());
        txtBirthdate.setText(patient.getDateOfBirth());
        txtAge.setText(String.valueOf(patient.getAge()));
        txtPhone.setText(patient.getPhoneNumber());
        txtEmail.setText(patient.getEmail());
    }

    private void toggleEditMode() {
        if (!isEditing) {
            enterEditMode();
        } else {
            savePatientData();
        }
    }

    private void enterEditMode() {
        txtPhone.setEditable(true);
        txtEmail.setEditable(true);
        txtPhone.setBackground(Color.WHITE);
        txtEmail.setBackground(Color.WHITE);
        btnEditSave.setText("Guardar");
        isEditing = true;
    }

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
