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
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initElements();
        addElementsToPanel();
    }

    private void initElements() {
        ImageIconRedrawer iconRedrawer = new ImageIconRedrawer();

        titleLabel = new JLabel("Quiénes somos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bodyTextLabel = new JLabel("Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum Lore ipsum");
        bodyTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bodyTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/calendar-appointment.png")));
        ImageIcon appointmentIcon = iconRedrawer.redrawImageIcon(30, 30);

        appointmentButton = new CustomizedButton();
        appointmentButton.setIcon(appointmentIcon);
        appointmentButton.setBackground(MAIN_APP_COLOUR);
        appointmentButton.setForeground(Color.WHITE);
        appointmentButton.setFocusPainted(false);
        appointmentButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        appointmentButton.setText("Pedir Cita");

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/cancel.png")));
        ImageIcon cancelIcon = iconRedrawer.redrawImageIcon(30, 30);

        cancelAppointmentButton = new CustomizedButton();
        cancelAppointmentButton.setIcon(cancelIcon);
        cancelAppointmentButton.setBackground(MAIN_APP_COLOUR.brighter());
        cancelAppointmentButton.setForeground(Color.WHITE);
        cancelAppointmentButton.setFocusPainted(false);
        cancelAppointmentButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cancelAppointmentButton.setText("Anular Cita");

        // TODO: Methods here
        ActionListener buttonAction = e -> System.out.println(e.getActionCommand() + " clickeado!");
        appointmentButton.addActionListener(buttonAction);
        cancelAppointmentButton.addActionListener(buttonAction);
    }

    private void addElementsToPanel() {
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(bodyTextLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        buttonPanel.add(appointmentButton);
        buttonPanel.add(cancelAppointmentButton);

        add(buttonPanel);
    }
}
