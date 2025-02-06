package Tests;

import java.util.Properties;

public class Tests {
    public static void main(String[] args) {
        System.out.println("Email username: " + System.getenv("TEST_EMAIL_USERNAME"));
        System.out.println("Email password: " + System.getenv("TEST_EMAIL_PASSWORD"));
        System.out.println("MySQL username: " + System.getenv("MySQL_Username"));
        System.out.println("MySQL password: " + System.getenv("MySQL_Password"));


        String emailSubject = String.format(
                "Correo de verificación para el Paciente  - Cuidarte+"
        );

        String emailBody = String.format(
                """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <p>Buenas,</p>

                    <p>Le queremos dar la bienvenida a nuestro <strong>Portal de Pacientes de Cuidarte+</strong>.
                    Para finalizar su registro correctamente, le pedimos que por favor introduzca el siguiente
                    código de verificación en la aplicación:</p>

                    <p style="font-size: 18px; font-weight: bold; color: blue;">5555</p>

                    <p>Saludos,<br><em>El equipo de Cuidarte+</em></p>
                </body>
                </html>
                """
        );

        System.out.println(emailSubject);
        System.out.println(emailBody);

        //System.getenv().forEach((k, v) -> System.out.println("Env: " + k + "=" + v));

    }
}