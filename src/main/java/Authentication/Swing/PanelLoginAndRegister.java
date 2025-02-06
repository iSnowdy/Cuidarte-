package Authentication.Swing;

import Authentication.Components.CustomTextField;
import Authentication.Components.CustomizedButton;
import Authentication.DataExample.UserExample;
import Models.Doctor;
import Models.Patient;
import Utils.Utility.ImageIconRedrawer;
import Utils.Utility.Validations;
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
        login = new JPanel();
        register = new JPanel();

        login.setBackground(Color.WHITE);
        register.setBackground(Color.WHITE);

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
        CustomTextField userDNI = new CustomTextField();
        ImageIconRedrawer imageIconRedrawer = new ImageIconRedrawer();
        imageIconRedrawer.setImageIcon(new ImageIcon("C:\\DAM\\identification-card-fill-svgrepo-com(1).png"));
        ImageIcon imageIconDNI = imageIconRedrawer.redrawImageIcon(20, 20);
        //System.out.println("Sizes are: " + imageIconDNI.getIconWidth() + " : " + imageIconDNI.getIconHeight());
        userDNI.setPrefixIcon(imageIconDNI);
        userDNI.setHintText("DNI: ");
        this.register.add(userDNI, "w 60%");

        CustomTextField userName = new CustomTextField();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/LoginRegisterImgs/usuario.png"));
        userName.setPrefixIcon(imageIcon);
        userName.setHintText("Nombre completo");
        this.register.add(userName, "w 60%"); // TODO: Consider editing the width

        CustomTextField userEmail = new CustomTextField();
        userEmail.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/correo.png")));
        userEmail.setHintText("Email");
        this.register.add(userEmail, "w 60%");

        CustomTextField userPhoneNumber = new CustomTextField();
        userPhoneNumber.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/telefono.png")));
        userPhoneNumber.setHintText("Número de teléfono");
        this.register.add(userPhoneNumber, "w 60%");

        CustomTextField userDateOfBirth = new CustomTextField();
        userDateOfBirth.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/calendario.png")));
        userDateOfBirth.setHintText("Fecha de nacimiento");
        this.register.add(userDateOfBirth, "w 60%");

        CustomTextField userPassword = new CustomTextField();
        userPassword.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/contraseña.png")));
        userPassword.setHintText("Contraseña");
        this.register.add(userPassword, "w 60%");

        CustomizedButton registerButton = new CustomizedButton();
        registerButton.setBackground(MAIN_APP_COLOUR); // TODO: Colours?
        registerButton.setForeground(Color.red); // TODO: Font?
        registerButton.setFont(MAIN_FONT);
        registerButton.addActionListener(eventRegister);
        // TODO: setText vs setLabel?
        registerButton.setLabel("Registrarse"); // TODO: How to make it round
        this.register.add(registerButton, "w 40%, h 40");
        // TODO: Refactor this
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String dni = userDNI.getText().trim();
                String username = userName.getText().trim();
                String email = userEmail.getText().trim();
                String phoneNumber = userPhoneNumber.getText().trim();
                String dateOfBirth = userDateOfBirth.getText().trim();
                String password = userPassword.getText().trim();

                // TODO: Do this properly please ty. Validate all data before inserting
                var formattedDate = AuthenticationValidator.validateAndParseDate(dateOfBirth);
                System.out.println("Formatted date is: " + formattedDate);
                patient = new Patient(dni, username, "Test Apellido", phoneNumber, email, formattedDate, 50, password, 50);
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
}
