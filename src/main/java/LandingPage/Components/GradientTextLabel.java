package LandingPage.Components;

import javax.swing.*;
import java.awt.*;

public class GradientTextLabel extends JLabel {
    private Color startColor;
    private Color endColor;

    /**
     * Creates a GradientTextLabel with customizable gradient colors.
     *
     * @param text      The text to display.
     * @param startColor The starting color of the gradient (top).
     * @param endColor   The ending color of the gradient (bottom).
     */
    public GradientTextLabel(String text, Color startColor, Color endColor) {
        super(text);
        this.startColor = startColor;
        this.endColor = endColor;
        setFont(new Font("Arial", Font.BOLD, 56)); // TODO: Default font. Maybe change this?
        setHorizontalAlignment(SwingConstants.CENTER); // Center text
        setOpaque(false); // Make the background transparent
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create vertical gradient from startColor (top) to endColor (bottom)
        GradientPaint gradient = new GradientPaint(
                0, 0, startColor, // Top color
                0, getHeight(), endColor // Bottom color
        );
        g2d.setPaint(gradient);

        // Get text size and position it correctly
        FontMetrics metrics = g2d.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        // Draw text with gradient
        g2d.drawString(getText(), x, y);
    }

    /**
     * Allows changing the gradient colors dynamically.
     *
     * @param startColor New start color.
     * @param endColor   New end color.
     */
    public void setGradientColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        repaint(); // Refresh component
    }
}
