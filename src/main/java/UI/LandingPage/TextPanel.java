package UI.LandingPage;

import Components.CustomizedButton;
import MainApplication.NavigationController;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;
import static Utils.Swing.Fonts.PANEL_TITLE_FONT;

public class TextPanel extends JPanel {
    private final NavigationController navigationController;
    private JLabel titleLabel;
    private JTextArea bodyTextArea;
    private CustomizedButton appointmentButton;
    private CustomizedButton cancelAppointmentButton;
    private JPanel buttonPanel;

    public TextPanel(NavigationController navigationController) {
        this.navigationController = navigationController;

        // Set fixed width for content (around 70% of standard width)
        setPreferredSize(new Dimension(700, 400));
        setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

        // Set panel background and layout
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        initializeComponents();
        layoutComponents();
    }

    // Initializes all UI components
    private void initializeComponents() {
        initLabels();
        initButtonPanel();
    }

    // Initializes the title and text area
    private void initLabels() {
        // Title label configuration
        titleLabel = new JLabel("Quiénes somos");
        titleLabel.setFont(PANEL_TITLE_FONT);
        titleLabel.setForeground(MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Body text configuration using JTextArea for automatic line wrapping
        bodyTextArea = new JTextArea(mainText);
        bodyTextArea.setFont(MAIN_FONT);
        bodyTextArea.setWrapStyleWord(true);
        bodyTextArea.setLineWrap(true);
        bodyTextArea.setEditable(false);
        bodyTextArea.setOpaque(false);
        bodyTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ScrollPane to contain text
        JScrollPane textScrollPane = new JScrollPane(bodyTextArea);
        textScrollPane.setBorder(BorderFactory.createEmptyBorder());
        textScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textScrollPane.setPreferredSize(new Dimension(700, 250));
        textScrollPane.setMaximumSize(new Dimension(700, 300));
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    // Initializes the button panel
    private void initButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // Create appointment button
        appointmentButton = createCustomButton("Pedir Cita", "/LandingPage/calendar-appointment.png", MAIN_APP_COLOUR, Color.WHITE);
        appointmentButton.addActionListener(e -> this.navigationController.appointmentRedirect());

        // Create cancel appointment button
        cancelAppointmentButton = createCustomButton("Anular Cita", "/LandingPage/cancel-white.png", MAIN_APP_COLOUR.brighter(), Color.WHITE);
        cancelAppointmentButton.addActionListener(e -> this.navigationController.appointmentRedirect());

        // Add buttons to the panel
        buttonPanel.add(appointmentButton);
        buttonPanel.add(cancelAppointmentButton);
    }

    // Creates a custom button with an icon
    private CustomizedButton createCustomButton(String text, String iconResource, Color backgroundColor, Color textColor) {
        ImageIconRedrawer iconRedrawer = new ImageIconRedrawer();
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconResource)));
        ImageIcon icon = iconRedrawer.redrawImageIcon(30, 30);

        CustomizedButton button = new CustomizedButton();
        button.setIcon(icon);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setText(text);
        return button;
    }

    // Adds the components to the panel with appropriate spacing
    private void layoutComponents() {
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10))); // Space between title and text
        add(bodyTextArea);
        add(Box.createRigidArea(new Dimension(0, 20))); // Space between text and buttons
        add(buttonPanel);
    }

    private final String mainText =
            "Bienvenido a Cuidarte+\n\n" +
                    "En Cuidarte+, tu salud es nuestra prioridad. Contamos con una amplia red de clínicas equipadas " +
                    "con la más avanzada tecnología y un equipo de profesionales altamente cualificados en diversas " +
                    "especialidades médicas. Nuestro compromiso es brindarte una atención integral, personalizada y de " +
                    "calidad, asegurando el bienestar de cada paciente con un enfoque humano y cercano.\n\n" +
                    "Desde consultas generales hasta tratamientos especializados, en Cuidarte+ nos esforzamos por " +
                    "ofrecerte un servicio eficiente, cómodo y accesible. Nuestras instalaciones están diseñadas para " +
                    "garantizar tu confort, y nuestro equipo médico trabaja con dedicación para proporcionarte " +
                    "diagnósticos precisos y soluciones adaptadas a tus necesidades.\n\n" +
                    "Confía en nosotros para cuidar de ti y de los tuyos. En Cuidarte+, estamos aquí para acompañarte " +
                    "en cada paso hacia una vida más saludable.";
}
