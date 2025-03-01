package Components;

import javax.swing.*;
import java.awt.*;

public class PanelRound extends JPanel {
    private int cornerRadius = 20;

    public PanelRound() {
        setOpaque(false);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(getBackground()); // TODO: Colour here?
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(graphics);
    }
}
