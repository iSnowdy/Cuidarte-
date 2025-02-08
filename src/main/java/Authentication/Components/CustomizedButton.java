package Authentication.Components;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.Evaluator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utils.Swing.Colors.SUBTITLE_COLOUR;

public class CustomizedButton extends JButton {
    private Animator buttonAnimator;
    private Point pressedPoint;
    private int targetSize;
    private float animationOpacity;
    private float animationSize;
    private Color effectColour = SUBTITLE_COLOUR; // TODO: Colour for the animation?

    public CustomizedButton() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setBackground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Implementation of a ripple effect animation
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Gets the highest of the two to ensure we cover the whole component
                targetSize = Math.max(getWidth(), getHeight()) * 2;
                animationSize = 0;
                // In order to expand the animation starting where we click
                pressedPoint = mouseEvent.getPoint();
                animationOpacity = 0.5f;

                if (buttonAnimator.isRunning()) buttonAnimator.stop();

                buttonAnimator.start();
            }
        });
        // To control the opacity of the animation. When it's done (fraction = 1), the opacity vanishes (0)
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (fraction > 0.5f) {
                    animationOpacity = 1 - fraction;
                }
                animationSize = fraction * targetSize;
                repaint();
            }
        };
        // TODO: Consider making this a utility class of sorts? Since it's used in multiple classes
        this.buttonAnimator = new Animator(700, target);
        this.buttonAnimator.setAcceleration(0.5f);
        this.buttonAnimator.setDeceleration(0.5f);
        this.buttonAnimator.setResolution(0);
    }

    public void addHoverEffectToButton(Color colorBase) {
        Evaluator<Color> colorEvaluator = new Evaluator<Color>() {
            @Override
            public Color evaluate(Color v0, Color v1, float fraction) {
                int r = (int) (v0.getRed() + fraction * (v1.getRed() - v0.getRed()));
                int g = (int) (v0.getGreen() + fraction * (v1.getGreen() - v0.getGreen()));
                int b = (int) (v0.getBlue() + fraction * (v1.getBlue() - v0.getBlue()));
                return new Color(r, g, b);
            }
        };

        Animator animator = PropertySetter.createAnimator(200, this, "background", colorEvaluator, colorBase, colorBase.darker());

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                animator.start();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                animator.stop();
                setBackground(colorBase);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        graphics2D.setColor(getBackground());
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        // Ripple effect
        if (pressedPoint != null) {
            graphics2D.setColor(effectColour);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animationOpacity));
            graphics2D.fillOval((int) (pressedPoint.x - animationSize / 2), (int) (pressedPoint.y - animationSize / 2),
                    (int) animationSize, (int) animationSize);
        }

        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}
