package PortalPage.Swing;


import Models.Patient;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PatientDataFrame extends JFrame {
    // Model for patient data (using temporary hardcoded data for now)
    private Patient patient;

    // UI Components for patient data
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtAddress;
    private JTextField txtPhone;

    // Buttons for toggling edit mode and closing the frame
    private JButton btnEditSave;
    private JButton btnClose;

    // Flag indicating if the form is in edit mode
    private boolean isEditing = false;

    // Constructor accepting a TestPatient object
    public PatientDataFrame(Patient patient) {
        this.patient = patient;
        initializeFrame();
        addComponents();
        loadPatientData();
        setVisible(true);
    }

    // Configure frame properties
    private void initializeFrame() {
        setTitle("Datos del Paciente");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Add main components to the frame
    private void addComponents() {
        JPanel mainPanel = createMainPanel();
        getContentPane().add(mainPanel);
    }

    // Create the main panel that holds the title, form, and button panels
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.add(createTitleLabel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return mainPanel;
    }

    // Create and return the title label
    private JLabel createTitleLabel() {
        JLabel lblTitle = new JLabel("Datos del Paciente", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Colors.MAIN_APP_COLOUR);
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        return lblTitle;
    }

    // Create and return the form panel that displays patient data
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createFormLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtName = createTextField(20);
        formPanel.add(txtName, gbc);

        // Row 2: Age
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createFormLabel("Edad:"), gbc);
        gbc.gridx = 1;
        txtAge = createTextField(5);
        formPanel.add(txtAge, gbc);

        // Row 3: Address
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createFormLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtAddress = createTextField(20);
        formPanel.add(txtAddress, gbc);

        // Row 4: Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createFormLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtPhone = createTextField(15);
        formPanel.add(txtPhone, gbc);

        return formPanel;
    }

    // Helper to create a form label
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    // Helper to create a text field in view mode (non-editable with background color)
    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
        textField.setEditable(false);
        return textField;
    }

    // Create and return the button panel with Edit/Save and Close buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        btnEditSave = createButton("Editar", Colors.MAIN_APP_COLOUR);
        btnEditSave.addActionListener(e -> toggleEditMode());
        buttonPanel.add(btnEditSave);

        btnClose = createButton("Cerrar", Colors.MY_RED);
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);

        return buttonPanel;
    }

    // Helper to create a styled button with given text and background color
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        return button;
    }

    // Load the patient data from the model into the text fields
    private void loadPatientData() {
        txtName.setText(patient.getFirstName());
        txtAge.setText(String.valueOf(patient.getAge()));
        txtAddress.setText(patient.getSurname());
        txtPhone.setText(patient.getPhoneNumber());
    }

    // Toggle between view mode and edit mode
    private void toggleEditMode() {
        if (!isEditing) {
            enterEditMode();
        } else {
            savePatientData();
        }
    }

    // Enable edit mode: make text fields editable and update their background color
    private void enterEditMode() {
        setFieldsEditable(true);
        setFieldsBackground(Color.WHITE);
        btnEditSave.setText("Guardar");
        isEditing = true;
    }

    // Save changes: update the model, disable editing, and revert background color
    private void savePatientData() {
        try {
            int age = Integer.parseInt(txtAge.getText().trim());
            // In an MVC structure, here we would notify the controller of the changes.
            //patient = new Patient(patient.getId(), txtName.getText().trim(), age, txtAddress.getText().trim(), txtPhone.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setFieldsEditable(false);
        setFieldsBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
        btnEditSave.setText("Editar");
        isEditing = false;
        loadPatientData(); // Refresh displayed data
    }

    // Set the editability of all text fields
    private void setFieldsEditable(boolean editable) {
        txtName.setEditable(editable);
        txtAge.setEditable(editable);
        txtAddress.setEditable(editable);
        txtPhone.setEditable(editable);
    }

    // Set the background color of all text fields
    private void setFieldsBackground(Color color) {
        txtName.setBackground(color);
        txtAge.setBackground(color);
        txtAddress.setBackground(color);
        txtPhone.setBackground(color);
    }
}
