package UI.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PanelSlide extends JPanel {
    private static final int DEFAULT_ANIMATION_SPEED = 15; // Default speed per frame
    private static final int TIMER_DELAY = 10; // Delay for smooth animations

    private final Timer timer;
    private int animationSpeed;
    private Component currentComponent;
    private Component newComponent;
    private AnimationType animationType;

    public enum AnimationType {
        TO_RIGHT,
        TO_LEFT
    }

    public PanelSlide() {
        this.animationSpeed = DEFAULT_ANIMATION_SPEED;
        setLayout(null);
        this.timer = new Timer(TIMER_DELAY, e -> animateTransition());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                adjustNewComponentSize();
            }
        });
    }

    // Displays a new component with an animated transition.
    public void show(Component component, AnimationType animationType) {
        if (timer.isRunning()) {
            return; // Prevent animation overlap
        }

        this.animationType = animationType;
        this.newComponent = component;
        newComponent.setSize(getSize());

        if (getComponentCount() == 0) {
            initializeFirstComponent();
        } else {
            prepareForAnimation();
        }
    }

    // Handles the animation movement step by step
    private void animateTransition() {
        int movement = (animationType == AnimationType.TO_RIGHT) ? animationSpeed : -animationSpeed;

        if (isAnimationInProgress()) {
            moveComponents(movement);
        } else {
            completeAnimation();
        }
    }

    private boolean isAnimationInProgress() {
        return (animationType == AnimationType.TO_RIGHT && newComponent.getX() < 0) ||
                (animationType == AnimationType.TO_LEFT && newComponent.getX() > 0);
    }

    // Moves both components during the animation
    private void moveComponents(int movement) {
        newComponent.setLocation(newComponent.getX() + movement, 0);
        currentComponent.setLocation(currentComponent.getX() + movement, 0);
    }

    // Finalizes the animation, removes the old component, and stops the timer
    private void completeAnimation() {
        newComponent.setLocation(0, 0);
        timer.stop();
        remove(currentComponent);
        currentComponent = newComponent;
    }

    // Ensures the new component is resized properly when the panel resizes
    private void adjustNewComponentSize() {
        if (newComponent != null) {
            newComponent.setSize(getSize());
        }
    }

    // If it's the first time showing a component, add it directly
    private void initializeFirstComponent() {
        add(newComponent);
        currentComponent = newComponent;
        repaint();
        revalidate();
    }

    private void prepareForAnimation() {
        int initialPosition = (animationType == AnimationType.TO_RIGHT) ? -getWidth() : getWidth();
        newComponent.setLocation(initialPosition, 0);

        add(newComponent);
        repaint();
        revalidate();
        timer.start();
    }

    public void setAnimationSpeed(int speed) {
        if (speed > 0) {
            this.animationSpeed = speed;
        }
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }
}
