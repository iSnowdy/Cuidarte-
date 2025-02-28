package Components;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.Evaluator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;

public abstract class BaseButton extends JButton {

    public BaseButton() {
        super();
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
}
