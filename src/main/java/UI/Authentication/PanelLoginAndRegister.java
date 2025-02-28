package UI.Authentication;

import Components.CustomTextField;
import Components.CustomizedButton;
import Database.AaModels.Patient;
import Utils.Utility.ImageIconRedrawer;
import Utils.Validation.MyValidator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.BiConsumer;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;


public class PanelLoginAndRegister extends JLayeredPane {
    private final JPanel loginPanel;
    private final JPanel registerPanel;
    private final ImageIconRedrawer imageIconRedrawer;
    private final BiConsumer<MessageTypes, String> showMessageCallBack; // Function obtained from Authenticator

    // Register fields
    private CustomTextField dniTextField;
    private CustomTextField fullNameTextField;
    private CustomTextField emailTextField;
    private CustomTextField phoneTextField;
    private CustomTextField birthDateTextField;
    private CustomTextField passwordTextField;

    // Login fields
    private CustomTextField emailLoginTextField;
    private CustomTextField passwordLoginTextField;

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

        registerPanel.add(createTitleLabel("Registrarse"));

        dniTextField = createTextField("/LoginRegisterImgs/dni.png", "DNI: ");
        fullNameTextField = createTextField("/LoginRegisterImgs/usuario.png", "Nombre completo: ");
        emailTextField = createTextField("/LoginRegisterImgs/correo.png", "Email: ");
        phoneTextField = createTextField("/LoginRegisterImgs/telefono.png", "Número de teléfono: ");
        birthDateTextField = createTextField("/LoginRegisterImgs/calendario.png", "Fecha de nacimiento: ");
        passwordTextField = createTextField("/LoginRegisterImgs/contraseña.png", "Contraseña: ");

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

        loginPanel.add(createTitleLabel("Iniciar Sesión"));

        emailLoginTextField = createTextField("/LoginRegisterImgs/correo.png", "Email: ");
        passwordLoginTextField = createTextField("/LoginRegisterImgs/contraseña.png", "Contraseña: ");

        loginPanel.add(emailLoginTextField, "w 60%");
        loginPanel.add(passwordLoginTextField, "w 60%");

        CustomizedButton loginButton = createButton("Login", loginAction);
        loginPanel.add(loginButton, "w 40%, h 40");

        JButton forgotPasswordButton = createForgotPasswordButton();
        loginPanel.add(forgotPasswordButton);
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MAIN_FONT);
        label.setForeground(MAIN_APP_COLOUR);
        return label;
    }

    private CustomTextField createTextField(String iconPath, String hintText) {
        CustomTextField textField = new CustomTextField();

        imageIconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconPath)));
        ImageIcon imageIcon = imageIconRedrawer.redrawImageIcon(ICON_WIDTH, ICON_HEIGHT);

        textField.setPrefixIcon(imageIcon);
        textField.setHintText(hintText);
        return textField;
    }

    private CustomizedButton createButton(String text, ActionListener action) {
        CustomizedButton button = new CustomizedButton();
        button.setBackground(MAIN_APP_COLOUR);
        button.setForeground(Color.RED);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 16));;
        button.setText(text);
        button.addActionListener(action);
        return button;
    }

    private JButton createForgotPasswordButton() {
        JButton button = new JButton("¿Olvidó su contraseña?");
        button.setFont(MAIN_FONT);
        button.setForeground(Color.YELLOW);
        button.setBackground(MAIN_APP_COLOUR);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
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
