package UI.Authentication;

import Components.CustomPasswordField;
import Components.CustomTextField;
import Components.CustomizedButton;
import Components.ShadowedLabel;
import Database.DAO.PatientDAO;
import Database.DAO.PatientServices;
import Database.Models.Patient;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Utils.Utility.ImageIconRedrawer;
import Utils.Utility.JavaMailSender;
import Utils.Utility.PasswordHasher;
import Utils.Validation.MyValidator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;
import static Utils.Swing.Fonts.AUTH_TITLE_LABEL_FONT;
import static Utils.Swing.Fonts.MAIN_FONT;


public class PanelLoginAndRegister extends JLayeredPane {
    private final JPanel loginPanel;
    private final JPanel registerPanel;
    private final ImageIconRedrawer imageIconRedrawer;
    private final BiConsumer<MessageTypes, String> showMessageCallBack; // Function obtained from Authenticator

    // Register fields
    private CustomTextField
            dniTextField, birthDateTextField, fullNameTextField, emailTextField, phoneTextField;
    private CustomPasswordField passwordTextField;

    // Login fields
    private CustomTextField emailLoginTextField;
    private CustomPasswordField passwordLoginTextField;

    // Temporary storage for password reset stuff
    private final Map<String, String> tempPasswords = new HashMap<>();

    private final int ICON_WIDTH = 20;
    private final int ICON_HEIGHT = 20;

    public PanelLoginAndRegister(ActionListener registerAction, ActionListener loginAction, BiConsumer<MessageTypes, String> messageAction) {
        this.showMessageCallBack = messageAction;

        setOpaque(false);
        setLayout(new CardLayout());

        imageIconRedrawer = new ImageIconRedrawer();

        loginPanel = createPanel();
        registerPanel = createPanel();

        initRegister(registerAction);
        initLogin(loginAction);

        add(loginPanel, "login");
        add(registerPanel, "register");

        showRegisterForm(true);
    }


    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private void initRegister(ActionListener registerAction) {
        registerPanel.setLayout(new MigLayout("wrap", "push[center]push", "push[]35[]10[]10[]10[]10[]10[]40[]push"));

        registerPanel.add(createShadowedTitleLabel("Registrarse"));

        dniTextField = createTextField("/LoginRegisterImgs/dni.png", "DNI: ");
        fullNameTextField = createTextField("/LoginRegisterImgs/usuario.png", "Nombre completo: ");
        emailTextField = createTextField("/LoginRegisterImgs/correo.png", "Email: ");
        phoneTextField = createTextField("/LoginRegisterImgs/telefono.png", "Número de teléfono: ");
        birthDateTextField = createTextField("/LoginRegisterImgs/calendario.png", "Fecha de nacimiento: ");
        passwordTextField = createPasswordTextField("Contraseña: ");

        registerPanel.add(dniTextField, "w 60%");
        registerPanel.add(fullNameTextField, "w 60%");
        registerPanel.add(emailTextField, "w 60%");
        registerPanel.add(phoneTextField, "w 60%");
        registerPanel.add(birthDateTextField, "w 60%");
        registerPanel.add(passwordTextField, "w 60%");

        CustomizedButton registerButton = createButton("Registrarse", registerAction);
        registerPanel.add(registerButton, "w 40%, h 40");
    }

    private void initLogin(ActionListener loginAction) {
        loginPanel.setLayout(new MigLayout("wrap", "push[center]push", "push[]35[]10[]10[]40[]push"));

        loginPanel.add(createShadowedTitleLabel("Iniciar Sesión"));

        emailLoginTextField = createTextField("/LoginRegisterImgs/correo.png", "Email: ");
        passwordLoginTextField = createPasswordTextField("Contraseña: ");

        loginPanel.add(emailLoginTextField, "w 60%");
        loginPanel.add(passwordLoginTextField, "w 60%");

        CustomizedButton loginButton = createButton("Login", loginAction);
        loginPanel.add(loginButton, "w 40%, h 40");

        JButton forgotPasswordButton = createForgotPasswordButton();
        loginPanel.add(forgotPasswordButton);
    }

    private ShadowedLabel createShadowedTitleLabel(String text) {
        return new ShadowedLabel(text, AUTH_TITLE_LABEL_FONT, MAIN_APP_COLOUR);
    }

