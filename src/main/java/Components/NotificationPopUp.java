package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NotificationPopUp {

    // Shows an information message
    public static void showInfoMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Shows a warning message
    public static void showWarningMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    // Shows an error message
    public static void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    // Shows a confirmation message with Yes/No options
    public static boolean showConfirmationMessage(Component parent, String title, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    // Shows a custom notification with a button and an optional action
    public static void showCustomNotification(Component parent, String title, String message, String buttonText, ActionListener actionListener) {
        Object[] options = {buttonText};

        int selection = JOptionPane.showOptionDialog(
                parent,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, // Default icon
                options,
                options[0]
        );

        if (selection == 0 && actionListener != null) {
            actionListener.actionPerformed(null);
        }
    }

    // Shows a notification with a custom icon
    public static void showCustomIconNotification(Component parent, String title, String message, String buttonText, String iconPath, ActionListener actionListener) {
        ImageIcon icon = new ImageIcon(iconPath);
        Object[] options = {buttonText};

        int selection = JOptionPane.showOptionDialog(
                parent,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                icon, // Custom icon
                options,
                options[0]
        );

        if (selection == 0 && actionListener != null) {
            actionListener.actionPerformed(null);
        }
    }
}
