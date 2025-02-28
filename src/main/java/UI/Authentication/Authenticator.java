package UI.Authentication;

import Exceptions.DatabaseInsertException;
import Components.NotificationPopUp;
import Database.Models.Patient;
import Database.DAO.PatientServices;
import Utils.Utility.JavaMailSender;
import Utils.Utility.CustomLogger;
import Utils.Utility.PasswordHasher;
import Utils.Validation.MyValidator;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authenticator extends JPanel {
    protected static final Logger LOGGER = CustomLogger.getLogger(Authenticator.class);

    private final JLayeredPane backGround;
    private final MigLayout layout;
    private Animator animator;
    private final PanelCover cover;
    private final PanelLoginAndRegister loginAndRegister;
    private final PanelLoading panelLoading;
    private final PanelVerifyCode panelVerifyCode;

    private Patient tempPatient;
    private Consumer<Patient> loginCallBack;

    private PatientServices patientServices;
    private JavaMailSender javaMailSender;

    private final int addSize = 30;
    private final int coverSize = 40;
    private final int loginSize = 60;
    private boolean isLogin;
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.###");

    private boolean isUserLoggedIn = false;

    public Authenticator(Consumer<Patient> loginCallBack) {
        this.loginCallBack = loginCallBack;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        this.backGround = new JLayeredPane();
        this.backGround.setLayout(new BorderLayout());
        add(backGround, BorderLayout.CENTER);

        layout = new MigLayout("fill, insets 0");
        cover = new PanelCover();
        panelLoading = new PanelLoading();
        panelVerifyCode = new PanelVerifyCode();
        loginAndRegister = new PanelLoginAndRegister(
                e -> registerPatient(),
                e -> loginPatient(),
                this::showMessage
        );

        initComponents();
        setupAnimations();
    }

    private void initComponents() {
        try {
            patientServices = new PatientServices();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Patient and Doctor services.", e);
        }

        backGround.setLayout(layout);
        backGround.setLayer(panelLoading, JLayeredPane.POPUP_LAYER);
        backGround.setLayer(panelVerifyCode, JLayeredPane.POPUP_LAYER);

        // Initially hide the loading and verify code panels. They will only be shown upon
        // user interaction while registering
        panelLoading.hideLoading();
        panelVerifyCode.hidePanel();

        backGround.add(panelLoading, "pos 0 0 100% 100%");
        backGround.add(panelVerifyCode, "pos 0 0 100% 100%");
        backGround.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        backGround.add(loginAndRegister, "width " + loginSize + "%, pos 1al 0 n 100%");

        cover.addEvent(e -> {
            if (!animator.isRunning()) animator.start();
        });

        panelVerifyCode.addEventButtonOK(e -> verifyPatientCode(tempPatient));
    }

    private void registerPatient() {
        try {
            // Retrieve patient data from the UI
            this.tempPatient = loginAndRegister.getPatientData().orElse(null);
            if (tempPatient == null) return;

            if (patientServices.verifyIfPatientExists(tempPatient)) {
                showMessage(MessageTypes.ERROR, "El paciente con los datos introducidos ya existe.");
                return;
            }

            // Send verification email
            showMessage(MessageTypes.SUCCESS, "Se le ha enviado un código de verificación a su correo " +
                    "(" + tempPatient.getEmail() + ") para finalizar el proceso.");

            sendEmailToPatient(tempPatient);

        } catch (DatabaseInsertException e) {
            // Handle duplicate patient or invalid password errors
            showMessage(MessageTypes.ERROR, e.getMessage());
            LOGGER.log(Level.WARNING, "Patient registration failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch any unexpected errors
            showMessage(MessageTypes.ERROR, "Error al registrar el usuario");
            LOGGER.log(Level.SEVERE, "Unexpected error during patient registration.", e);
        }
    }

    private void sendEmailToPatient(Patient patient) {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(panelLoading::showLoading);

                int minimumWaitingTime = 5000;
                long startTime = System.currentTimeMillis();

                javaMailSender = new JavaMailSender(patient.getEmail());
                javaMailSender.generateVerificationEmail(patient.getFirstName(), patient.getVerificationCode());

                boolean isEmailSent = javaMailSender.sendEmailInBackground();

                // Like this we make sure the loading panel is showing at least 3 seconds
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < minimumWaitingTime) {
                    Thread.sleep(minimumWaitingTime - elapsedTime);
                }

                SwingUtilities.invokeLater(() -> {
                    panelLoading.hideLoading();
                    if (isEmailSent) panelVerifyCode.showPanel();
                    else showMessage(MessageTypes.ERROR, "Error al enviar correo al usuario");
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    panelLoading.hideLoading();
                    showMessage(MessageTypes.ERROR, "Error al enviar correo al usuario");
                });
                LOGGER.log(Level.SEVERE, "Error while trying to send email to the user", e);
            }
        }).start();
    }

    private void verifyPatientCode(Patient patient) {
        try {
            if (patient == null) return;

            int inputCode = panelVerifyCode.getInputCodeAsInt().orElse(-1);
            int patientCode = patient.getVerificationCode();

            if (MyValidator.verifyPatientCode(patientCode, inputCode)) {
                patientServices.registerPatient(patient);
                showMessage(MessageTypes.SUCCESS, "Código correcto. Registro completado.");
                this.userLoggedIn(true);
                if (loginCallBack != null) loginCallBack.accept(patient);

                // Clear cache just in case
                patient = null;
                panelVerifyCode.hidePanel();
            } else {
                showMessage(MessageTypes.ERROR, "Código incorrecto. Inténtelo nuevamente.");
                this.userLoggedIn(false);
            }
        } catch (Exception e) {
            showMessage(MessageTypes.ERROR, "Error en la verificación.");
            e.printStackTrace();
        }
    }

    private void loginPatient() {
        try {
            // Retrieve user input
            String email = loginAndRegister.getEmailInputFromLogin();
            String password = loginAndRegister.getPasswordInputFromLogin();

            // TODO: To keep track of login credentials
            System.out.println("Email is: " + email);
            System.out.println("Password is: " + password);

            // Validate input before proceeding
            if (email.isEmpty() || password.isEmpty()) {
                showMessage(MessageTypes.ERROR, "Por favor, ingrese su correo y contraseña.");
                return;
            }

            // Attempt to find patient by email
            Optional<Patient> patientOpt = patientServices.getPatientByEmail(email);
            if (patientOpt.isEmpty()) {
                showMessage(MessageTypes.ERROR, "Correo no registrado.");
                return;
            }

            Patient patient = patientOpt.get();

            // Verify password using the hasher
            if (!(PasswordHasher.verifyPassword(password, patient.getSalt(), patient.getPassword()))) {
                showMessage(MessageTypes.ERROR, "Contraseña incorrecta.");
                return;
            }

            // Successful login
            showMessage(MessageTypes.SUCCESS, "Inicio de sesión exitoso.");
            LOGGER.info("Patient " + patient.getEmail() + " logged in successfully.");
            this.userLoggedIn(true);
            if (loginCallBack != null) loginCallBack.accept(patient);

            // TODO: Integrate with the main dashboard or transition to the next screen
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during patient login", e);
            showMessage(MessageTypes.ERROR, "Error durante el inicio de sesión.");
        }
    }

    private void showMessage(MessageTypes messageType, String message) {
        SwingUtilities.invokeLater(() -> {
            switch (messageType) {
                case SUCCESS -> NotificationPopUp.showInfoMessage(this, "Éxito", message);
                case ERROR -> NotificationPopUp.showErrorMessage(this, "Error", message);
                case WARNING -> NotificationPopUp.showWarningMessage(this, "Advertencia", message);
                default -> NotificationPopUp.showInfoMessage(this, "Información", message);
            }
        });
    }

    // Animations

    private void setupAnimations() {
        TimingTarget target = createTimingTarget();
        animator = createAnimator(target, 1000);
    }

    private TimingTarget createTimingTarget() {
        return new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                updateEveryFrameAnimation(fraction);
            }

            @Override
            public void end() {
                isLogin = !isLogin;
            }
        };
    }

    private void updateEveryFrameAnimation(float fraction) {
        double fractionCover = isLogin ? 1f - fraction : fraction;
        double fractionLogin = isLogin ? fraction : 1f - fraction;
        double size = coverSize + (fraction <= 0.5f ? fraction * coverSize : addSize - (fraction * coverSize));

        // Rounding using Math.max and decimalFormat as to not use a very small number (MigLayout throws error)
        fractionCover = Math.max(0.001, fractionCover);
        fractionLogin = Math.max(0.001, fractionLogin);
        fractionCover = Double.parseDouble(decimalFormat.format(fractionCover));
        fractionLogin = Double.parseDouble(decimalFormat.format(fractionLogin));

        if (fraction >= 0.5f) {
            loginAndRegister.showRegisterForm(isLogin);
            cover.animateText(!isLogin);
        }
        layout.setComponentConstraints(cover, "width " + size + "%, pos " + fractionCover + "al 0 n 100%");
        layout.setComponentConstraints(loginAndRegister, "width " + loginSize + "%, pos " + fractionLogin + "al 0 n 100%");
        backGround.revalidate();
    }

    private Animator createAnimator(TimingTarget target, int duration) {
        Animator animator = new Animator(duration, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);
        return animator;
    }

    public void userLoggedIn(boolean isLoggedIn) {
        this.isUserLoggedIn = isLoggedIn;
    }

    public Patient getPatientFromAuthenticator() {
        return tempPatient;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }
}
