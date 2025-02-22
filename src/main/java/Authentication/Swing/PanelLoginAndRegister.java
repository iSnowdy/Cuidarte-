package Authentication.Swing;

import Authentication.Components.CustomTextField;
import Authentication.Components.CustomizedButton;
import Models.Doctor;
import Models.Patient;
import Utils.Utility.ImageIconRedrawer;
import Utils.Validation.AuthenticationValidator;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

public class PanelLoginAndRegister extends JLayeredPane {
    private JPanel login, register;

    private ImageIconRedrawer imageIconRedrawer;
    private Patient patient;
    private Doctor doctor;

    public PanelLoginAndRegister(ActionListener eventRegister) {
        setOpaque(false);
        setLayout(new CardLayout());
        initComponents();
        initRegister(eventRegister);
        initLogin();

        // Register by default
        this.login.setVisible(false);
        this.register.setVisible(true);
    }

    private void initComponents() {
        this.imageIconRedrawer = new ImageIconRedrawer();

        this.login = new JPanel();
        this.register = new JPanel();

        this.login.setBackground(Color.WHITE);
        this.register.setBackground(Color.WHITE);

        add(login, "login");
        add(register, "register");
    }

    // TODO: Add the logo at the top
    private void initRegister(ActionListener eventRegister) {
        // Adjust space between elements
        this.register.setLayout(new MigLayout("wrap", "push[center]push", "push[]35[]10[]10[]10[]10[]10[]40[]push"));
        JLabel registerLabel = new JLabel("Registrarse");
        registerLabel.setFont(MAIN_FONT); // TODO: Font
        registerLabel.setForeground(MAIN_APP_COLOUR);
        this.register.add(registerLabel);

        // TODO: Data Validation. Data Input to DB. Hash password. Use another thing for password and email?
        // TODO: Refactor this? Too many blocks of code very similar to each other
        // TODO: Use ImageIconRedrawer to redraw images
        CustomTextField dniTextField = generateCustomTextField(
                "C:\\DAM\\identification-card-fill-svgrepo-com(1).png",
                20,
                20,
                "DNI: "
        );
        this.register.add(dniTextField, "w 60%");  // TODO: Consider editing the width
        
        CustomTextField usernameTextField = generateCustomTextField(
                "/LoginRegisterImgs/usuario.png",
                20,
                20,
                "Nombre completo: "
        );
        this.register.add(usernameTextField, "w 60");

        CustomTextField userEmail = new CustomTextField();
        userEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/correo.png")));
        userEmail.setHintText("Email");
        this.register.add(userEmail, "w 60%");
        
        CustomTextField emailTextField = generateCustomTextField(
                "/LoginRegisterImgs/correo.png",
                20,
                20,
                "Email: "
        );
        this.register.add(emailTextField, "w 60%");

        CustomTextField phoneNumberTExtField = generateCustomTextField(
                "/LoginRegisterImgs/telefono.png",
                20,
                20,
                "Número de teléfono: "
        );
        this.register.add(phoneNumberTExtField, "w 60%");
        
        CustomTextField dateOfBirthTextField = generateCustomTextField(
                "/LoginRegisterImgs/calendario.png",
                20,
                20,
                "Fecha de nacimiento: "
        );
        this.register.add(dateOfBirthTextField, "w 60%");

        CustomTextField passwordTextField = generateCustomTextField(
                "/LoginRegisterImgs/contraseña.png",
                20,
                20,
                "Contraseña: "
        );
        this.register.add(passwordTextField, "w 60%");

        CustomizedButton registerButton = new CustomizedButton();
        registerButton.setBackground(MAIN_APP_COLOUR); // TODO: Colours?
        registerButton.setForeground(Color.red); // TODO: Font?
        registerButton.setFont(MAIN_FONT);
        registerButton.addActionListener(eventRegister);
        // TODO: setText vs setLabel?
        registerButton.setLabel("Registrarse");
        this.register.add(registerButton, "w 40%, h 40");
        // TODO: Refactor this
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String dni = dniTextField.getText().trim();
                String username = usernameTextField.getText().trim();
                String email = userEmail.getText().trim();
                String phoneNumber = phoneNumberTExtField.getText().trim();
                String dateOfBirth = dateOfBirthTextField.getText().trim();
                String password = passwordTextField.getText().trim();

                String[] nameAndSurname = nameAndSurnamesSplitted(username);

                // TODO: Do this properly please ty. Validate all data before inserting
                var formattedDate = AuthenticationValidator.validateAndParseDate(dateOfBirth);
                //patient = new Patient(dni, nameAndSurname[0], nameAndSurname[1], phoneNumber, email, formattedDate, 50, password, 50);
            }
        });
    }

    private void initLogin() {
        // Adjust space between elements
        // TODO: Check the amount of elements present. Login Button or Forgot Password first? Separation between them?
        this.login.setLayout(new MigLayout("wrap", "push[center]push", "push[]35[]10[]10[]40[]push"));
        JLabel loginLabel = new JLabel("Iniciar Sesión");
        loginLabel.setFont(MAIN_FONT); // TODO: Font
        loginLabel.setForeground(MAIN_APP_COLOUR);
        this.login.add(loginLabel);

        CustomTextField userEmail = new CustomTextField();
        userEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/correo.png")));
        userEmail.setHintText("Email");
        this.login.add(userEmail, "w 60%");

        CustomTextField userPassword = new CustomTextField();
        userPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/contraseña.png")));
        userPassword.setHintText("Contraseña");
        this.login.add(userPassword, "w 60%");

        CustomizedButton loginButton = new CustomizedButton();
        loginButton.setBackground(MAIN_APP_COLOUR);
        loginButton.setForeground(Color.red);
        loginButton.setFont(MAIN_FONT);
        loginButton.setLabel("Login");
        this.login.add(loginButton, "w 40%, h 40");

        JButton forgotPasswordButton = new JButton("¿Olvidó su contraseña?"); // TODO: JMail?
        forgotPasswordButton.setFont(MAIN_FONT);
        forgotPasswordButton.setForeground(Color.YELLOW); // TODO: Stylize it
        forgotPasswordButton.setBackground(MAIN_APP_COLOUR);
        forgotPasswordButton.setContentAreaFilled(false); // ?
        forgotPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.login.add(forgotPasswordButton);
    }

    public void showRegisterForm(boolean show) {
        if (show) {
            this.register.setVisible(true);
            this.login.setVisible(false);
        } else {
            this.register.setVisible(false);
            this.login.setVisible(true);
        }
    }

    public Patient getPatient() {
        return patient;
    }
    
    private CustomTextField generateCustomTextField(String imagePath, int width, int height, String textHint) {
        CustomTextField generatedTextField = new CustomTextField();
        imageIconRedrawer.setImageIcon(new ImageIcon(imagePath));
        ImageIcon imageIcon = imageIconRedrawer.redrawImageIcon(width, height);
        generatedTextField.setPrefixIcon(imageIcon);
        
        generatedTextField.setHintText(textHint);
        
        return generatedTextField;
    }

    private String[] nameAndSurnamesSplitted(String fullname) {
        String [] parts = fullname.split(" ", 2); // Max split it twice

        String name = parts[0].trim();
        String surnames = parts.length > 1 ? parts[1].trim() : "";

        return new String[]{name, surnames};
    }
}
