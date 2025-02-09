package LandingPage.Components;

import Authentication.Components.CustomizedButton;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

// Consider passing as arguments to the constructor / a method the text inside
// the notification, the button text and what action should it do?

// Also. Where do I even use this?

public class NotificationPopUp extends JDialog {
    private JPanel contentPanel;

    private JLabel
            titleLabel, iconLabel, closeLabel;

    private CustomizedButton actionButton;

    public NotificationPopUp(JFrame parent, String title, String notificationMessage, String buttonText, ActionListener actionListener) {
        super(parent, true);
        setLayout(new BorderLayout());
        setUndecorated(true);
        setSize(300, 150); // TODO: Check this
        setLocationRelativeTo(parent);

        initComponents(title, notificationMessage, buttonText, actionListener);
        addComponentsToLayout();
    }

    private void initComponents(String title, String notificationMessage, String buttonText, ActionListener actionListener) {
        this.contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // TODO: Consider using this for buttons?
        contentPanel.setBackground(Color.WHITE);

        // Header elements (icon, text, close)
        ImageIconRedrawer iconRedrawer = new ImageIconRedrawer();
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/info.png")));
        ImageIcon infoIcon = iconRedrawer.redrawImageIcon(20, 20);
        this.iconLabel = new JLabel(infoIcon);

        this.titleLabel = new JLabel(title);
        titleLabel.setFont(MAIN_FONT); // TODO: Stylize this

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/cancel-black.png")));
        ImageIcon closeIcon = iconRedrawer.redrawImageIcon(20, 20);
        this.closeLabel = new JLabel(closeIcon);
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Close the notification upon clicking the "X"
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                dispose();
            }
        });

        // Body elements (text)
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(Color.WHITE);
        messagePanel.add(new JLabel(notificationMessage)); // TODO: Style it?

        // Bottom elements (button)
        JPanel buttonPanel = new JPanel();
        actionButton = new CustomizedButton();
        actionButton.setBackground(MAIN_APP_COLOUR);
        actionButton.setForeground(Color.WHITE);
        actionButton.setFocusPainted(false); // Deletes that border when using keyboard
        actionButton.setText(buttonText);
        actionButton.addActionListener(actionListener); // To make it even more flexible

        // Consider also adding a cancel button or reject
        buttonPanel.add(actionButton);

        contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        contentPanel.add(messagePanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(closeLabel, BorderLayout.EAST);
        headerPanel.add(this.titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private void addComponentsToLayout() {
        add(contentPanel);
    }

    // Static method to use this class anywhere in the project
    public static void invokeNotification(JFrame parent, String title, String notificationMessage, String buttonText, ActionListener actionListener) {
        NotificationPopUp newPopUp = new NotificationPopUp(parent, title, notificationMessage, buttonText, actionListener);
        newPopUp.setVisible(true);
    }


}
