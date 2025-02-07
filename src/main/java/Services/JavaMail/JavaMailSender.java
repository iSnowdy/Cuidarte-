package Services.JavaMail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JavaMailSender {
    private String
            emailTo,
            emailFrom, hostUserName, hostPassword, host;

    private String emailSubject, emailBody;
    private Properties properties;
    private Session session;
    private ExecutorService backgroundExecutor;

    public JavaMailSender(final String emailTo) {
        this.emailTo = emailTo;

        // System User Environment Variables
        this.emailFrom = System.getenv("TEST_EMAIL_USERNAME");
        this.hostUserName = System.getenv("TEST_EMAIL_USERNAME");
        this.hostPassword = System.getenv("TEST_EMAIL_PASSWORD");

        this.host = "smtp.gmail.com";
        checkEnvironmentVariables();
        this.backgroundExecutor = Executors.newSingleThreadExecutor();
    }

    private void checkEnvironmentVariables() {
        if ( this.emailFrom == null || this.hostUserName == null || this.hostPassword == null) {
            throw new IllegalArgumentException("Missing required environment variables");
        }
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    // TODO: How to check if the email is sent and return boolean?
    public boolean sendEmailInBackground() {
        this.backgroundExecutor.execute(new Runnable() {
            public void run() {
                sendEmail();
            }
        });
        return true;
    }

    private boolean sendEmail() {
        if (isAbleToSendEmail()) {
            createProperties();
            createSession();

            try {
                Message message = new MimeMessage(session);

                message.setFrom(new InternetAddress(this.emailFrom)); // From who
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailTo)); // To who
                message.setSubject(this.emailSubject); // Subject
                message.setText(this.emailBody); // Body


                Transport.send(message);
                return true;
            } catch (MessagingException e) {
                System.out.println("Error while trying to send the email");
                e.printStackTrace();
                return false;
            }
        }  else {
            System.out.println("Insufficient data to send the email");
            return false;
        }
    }

    private boolean isAbleToSendEmail() {
        return  this.emailTo != null && !this.emailTo.isEmpty()
                && this.emailFrom != null && !this.emailFrom.isEmpty()
                && this.emailSubject != null && !this.emailSubject.isEmpty()
                && this.emailBody != null && !this.emailBody.isEmpty();
    }

    private void createProperties() {
        this.properties = new Properties();

        this.properties.put("mail.smtp.auth", true);
        this.properties.put("mail.smtp.starttls.enable", true);
        this.properties.put("mail.smtp.host", this.host);
        this.properties.put("mail.smtp.port", "587");
    }

    private void createSession() {
        this.session = Session.getDefaultInstance(this.properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(hostUserName, hostPassword);
                    }
                });
    }

    public void generateVerificationEmail(String patientName, int verificationCode) {
        this.emailSubject =
                "Correo de verificación para el Paciente " + patientName + " - Cuidarte+";

        this.emailBody =
                "Buenas,\n\n" +
                        "Le queremos dar la bienvenida a nuestro Portal de Pacientes de Cuidarte+.\n" +
                        "Para finalizar su registro correctamente, le pedimos que por favor introduzca " +
                        "el siguiente codigo de verificación en la aplicación:\n\n" +
                        verificationCode +
                        "\n\n" +
                        "Saludos,\n" +
                        "El equipo de Cuidarte+";
    }
}
