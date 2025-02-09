package LandingPage.Swing;

import Authentication.Components.CustomizedButton;
import LandingPage.Components.DropDownMenu;
import LandingPage.Components.NotificationPopUp;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        setBackground(Color.WHITE);
        setOpaque(true);
        initComponents();
        addComponentsToLayout();
    }

    private void initComponents() {
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
                DropDownMenu dropDownMenu = new DropDownMenu();
                dropDownMenu.showMenu(menuIcon);
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

        // Example on how to use the Notification Pop Up class
        appointmentButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                NotificationPopUp.invokeNotification(
                        (JFrame) window, // Casting Window to JFrame
                        "Cita médica", "Tu cita ha sido agendada.", "Aceptar",
                        evt -> System.out.println("Notificación cerrada"));
            } else {
                System.err.println("Error: No se pudo obtener el JFrame principal.");
            }
        });


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
        setLayout(new BorderLayout(0, 0));

        // Top panel with centered layout
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10); // Horizontal spacing between components
        gbc.anchor = GridBagConstraints.CENTER; // Center components vertically and horizontally

        // Add logo to the left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5; // Push logo to the left
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(appLogo, gbc);

        // Add buttons and dropdown menu to the right
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Horizontal spacing
        rightPanel.setOpaque(false);
        rightPanel.add(loginButton);
        rightPanel.add(registerButton);
        rightPanel.add(menuIcon);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5; // Push buttons to the right
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(rightPanel, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Separator
        JPanel separatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        separatorPanel.setOpaque(false);
        separatorPanel.add(separator);
        add(separatorPanel, BorderLayout.CENTER);

        // Center panel containing the text
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Vertical spacing between components
        centerPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        centerPanel.add(subtitleLabel, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel containing the other two buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50)); // Horizontal spacing
        bottomPanel.setOpaque(false);
        bottomPanel.add(appointmentButton);
        bottomPanel.add(callButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
