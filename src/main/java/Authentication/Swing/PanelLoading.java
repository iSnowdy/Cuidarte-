package Authentication.Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class PanelLoading extends JPanel {
    private JLabel loadingLabel;

    public PanelLoading() {
        setOpaque(false);
        setFocusCycleRoot(true); // What is this exactly?
        setVisible(false);
        setFocusable(true);
        initComponents();
        // Empty on purpose. It prevents the user from clicking anywhere else while this is visible
        addMouseListener(new MouseAdapter() {});
    }

    // Add loading Icon as a Gif
    private void initComponents() {
        loadingLabel = new JLabel();
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setIcon(new ImageIcon(getClass().getResource("/LoginRegisterImgs/loading-bar.gif")));

        setLayout(new BorderLayout());
        add(loadingLabel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(new Color(255, 255, 255)); // Background colour while the Panel is visible TODO: Change it to a grey?
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% opacity
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }
}
