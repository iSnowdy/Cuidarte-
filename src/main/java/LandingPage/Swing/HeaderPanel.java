package LandingPage.Swing;

import Authentication.Components.CustomizedButton;
import Utils.Utility.ImageIconRedrawer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

import static Utils.Swing.Colors.*;
import static Utils.Swing.Fonts.MAIN_FONT;

public class HeaderPanel extends JPanel {
    private JLabel appLogo;
    private CustomizedButton registerButton;
    private CustomizedButton loginButton;
    private JLabel menuIcon;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private CustomizedButton appointmentButton;
    private CustomizedButton callButton;
    private JSeparator separator;

    private ImageIconRedrawer iconRedrawer;

    public HeaderPanel() {
        setOpaque(false);
        initComponents();
        addComponentsToLayout();
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap, insets 10", "[center]", "[]10[]10[]10[]"));
        iconRedrawer = new ImageIconRedrawer();
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/General/Logo-Temp.png")));
        ImageIcon logoIcon = iconRedrawer.redrawImageIcon(50, 50);

        appLogo = new JLabel(logoIcon);

        registerButton = new CustomizedButton();
        registerButton.setBackground(MAIN_APP_COLOUR);
        registerButton.setFont(MAIN_FONT);
        registerButton.setForeground(Color.WHITE);
        registerButton.addHoverEffectToButton(MAIN_APP_COLOUR);
        registerButton.setText("Registrarse");

        loginButton = new CustomizedButton();
        loginButton.setBackground(SECONDARY_APP_COLOUR);
        loginButton.setFont(MAIN_FONT);
        loginButton.setForeground(Color.WHITE);
        loginButton.addHoverEffectToButton(SECONDARY_APP_COLOUR);
        loginButton.setText("Iniciar Sesión");



        // Dropdown Menu (future implementation)
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/dropdown-menu.png")));
        ImageIcon dropDownIcon = iconRedrawer.redrawImageIcon(20, 20);
        menuIcon = new JLabel(dropDownIcon);
        menuIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // TODO: Future implementation
                System.out.println("Menú desplegable clicado");
            }
        });

        // Separator line
        separator = new JSeparator();
        separator.setForeground(Color.GRAY);
        separator.setPreferredSize(new Dimension(500, 2));


        titleLabel = new JLabel("Cuidarte+");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // TODO: Font

        subtitleLabel = new JLabel("Tu salud, nuestra prioridad");
        subtitleLabel.setForeground(SUBTITLE_COLOUR);

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/calendar-appointment.png")));
        ImageIcon appointmentIcon = iconRedrawer.redrawImageIcon(50, 50);
        appointmentButton = new CustomizedButton();
        appointmentButton.setBackground(MY_RED);
        appointmentButton.setFont(MAIN_FONT);
        appointmentButton.setForeground(Color.WHITE);
        appointmentButton.addHoverEffectToButton(MY_RED.darker());
        appointmentButton.setIcon(appointmentIcon);
        appointmentButton.setText("Pedir Cita");

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/phone-call.png")));
        ImageIcon callIcon = iconRedrawer.redrawImageIcon(50, 50);
        callButton = new CustomizedButton();
        callButton.setBackground(SECONDARY_APP_COLOUR);
        callButton.setFont(MAIN_FONT);
        callButton.setForeground(Color.WHITE);
        callButton.addHoverEffectToButton(SECONDARY_APP_COLOUR.darker());
        callButton.setIcon(callIcon);
        callButton.setText("+34 800 500 220");
    }

    private void addComponentsToLayout() {
        JPanel headerPanel = new JPanel(new MigLayout("insets 0, fillx", "[left]push[right]10[right]10[right]", "[]"));
        headerPanel.setOpaque(false);
        headerPanel.add(appLogo);
        headerPanel.add(loginButton);
        headerPanel.add(registerButton);
        headerPanel.add(menuIcon);

        add(headerPanel, "growx, wrap");

        add(separator, "center, wrap");

        add(titleLabel, "center, wrap");
        add(subtitleLabel, "center, wrap");

        JPanel buttonPanel = new JPanel(new MigLayout("fillx, insets 0", "[center]10[center]", "[]"));
        buttonPanel.setOpaque(false);
        buttonPanel.add(appointmentButton);
        buttonPanel.add(callButton);

        add(buttonPanel, "center, wrap");
    }
}
