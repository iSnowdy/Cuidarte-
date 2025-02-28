package Components;

import javax.swing.*;
import java.awt.*;

public class PanelRound extends JPanel {

    public PanelRound() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.WHITE); // TODO: Colour
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(graphics);
    }
}