    private CustomTextField createTextField(String iconPath, String hintText) {
        CustomTextField textField = new CustomTextField();

        imageIconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconPath)));
        ImageIcon imageIcon = imageIconRedrawer.redrawImageIcon(ICON_WIDTH, ICON_HEIGHT);

        textField.setPrefixIcon(imageIcon);
        textField.setHintText(hintText);
        textField.setFont(MAIN_FONT);
        return textField;
    }

    private CustomPasswordField createPasswordTextField(String hintText) {
        CustomPasswordField customPasswordField = new CustomPasswordField();

        imageIconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/contraseña.png")));
        ImageIcon imageIcon = imageIconRedrawer.redrawImageIcon(ICON_WIDTH, ICON_HEIGHT);

        customPasswordField.setPrefixIcon(imageIcon);
        customPasswordField.setHintText(hintText);
        return customPasswordField;
    }

    private CustomizedButton createButton(String text, ActionListener action) {
        CustomizedButton button = new CustomizedButton();
        button.setBackground(MAIN_APP_COLOUR);
        button.setForeground(Color.WHITE);
        button.setFont(MAIN_FONT);
        ;
        button.setText(text);
        button.addActionListener(action);
        return button;
    }

    private JButton createForgotPasswordButton() {
        JButton button = new JButton("¿Olvidó su contraseña?");
        button.setFont(MAIN_FONT.deriveFont(Font.ITALIC));
        button.setForeground(Color.BLACK);
        button.setBackground(SECONDARY_APP_COLOUR);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> handleForgotPassword());
        return button;
    }

    private void handleForgotPassword() {
        String email = emailLoginTextField.getText().trim();

        if (email.isEmpty()) {
            showMessageCallBack.accept(MessageTypes.ERROR, "Por favor, " +
                    "introduzca su email para recuperar la contraseña.");
            return;
        }

        Optional<Patient> optionalPatient = findPatientByEmail(email);
        if (optionalPatient.isEmpty()) {
            showMessageCallBack.accept(MessageTypes.ERROR, "No se encontró una cuentra con el correo:\n" + email);
            return;
        }

        Patient patient = optionalPatient.get();
        JavaMailSender mailSender = new JavaMailSender(patient.getEmail());
        String newTempPassword = mailSender.sendPasswordResetEmail(patient.getFirstName() + " " + patient.getSurname());

        // Store the generated password in memory for now
        tempPasswords.put(patient.getDNI(), newTempPassword);

        SwingUtilities.invokeLater(() -> {
            String enteredPassword = PasswordVerificationDialog.showPasswordInputDialog(this);
            if (enteredPassword != null && !enteredPassword.trim().isEmpty()) {
                verifyAndPersistPassword(patient, enteredPassword);
            }
        });
    }

    private Optional<Patient> findPatientByEmail(String email) {
        try {
            PatientDAO patientDAO = new PatientDAO();
            return patientDAO.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Handles password verification and update through the dialog created
    public boolean verifyAndPersistPassword(Patient patient, String enteredPassword) {
        String storedTempPassword = tempPasswords.get(patient.getDNI());

        if (storedTempPassword == null || !storedTempPassword.equals(enteredPassword)) {
            showMessageCallBack.accept(MessageTypes.ERROR, "La contraseña ingresada no coincide con la enviada a" +
                    "su correo.");
            return false;
        }
        // Sets the new password after hashing it
        patient.setPassword(PasswordHasher.hashPassword(enteredPassword, patient.getSalt()));

        try {
            if (new PatientDAO().update(patient)) { // Persists
                // Removes the temp password for memory now that it has been updated
                tempPasswords.remove(patient.getDNI());
                showMessageCallBack.accept(MessageTypes.SUCCESS, "Su contraseña ha sido actualizada correctamente.");
                return true;
            } else {
                showMessageCallBack.accept(MessageTypes.ERROR, "Hubo un error al actualizar la contraseña.");
                return false;
            }
        } catch (DatabaseOpeningException | DatabaseQueryException e) {
            showMessageCallBack.accept(MessageTypes.ERROR, "Hubo un error al actualizar la contraseña.");
            return false;
        }
    }

    // Switch between login and register forms
    public void showRegisterForm(boolean show) {
        registerPanel.setVisible(show);
        loginPanel.setVisible(!show);
    }

    // Retrieves a Patient object based on the input fields
    public Optional<Patient> getPatientData() {
        String dni = dniTextField.getText().trim();
        String fullName = fullNameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        String birthDate = birthDateTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (!MyValidator.isValidDNIFormat(dni)) {
            showMessageCallBack.accept(MessageTypes.ERROR, "DNI inválido. Verifique el formato.");
            return Optional.empty();
        }
        if (!MyValidator.isValidTextLength(fullName, 3)) {
            showMessageCallBack.accept(MessageTypes.ERROR, "El nombre debe tener al menos 3 caracteres.");
            return Optional.empty();
        }
        if (!MyValidator.isValidEmailAddress(email)) {
            System.out.println("Email is: " + email);
            showMessageCallBack.accept(MessageTypes.ERROR, "Formato del correo electrónico inválido.");
            return Optional.empty();
        }
        if (!MyValidator.isValidPhoneNumber(phone)) {
            showMessageCallBack.accept(MessageTypes.ERROR, "Formato del número de teléfono inválido.");
            return Optional.empty();
        }
        if (!MyValidator.isValidPassword(password)) {
            showMessageCallBack.accept(MessageTypes.ERROR, "La contraseña debe tener al menos 6 caracteres y un número.");
            return Optional.empty();
        }

        String[] nameSplit = nameAndSurnamesSplit(fullName);

        Optional<java.sql.Date> parsedDate = MyValidator.validateAndParseDate(birthDate);
        if (parsedDate.isEmpty()) {
            showMessageCallBack.accept(MessageTypes.ERROR, "Fecha de nacimiento inválida. Use el formato YYYY-MM-DD.");
            return Optional.empty();
        }

        int age = calculateAge(parsedDate.get());

        return Optional.of(new Patient(dni, nameSplit[0], nameSplit[1], phone, email, Optional.of(parsedDate.get()), age, password));
    }

    private String[] nameAndSurnamesSplit(String fullname) {
        String[] parts = fullname.split(" ", 2); // Max split it twice

        String name = parts[0].trim();
        String surnames = parts.length > 1 ? parts[1].trim() : "";

        return new String[]{name, surnames};
    }

    private int calculateAge(Date birthDate) {
        LocalDate birthLocalDate = birthDate.toLocalDate();
        return Period.between(birthLocalDate, LocalDate.now()).getYears();
    }


    // Retrieves the email input from the login form
    public String getEmailInputFromLogin() {
        return emailLoginTextField.getText().trim();
    }

    // Retrieves the password input from the login form
    public String getPasswordInputFromLogin() {
        return passwordLoginTextField.getText().trim();
    }
}
