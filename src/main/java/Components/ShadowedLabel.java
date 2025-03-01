package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShadowedLabel extends JLabel {
    private Color shadowColour = new Color(100, 100, 100, 100); // Shadow colour
    private int shadowOffsetX = 4; // To X
    private int shadowOffsetY = 4; // To Y
    private float shadowBlur = 5f; // Higher = more blur

    public ShadowedLabel(String text, Font font, Color textColour) {
        super(text);
        setFont(font);
        setForeground(textColour);
    }

    // To modify the shadow colour and effect
    public void setShadowColour(Color colour) {
        this.shadowColour = colour;
        repaint();
    }

    public void setShadowOffset(int offsetX, int offsetY) {
        this.shadowOffsetX = offsetX;
        this.shadowOffsetY = offsetY;
        repaint();
    }

    public void setShadowBlur(float blur) {
        this.shadowBlur = blur;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gShadow = shadow.createGraphics();

        gShadow.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gShadow.setFont(getFont());
        gShadow.setColor(shadowColour);

        // Shadow
        gShadow.drawString(getText(), shadowOffsetX, getFontMetrics(getFont()).getAscent() + shadowOffsetY);

        // Blur effect
        g2.drawImage(shadow, 0, 0, this);

        // Text colour
        g2.setColor(getForeground());
        g2.drawString(getText(), 0, getFontMetrics(getFont()).getAscent());

        g2.dispose();
    }
}
