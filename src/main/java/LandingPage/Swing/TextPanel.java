package LandingPage.Swing;

import Authentication.Components.CustomizedButton;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class TextPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel bodyTextLabel;
    private CustomizedButton appointmentButton;
    private CustomizedButton cancelAppointmentButton;
    private JPanel buttonPanel;

    public TextPanel() {
        // Set panel background and layout
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents();
        layoutComponents();
    }

    /**
     * Initializes all UI components in the panel.
     */
    private void initializeComponents() {
        initLabels();
        initButtonPanel();
    }

    /**
     * Initializes the title and body text labels.
     */
    private void initLabels() {
        // Title label configuration
        titleLabel = new JLabel("Quiénes somos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Body text label configuration
        bodyTextLabel = new JLabel("Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum");
        bodyTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bodyTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Initializes the button panel and creates the appointment and cancel buttons.
     */
    private void initButtonPanel() {
        // Create panel with centered FlowLayout
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // Create appointment button
        appointmentButton = createCustomButton("Pedir Cita", "/LandingPage/calendar-appointment.png", MAIN_APP_COLOUR, Color.WHITE);

        // Create cancel appointment button with a brighter background
        cancelAppointmentButton = createCustomButton("Anular Cita", "/LandingPage/cancel-white.png", MAIN_APP_COLOUR.brighter(), Color.WHITE);

        // Add a common action listener for demonstration purposes
        ActionListener buttonAction = e -> System.out.println(e.getActionCommand() + " clicked!");
        appointmentButton.addActionListener(buttonAction);
        cancelAppointmentButton.addActionListener(buttonAction);

        // Add buttons to the button panel
        buttonPanel.add(appointmentButton);
        buttonPanel.add(cancelAppointmentButton);
    }

    /**
     * Helper method to create a customized button with an icon.
     *
     * @param text            Button text.
     * @param iconResource    Path to the icon resource.
     * @param backgroundColor Background color for the button.
     * @param textColor       Text color for the button.
     * @return Configured CustomizedButton.
     */
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

    /**
     * Adds the components to the panel with appropriate spacing.
     */
    private void layoutComponents() {
        // Add title label with spacing
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Add body text label with spacing
        add(bodyTextLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Add the button panel
        add(buttonPanel);
    }
}
