package UI.LandingPage;

import Components.CustomizedButton;
import Components.NotificationPopUp;
import UI.Authentication.Authenticator;
import Components.DropDownMenu;
import Components.GradientTextLabel;
import MainApplication.NavigationController;
import Utils.Utility.ImageIconRedrawer;
import Utils.Utility.PhoneDialer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

import static Utils.Swing.Colors.*;
import static Utils.Swing.Fonts.MAIN_FONT;
import static Utils.Swing.Fonts.PANEL_TITLE_FONT;

public class HeaderPanel extends JPanel {
    private JFrame mainFrame;

    private final NavigationController navigationController;
    private DropDownMenu dropDownMenu;

    private JLabel
            appLogo, welcomeLabel,
            menuIcon,
            plusLabel, subtitleLabel;
    private GradientTextLabel titleLabel;
    private CustomizedButton
            registerButton, loginButton, logoutButton,
            appointmentButton, callButton,
            sendMessageButton;
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
        initWelcomeLabel();
        initButtons();
        initMenuIcon();
        initSeparator();
        initTitles();
    }

    // Initializes the application logo
    private void initLogo() {
        appLogo = new JLabel(createIcon("/General/app-logo.png", 80, 80)); // Adjusted size
    }

    // Welcome Label displaying the user's name if they are logged in
    private void initWelcomeLabel() {
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 30));
        welcomeLabel.setForeground(SUBTITLE_COLOUR);
    }

    // Initializes all buttons
    private void initButtons() {
        int buttonWidth = 120; // TODO: Sizes?
        int buttonHeight = 40;

        registerButton = createButton("Registrarse", MAIN_APP_COLOUR, MAIN_FONT, Color.WHITE, MAIN_APP_COLOUR);
        registerButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        registerButton.addActionListener(e -> initiateRegister());

        loginButton = createButton("Iniciar Sesión", SECONDARY_APP_COLOUR, MAIN_FONT, Color.WHITE, SECONDARY_APP_COLOUR);
        loginButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        loginButton.addActionListener(e -> initiateLogin());

        logoutButton = createButton("Cerrar Sesión", MY_RED, MAIN_FONT, Color.WHITE, MY_RED);
        logoutButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        logoutButton.addActionListener(e -> handleLogout());

        appointmentButton = createButton("Pedir Cita", MY_RED, MAIN_FONT, Color.WHITE, MY_RED);
        appointmentButton.setIcon(createIcon("/LandingPage/calendar-appointment.png", 35, 35));
        appointmentButton.setPreferredSize(new Dimension(160, 50));
        appointmentButton.addActionListener(e -> navigationController.appointmentRedirect());

        callButton = createButton("¡Llámenos!", SECONDARY_APP_COLOUR, MAIN_FONT, Color.WHITE, SECONDARY_APP_COLOUR.darker());
        callButton.setIcon(createIcon("/LandingPage/phone-call.png", 35, 35));
        callButton.setPreferredSize(new Dimension(160, 50));
        callButton.addActionListener(e -> initiateCall());

        // TODO: Check this style
        sendMessageButton = createButton("Mensajes", SECONDARY_APP_COLOUR, MAIN_FONT, Color.WHITE, SECONDARY_APP_COLOUR.darker());
        sendMessageButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        sendMessageButton.addActionListener(e -> openMessageDialog());
    }

    // Deletes user cache and refreshes the UI (displays back the login/register buttons)
    private void handleLogout() {
        navigationController.logoutUser();
        NotificationPopUp.showInfoMessage(
                this,
                "Sesión cerrada",
                "Sesión cerrada con éxito.\n\n" +
                        "¡Nos vemos! Esperamos verle pronto."
        );
        updateUIComponents();
    }

    // Updates the UI components based on login status
    private void updateUIComponents() {
        if (navigationController.isUserLoggedIn()) {
            welcomeLabel.setText("Hola, " + navigationController.getLoggedInPatient().getFirstName() + "!");

            // Show welcome message and logout button, hide login/register buttons
            welcomeLabel.setVisible(true);
            logoutButton.setVisible(true);
            registerButton.setVisible(false);
            loginButton.setVisible(false);
            sendMessageButton.setVisible(true);
        } else {
            welcomeLabel.setText(""); // Clear welcome message

            // Show login/register buttons, hide logout button and welcome message
            welcomeLabel.setVisible(false);
            logoutButton.setVisible(false);
            registerButton.setVisible(true);
            loginButton.setVisible(true);
            sendMessageButton.setVisible(false);
        }
        revalidate();
        repaint();
    }

    // Initializes the dropdown menu icon with an appropriate size
    private void initMenuIcon() {
        int iconSize = 40; // Slightly reduced size
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
    // Opens the authentication frame with the specified mode (Login or Register)
    private void openAuthenticationFrame(boolean startInRegister) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(startInRegister ? "Inicio de Sesión" : "Registro de Usuario");
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(this);

            Authenticator authenticator = new Authenticator(patient -> {
                navigationController.loginUser(patient);
                updateUIComponents();
                frame.dispose();
            }, startInRegister);

            frame.add(authenticator);
            frame.setVisible(true);
        });
    }

    // Open the frame in login mode
    private void initiateLogin() {
        openAuthenticationFrame(false);
    }

    // Open the frame in register mode
    private void initiateRegister() {
        openAuthenticationFrame(true);
    }

    // Call
    private void initiateCall() {
        String formattedPhoneNumber = PhoneDialer.obtainFormattedPhoneNumber(mainPhoneNumber);
        PhoneDialer.makeCall(formattedPhoneNumber, this);
    }

    // Arranges all components in the appropriate layout
    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Top panel with background color
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(true);
        topPanel.setBackground(new Color(230, 240, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20)); // Padding for spacing

        // Logo & Welcome Panel (Left Side)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        logoPanel.add(appLogo);
        logoPanel.add(Box.createHorizontalStrut(10)); // Space between logo and text
        logoPanel.add(welcomeLabel);

        // Right Panel (Buttons and Dropdown)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;  // All items in the same row
        gbc.insets = new Insets(5, 10, 5, 10); // Padding between elements

        gbc.gridx = 0;
        rightPanel.add(loginButton, gbc);

        gbc.gridx++;
        gbc.insets = new Insets(5, 10, 5, 30); // Increase right padding for space
        rightPanel.add(registerButton, gbc);

        gbc.gridx++;
        rightPanel.add(logoutButton, gbc); // Initially hidden

        gbc.gridx++;
        rightPanel.add(sendMessageButton, gbc); // Also initially hidden

        gbc.gridx++;
        gbc.insets = new Insets(5, 30, 5, 10); // More space before the dropdown menu
        rightPanel.add(menuIcon, gbc);


        // Add components to top panel
        topPanel.add(logoPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.insets = new Insets(5, 0, 5, 0);
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;

        // Title Section
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        titlePanel.add(titleLabel);
        titlePanel.add(plusLabel);

        centerPanel.add(titlePanel, gbcCenter);
        gbcCenter.gridy = 1;
        centerPanel.add(subtitleLabel, gbcCenter);
        gbcCenter.gridy = 2;
        centerPanel.add(separator, gbcCenter);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Buttons (Appointment and Call)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setOpaque(false);
        bottomPanel.add(appointmentButton);
        bottomPanel.add(callButton);

        add(bottomPanel, BorderLayout.SOUTH);

        updateUIComponents();
    }

    private void openMessageDialog() {
        JFrame messageFrame = new JFrame("Mensajes");
        messageFrame.setSize(800, 600);
        messageFrame.setLocationRelativeTo(this);
        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String patientDNI = navigationController.getLoggedInPatient().getDNI();
        messageFrame.add(new MessagePanel(patientDNI));

        messageFrame.setVisible(true);
    }
}
