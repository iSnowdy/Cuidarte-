package Calendar.Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PanelSlide extends JPanel {
    private final int animationSpeed = 10; // Per frame
    private final Timer timer;

    private Component currentComponent;
    private Component newComponent;

    public enum AnimationType {
        TO_RIGHT,
        TO_LEFT;
    }

    private AnimationType animationType;

    public PanelSlide() {
        initComponents();
        setLayout(null);

        // Adjusts the size of the new component to the current panel's size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                if (newComponent != null) {
                    newComponent.setSize(getSize());
                }
            }
        });

        this.timer = new Timer(0, e -> animate());
    }

    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 319, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 192, Short.MAX_VALUE)
        );
    }

    public void show(Component component, AnimationType animationType) {
        if (!timer.isRunning()) {
            this.animationType = animationType;
            this.newComponent = component;
            newComponent.setSize(getSize());

            if (getComponentCount() == 0) {
                add(component);
                currentComponent = component;
                repaint();
                revalidate();
            } else {
                if (animationType == AnimationType.TO_RIGHT) {
                    newComponent.setLocation(-newComponent.getWidth(), 0);
                } else {
                    newComponent.setLocation(getWidth(), 0);
                }

                add(component);
                repaint();
                revalidate();
                timer.start();
            }
        }
    }

    // Slide animation
    private void animate() {
        if (animationType == AnimationType.TO_RIGHT) {
            if (newComponent.getLocation().x < 0) {
                newComponent.setLocation(newComponent.getLocation().x + animationSpeed, 0);
                currentComponent.setLocation(currentComponent.getLocation().x + animationSpeed, 0);
            } else {
                stopAnimation();
            }
        } else {
            if (newComponent.getLocation().x > 0) {
                newComponent.setLocation(newComponent.getLocation().x - animationSpeed, 0);
                currentComponent.setLocation(currentComponent.getLocation().x - animationSpeed, 0);
            } else {
                stopAnimation();
            }
        }
    }

    // Stops the animation and refreshes the view
    private void stopAnimation() {
        newComponent.setLocation(0, 0);
        timer.stop();
        remove(currentComponent);
        currentComponent = newComponent;
    }

    // Getters and Setters
    public int getAnimationSpeed() {
        return animationSpeed;
    }
}
