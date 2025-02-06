package Authentication.Main;

/*
TODO: Make it so that the window pops up in the middle of the screen and of a certain size. As a pop-up
      login/register kind of thing
 */

import Authentication.DataExample.UserExample;
import Authentication.MessageTypes;
import Authentication.Swing.*;
import Models.Patient;
import Services.DB.DoctorServices;
import Services.DB.PatientServices;
import Services.JavaMail.JavaMailSender;
import Utils.Validation.AuthenticationValidator;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Main extends JFrame {
    private JLayeredPane backGround;

    private MigLayout layout;
    private PanelCover cover;
    private PanelLoginAndRegister loginAndRegister;
    private PanelLoading panelLoading;
    private PanelVerifyCode panelVerifyCode;

    private PatientServices patientServices;
    private DoctorServices doctorServices;

    private JavaMailSender javaMailSender;


    private final int addSize = 30;
    private final int coverSize = 40;
    private final int loginSize = 60;

    private boolean isLogin;
    // Specify how decimals will be formatted. This would round only if the value has 3 (###) decimals
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.###");


    public Main() {
        setTitle("Authentication Login / Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 1000); // TODO: Check appropriate sizes

        initComponents();
        start();
    }

    private void initComponents() {
        this.backGround = new JLayeredPane();
        this.backGround.setLayout(new BorderLayout());
        this.backGround.setBackground(Color.WHITE);
        this.backGround.setOpaque(true);

        this.add(backGround, BorderLayout.CENTER);
    }

    // TODO: Refactor PanelLoading. Too much code here
    private void start() {
        // Surround with try-catch?
        this.patientServices = new PatientServices();
        this.doctorServices = new DoctorServices();

        // Layout Constraints | Column Constraints
        this.layout = new MigLayout("fill, insets 0");
        this.cover = new PanelCover();
        this.panelLoading = new PanelLoading();
        this.panelVerifyCode = new PanelVerifyCode();

        ActionListener eventRegister = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        };
        this.loginAndRegister = new PanelLoginAndRegister(eventRegister);


        TimingTarget target = createTimingTarget();
        Animator animator = createAnimator(target, 1000);

        this.backGround.setLayout(layout);
        this.backGround.setLayer(panelLoading, JLayeredPane.POPUP_LAYER); // what is this?
        this.backGround.setLayer(panelVerifyCode, JLayeredPane.POPUP_LAYER);
        // Cover the whole window with the loading GIF
        this.backGround.add(panelLoading, "pos 0 0 100% 100%");
        this.backGround.add(panelVerifyCode, "pos 0 0 100% 100%");
        this.backGround.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        this.backGround.add(loginAndRegister, "width " + loginSize + "%, pos 1al 0 n 100%");
        this.cover.addEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked");
                if (!animator.isRunning()) animator.start();
            }
        });
        this.panelVerifyCode.addEventButtonOK(new ActionListener() {

        });
    }

    private void register() {
        Patient patient = loginAndRegister.getPatient();

        try {
            if (AuthenticationValidator.checkForDuplicatePatient(patient)) {
                showMessage(MessageTypes.ERROR, "Paciente ya registrado con ese DNI o correo");
            } else {
                patientServices.registerPatient(patient);
                showMessage(MessageTypes.SUCCESS, "Paciente con DNI " + patient.getDNI() + " registrado correctamente");
                System.out.println("Patient email is: " + patient.getEmail());
                sendMain(patient);
            }
        } catch (Exception e) {
            showMessage(MessageTypes.ERROR, "Error al registrar el usuario");
        }

        //showMessage(MessageTypes.SUCCESS, "Testing Message");
        //this.panelLoading.setVisible(true);
        this.panelVerifyCode.setVisible(true);
        System.out.println(patient.getFirstName());
        System.out.println(patient.getEmail());
    }

    private void sendMain(Patient patient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    panelLoading.setVisible(true);
                    javaMailSender = new JavaMailSender(patient.getEmail());
                    javaMailSender.generateVerificationEmail(patient.getFirstName(), patient.getVerificationCode());
                    if (javaMailSender.sendEmailInBackground()) {
                        panelLoading.setVisible(false);
                        panelVerifyCode.setVisible(true);
                    } else {
                        panelLoading.setVisible(false);
                        showMessage(MessageTypes.ERROR, "Error al enviar correo al usuario");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error during thread for loading panel");
                }
            }
        }).start();
    }

    private void showMessage(MessageTypes messageType, String message) {
        Message messageToShow = new Message();
        messageToShow.showMessage(messageType, message);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                if (!messageToShow.isShowing()) {
                    // Adds the message to the background
                    backGround.add(messageToShow, "pos 0.5al -10", 0); // Starting position
                    messageToShow.setVisible(true);
                    backGround.repaint();
                }
            }

            @Override
            public void timingEvent(float fraction) {
                // Distance, in px, that the message will move throughout the animation
                float value;
                if (messageToShow.isShowing()) {
                    value = 40 * (1f - fraction); // Moves up the message if it's showing
                } else {
                    value = 40 * fraction; // Moves down the message to show it
                }
                layout.setComponentConstraints(messageToShow, "pos 0.5al " + (int) (value - 10));
                backGround.repaint(); // Redraws the component
                backGround.revalidate(); // Updates the position and size of the components
            }

            @Override
            public void end() {
                if (messageToShow.isShowing()) {
                    backGround.remove(messageToShow);
                    backGround.repaint();
                    backGround.revalidate();
                } else {
                    messageToShow.setShow(true);
                }
            }
        };
        int animationDuration = 2000;
        Animator messageAnimator = createAnimator(target, animationDuration);
        messageAnimator.start();
    }

    // TimingTarget is an interface to define methods executed during an animation
    // timingEvent() is called on every frame of the animation. Starting from 0.0 to 1.0
    // end() is called when the animation ends
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
    // TODO: Too much going on here. Extract methods!
    private void updateEveryFrameAnimation(float fraction) {
        double fractionCover;
        double fractionLogin;
        double size = coverSize;

        // First half of the animation (0.5f) it will grow (expand)
        // Then, second half (else) will contract, going back to the original size
        if (fraction <= 0.5f) {
            size += fraction * size;
        } else {
            size += addSize - (fraction * size);
        }
        // Determines where the Panel will move
        // when isLogin it will revert the animation (setting fraction back to 0.0)
        // Covers are opposed to each other
        if (isLogin) {
            fractionCover = 1f - fraction;
            fractionLogin = fraction;

            // According to where the frame of the animation is, move the text according to it
            if (fraction >= 0.5f) {
                cover.registerRight(fractionCover * 100);
            } else {
                cover.loginRight(fractionLogin * 100);
            }
        } else {
            fractionCover = fraction;
            fractionLogin = 1f - fraction;

            if (fraction <= 0.5f) {
                cover.registerLeft(fraction * 100);
            } else {
                cover.loginLeft((1f - fraction) * 100);
            }
        }

        if (fraction >= 0.5f) loginAndRegister.showRegisterForm(isLogin);

        fractionCover = Double.valueOf(decimalFormat.format(fractionCover));
        fractionLogin = Double.valueOf(decimalFormat.format(fractionLogin));

        layout.setComponentConstraints(cover, "width " + size
                + "%, pos " + fractionCover + "al 0 n 100%");
        layout.setComponentConstraints(loginAndRegister, "width " + loginSize
                + "%, pos " + fractionLogin + "al 0 n 100%");

        backGround.revalidate(); // Redraw every frame
    }

    // Click on Login / Register triggers the slide animation
    private Animator createAnimator(TimingTarget target, int duration) {
        final int ANIMATION_DURATION = duration; // Adjust it?
        final float ANIMATION_ACELERATION = 0.5f;
        final float ANIMATION_DECELERATION = 0.5f;

        Animator animator = new Animator(ANIMATION_DURATION, target);
        animator.setAcceleration(ANIMATION_ACELERATION);
        animator.setDeceleration(ANIMATION_DECELERATION);
        animator.setResolution(0); // Smooth animation

        return animator;
    }

    public static void main(String[] args) {



        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}