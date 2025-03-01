package UI.Authentication;

import Components.CustomPasswordField;
import Components.CustomizedButton;
import javax.swing.*;
import java.awt.*;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Colors.MY_RED;
import static Utils.Swing.Fonts.PATIENT_PORTAL_SUB_PANEL_FONT;
import static Utils.Swing.Fonts.MAIN_FONT;

public class PasswordVerificationDialog {

    public static String showPasswordInputDialog(Component parent) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Verificación de contraseña", true);
        dialog.setSize(450, 250);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(parent);

        // Shadowy panel behind the main one
        // Sombra simulada con un panel de fondo
        JPanel shadowPanel = new JPanel();
        shadowPanel.setBackground(new Color(0, 0, 0, 50));
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shadowPanel.setLayout(new BorderLayout());

        // Main panel with white background but gray borders containing the elements
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title label
        JLabel titleLabel = new JLabel("Verificación de contraseña");
        titleLabel.setFont(PATIENT_PORTAL_SUB_PANEL_FONT.deriveFont(Font.BOLD, 20));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Instruction label
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("Ingrese la nueva contraseña enviada a su correo:");
        instructionLabel.setFont(MAIN_FONT);
        instructionLabel.setForeground(Color.DARK_GRAY);
        gbc.insets = new Insets(20, 10, 5, 10); // Padding between elements
        mainPanel.add(instructionLabel, gbc);

        // Password field
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 15, 10);
        CustomPasswordField passwordField = new CustomPasswordField();
        passwordField.setHintText("Nueva contraseña");
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        // Confirm Button
        CustomizedButton confirmButton = new CustomizedButton();
        confirmButton.setText("Confirmar");
        confirmButton.setBackground(MAIN_APP_COLOUR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(MAIN_FONT);
        confirmButton.addHoverEffectToButton(MAIN_APP_COLOUR.darker());

        // Cancel Button (Red)
        CustomizedButton cancelButton = new CustomizedButton();
        cancelButton.setText("Cancelar");
        cancelButton.setBackground(MY_RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(MAIN_FONT);
        cancelButton.addHoverEffectToButton(MY_RED.darker());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // Adding buttons
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);

        // Adds the main panel containing all elements to the shadow one
        shadowPanel.add(mainPanel, BorderLayout.CENTER);
        dialog.add(shadowPanel, BorderLayout.CENTER);

        // Button Actions
        confirmButton.addActionListener(e -> {
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.setVisible(true);

        return passwordField.getText().trim();
    }
}
