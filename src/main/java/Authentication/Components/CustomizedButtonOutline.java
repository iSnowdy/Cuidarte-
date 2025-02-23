package Authentication.Components;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomizedButtonOutline extends BaseButton {

    public CustomizedButtonOutline() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(5, 0, 5, 0));
        setBackground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(getBackground()); // Noice
        graphics2D.drawRoundRect(0, 0, width - 1, height - 1, height, height); // Play with this a bit

        super.paintComponent(graphics2D);
    }
}
