package Authentication.Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class PanelVerifyCode extends JPanel {

    public PanelVerifyCode() {
        initComponents();

        setOpaque(false);
        setFocusCycleRoot(true);
        setVisible(false);
        setFocusable(true);
        addMouseListener(new MouseAdapter() {});
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(Color.RED);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% opacity
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    private void initComponents() {}
}
