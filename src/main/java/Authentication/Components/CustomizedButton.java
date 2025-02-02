package Authentication.Components;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CustomizedButton extends JButton {
    private Animator buttonAnimator;
    private Point pressedPoint;
    private int targetSize;
    private float animationOpacity;
    private float animationSize;
    private Color effectColour = new Color(255, 150, 100); // TODO: Colour for the animation?

    public CustomizedButton() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
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

    @Override
    protected void paintComponent(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();

        // Creates a buffered image of the same size of the component
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(getBackground());
        graphics2D.fillRoundRect(0, 0, width, height, height, height);

        // Draws the ripple effect
        if (pressedPoint != null) {
            graphics2D.setColor(effectColour);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animationOpacity));
            graphics2D.fillOval((int) (pressedPoint.x - animationSize / 2), (int) (pressedPoint.y - animationSize / 2), (int) animationSize, (int) animationSize);
        }

        graphics2D.dispose();
        graphics.drawImage(image, 0, 0, width, height, null);
        super.paintComponent(graphics);
    }
}
