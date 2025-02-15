package Utils.Utility;

import LandingPage.Components.NotificationPopUp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PhoneDialer {
    public static void makeCall(String phoneNumber, Component parentComponent) {
        String operativeSystem = System.getProperty("os.name").toLowerCase();

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                // Some OS are compatible with URIs such as tel
                Desktop.getDesktop().browse(new URI("tel: " + phoneNumber));
            } else if (operativeSystem.contains("win")) {
                // Windows use Skype
                Runtime.getRuntime().exec("cmd.exe /c start skype:" + phoneNumber);
            } else if (operativeSystem.contains("mac")) {
                Runtime.getRuntime().exec("open tel" + phoneNumber);
            } else if (operativeSystem.contains("linux")) {
                Runtime.getRuntime().exec("xdg-open tel:" + phoneNumber);
            } else {
                System.err.println("No se pudo abrir la aplicación de llamadas en este sistema");
                showErrorNotification(parentComponent);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error al intentar llamar al número: " + phoneNumber);
        }
    }

    private static void showErrorNotification(Component parentComponent) {
        Window window = SwingUtilities.getWindowAncestor(parentComponent);
        if (window instanceof JFrame) {
            NotificationPopUp.invokeNotification(
                    (JFrame) window,
                    "Error en la llamada",
                    "No se pudo abrir la aplicación de llamadas.",
                    "Aceptar",
                    event -> System.out.println("Notificación cerrada")
            );
        } else {
            System.err.println("No se pudo obtener el componente padre");
        }
    }

    public static String obtainFormattedPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^+0-9]", "");
    }
}
