package Utils.Utility;

import Components.NotificationPopUp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleMapsRedirect {
    public static void openGoogleMaps(String address, Component parentComponent) {
        try {
            // Encode the address to URL format so we can Google Search it
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String googleMapsUrl = "https://www.google.com/maps/search/?api=1&query=" + encodedAddress;

            Desktop.getDesktop().browse(new URI(googleMapsUrl));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showErrorNotification(Component parentComponent) {
        Window window = SwingUtilities.getWindowAncestor(parentComponent);
        if (window instanceof JFrame) {
            NotificationPopUp.showCustomNotification(
                    (JFrame) window,
                    "Error al abrir Google Maps",
                    "No se pudo abrir Google Maps.",
                    "Aceptar",
                    event -> System.out.println("Notificación cerrada")
            );
        } else {
            System.err.println("No se pudo obtener el componente padre");
        }
    }
}
