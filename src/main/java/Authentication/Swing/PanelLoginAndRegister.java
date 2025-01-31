package Authentication.Swing;

import Authentication.Components.CustomTextField;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

import static Utils.Colors.MAIN_APP_COLOUR;
import static Utils.Fonts.MAIN_FONT;

public class PanelLoginAndRegister extends JLayeredPane {
    private JPanel login, register;

    public PanelLoginAndRegister() {
        setOpaque(false);
        setLayout(new CardLayout());
        initComponents();
        initRegister();
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

    private void initRegister() {
        this.register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]push"));
        JLabel registerLabel = new JLabel("Registrarse");
        registerLabel.setFont(MAIN_FONT); // TODO: FOnt
        registerLabel.setForeground(MAIN_APP_COLOUR);
        this.register.add(registerLabel);

        // TODO: Data Validation. Data Input to DB. Hash password. Use another thing for password and email?
        // TODO: Refactor this? Too many blocks of code very similar to each other
        CustomTextField userName = new CustomTextField();
        userName.setPrefixIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/usuario.png")));
        userName.setHintText("Nombre completo");
        this.register.add(userName, "w 60%");

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
    }

    private void initLogin() {
        this.login.setLayout(new MigLayout("wrap", "push[center]push", "push[]push"));
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

        JButton buttonLoger = new JButton("Login");
        this.login.add(buttonLoger);
    }
}
