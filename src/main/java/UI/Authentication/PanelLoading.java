package UI.Authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class PanelLoading extends JPanel {
    private final JLabel loadingLabel;
    private final Color DEFAULT_BACKGROUND = new Color(200, 200, 200, 128); // Greyish with opacity

    public PanelLoading() {
        setOpaque(false);
        setLayout(new BorderLayout());

        // Prevents user interactions while the panel is active
        addMouseListener(new MouseAdapter() {});

        loadingLabel = new JLabel("", SwingConstants.CENTER);
        setLoadingIcon("/LoginRegisterImgs/loading-bar.gif");

        add(loadingLabel, BorderLayout.CENTER);
    }

    /**
     * Sets a new GIF loading icon.
     * @param path Resource path of the GIF.
     */
    public void setLoadingIcon(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        loadingLabel.setIcon(icon);
    }

    /**
     * Displays the loading panel.
     */
    public void showLoading() {
        setVisible(true);
    }

    /**
     * Hides the loading panel.
     */
    public void hideLoading() {
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(DEFAULT_BACKGROUND);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }
}
