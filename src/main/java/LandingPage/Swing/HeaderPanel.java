package LandingPage.Swing;

import Authentication.Components.CustomizedButton;
import Authentication.Main.Authenticator;
import LandingPage.Components.DropDownMenu;
import LandingPage.Components.GradientTextLabel;
import MainApplication.NavigationController;
import Utils.Utility.ImageIconRedrawer;
import Utils.Utility.PhoneDialer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

import static Utils.Swing.Colors.*;
import static Utils.Swing.Fonts.MAIN_FONT;

public class HeaderPanel extends JPanel {
    private JFrame mainFrame;

    private final NavigationController navigationController;
    private DropDownMenu dropDownMenu;

    private JLabel appLogo;
    private CustomizedButton registerButton;
    private CustomizedButton loginButton;
    private JLabel menuIcon;
    private GradientTextLabel titleLabel;
    private JLabel plusLabel;
    private JLabel subtitleLabel;
    private CustomizedButton appointmentButton;
    private CustomizedButton callButton;
    private JSeparator separator;

    private final String mainPhoneNumber = "+34 800 500 220";

    public HeaderPanel(JFrame mainFrame, NavigationController navigationController) {
        this.mainFrame = mainFrame;
        this.navigationController = navigationController;
        this.dropDownMenu = new DropDownMenu(navigationController);

        setBackground(Color.WHITE); // Background for the entire header
        setOpaque(true);
        setLayout(new BorderLayout());
        initComponents();
        layoutComponents();
    }

    // Initialize all UI components
    private void initComponents() {
        initLogo();
        initButtons();
        initMenuIcon();
        initSeparator();
        initTitles();
    }

    // Initializes the application logo
    private void initLogo() {
        appLogo = new JLabel(createIcon("/General/app-logo.png", 70, 70)); // Adjusted size
    }

    // Initializes all buttons
    private void initButtons() {
        int buttonWidth = 120; // TODO: Sizes?
        int buttonHeight = 40;

        registerButton = createButton("Registrarse", MAIN_APP_COLOUR, MAIN_FONT, Color.WHITE, MAIN_APP_COLOUR);
        registerButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        loginButton = createButton("Iniciar Sesión", SECONDARY_APP_COLOUR, MAIN_FONT, Color.WHITE, SECONDARY_APP_COLOUR);
        loginButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        loginButton.addActionListener(e -> initiateLogin());

        appointmentButton = createButton("Pedir Cita", MY_RED, MAIN_FONT, Color.WHITE, MY_RED.darker());
        appointmentButton.setIcon(createIcon("/LandingPage/calendar-appointment.png", 35, 35));
        appointmentButton.setPreferredSize(new Dimension(160, 50));
        appointmentButton.addActionListener(e -> navigationController.appointmentRedirect());

        callButton = createButton(mainPhoneNumber, SECONDARY_APP_COLOUR, MAIN_FONT, Color.WHITE, SECONDARY_APP_COLOUR.darker());
        callButton.setIcon(createIcon("/LandingPage/phone-call.png", 35, 35));
        callButton.setPreferredSize(new Dimension(160, 50));
        callButton.addActionListener(e -> initiateCall());
    }

    // Initializes the dropdown menu icon with an appropriate size
    private void initMenuIcon() {
        int iconSize = 30; // Slightly reduced size
        menuIcon = new JLabel(createIcon("/LandingPage/dropdown-menu.png", iconSize, iconSize));
        menuIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        menuIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dropDownMenu.showMenu(menuIcon);
            }
        });
    }

    // Initializes the separator below the title
    private void initSeparator() {
        separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        separator.setPreferredSize(new Dimension(350, 2)); // Separator width
    }

    // Initializes the title and subtitle labels with proper styling
    private void initTitles() {
        titleLabel = new GradientTextLabel("Cuidarte", SECONDARY_APP_COLOUR, MAIN_APP_COLOUR);
        //titleLabel.setFont(new Font("Arial", Font.BOLD, 26)); // Size
        plusLabel = new JLabel("+");
        plusLabel.setFont(new Font("Arial", Font.BOLD, 46));
        plusLabel.setForeground(Color.RED);

        subtitleLabel = new JLabel("Tu salud, nuestra prioridad");
        subtitleLabel.setForeground(SUBTITLE_COLOUR);
        subtitleLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 17));
    }

    // Helper method to create an icon with a specific size
    private ImageIcon createIcon(String resourcePath, int width, int height) {
        ImageIconRedrawer redrawer = new ImageIconRedrawer();
        redrawer.setImageIcon(new ImageIcon(getClass().getResource(resourcePath)));
        return redrawer.redrawImageIcon(width, height);
    }

    // Helper method to create a customized button with hover effect
    private CustomizedButton createButton(String text, Color background, Font font, Color textColor, Color hoverColor) {
        CustomizedButton button = new CustomizedButton();
        button.setText(text);
        button.setBackground(background);
        button.setFont(font);
        button.setForeground(textColor);
        button.addHoverEffectToButton(hoverColor);
        return button;
    }

    // Login and Register onClicks
    // The CallBack on Authenticator will notify us when the Patient has successfully logged in
    // Upon the notification, the patient data will be passed down to the NavigationController
    // and the frame will be closed
    private void initiateLogin() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inicio de Sesión");
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(this);

            Authenticator authenticator = new Authenticator(patient -> {
                navigationController.loginUser(patient);

                frame.dispose();
            });

            frame.add(authenticator);
            frame.setVisible(true);
        });
    }

    private void initiateRegister() {

    }

    // Call
    private void initiateCall() {
        String formattedPhoneNumber = PhoneDialer.obtainFormattedPhoneNumber(mainPhoneNumber);
        PhoneDialer.makeCall(formattedPhoneNumber, this);
    }

    // Arranges all components in the appropriate layout
    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel with a different background color
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(230, 240, 255)); // TODO: Contrast?
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Paddings

        // Panel for buttons and menu icon, aligned to the right
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0)); // Spacing
        rightPanel.setOpaque(false);
        rightPanel.add(loginButton);
        rightPanel.add(registerButton);
        rightPanel.add(menuIcon);

        topPanel.add(appLogo, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center panel containing title and subtitle
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // Spacing
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Panel to hold "Cuidarte" and "+"
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Ensures small gap between text and "+"
        titlePanel.add(titleLabel);
        titlePanel.add(plusLabel);

        centerPanel.add(titlePanel, gbc);

        gbc.gridy = 1;
        centerPanel.add(subtitleLabel, gbc);
        gbc.gridy = 2;
        centerPanel.add(separator, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with the appointment and call buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setOpaque(false);
        bottomPanel.add(appointmentButton);
        bottomPanel.add(callButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
