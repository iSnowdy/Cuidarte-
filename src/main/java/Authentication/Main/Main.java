package Authentication.Main;

import Authentication.Swing.PanelCover;
import Authentication.Swing.PanelLoginAndRegister;
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
    private javax.swing.JLayeredPane backGround;

    private MigLayout layout;
    private PanelCover cover;
    private PanelLoginAndRegister loginAndRegister;

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

    private void start() {
        // Layout Constraints | Column Constraints
        this.layout = new MigLayout("fill, insets 0");
        this.cover = new PanelCover();
        this.loginAndRegister = new PanelLoginAndRegister();


        TimingTarget target = createTimingTarget();
        Animator animator = createAnimator(target);

        this.backGround.setLayout(layout);
        this.backGround.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        this.backGround.add(loginAndRegister, "width " + loginSize + "%, pos 1al 0 n 100%");
        this.cover.addEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked");
                if (!animator.isRunning()) animator.start();
            }
        });
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
        } else {
            fractionCover = fraction;
            fractionLogin = 1f - fraction;
        }
        fractionCover = Double.valueOf(decimalFormat.format(fractionCover));
        fractionLogin = Double.valueOf(decimalFormat.format(fractionLogin));

        layout.setComponentConstraints(cover, "width " + size
                + "%, pos " + fractionCover + "al 0 n 100%");
        layout.setComponentConstraints(loginAndRegister, "width " + loginSize
                + "%, pos " + fractionLogin + "al 0 n 100%");

        backGround.revalidate(); // Redraw every frame
    }

    // Click on Login / Register triggers the slide animation
    private Animator createAnimator(TimingTarget target) {
        final int ANIMATION_DURATION = 1000;
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