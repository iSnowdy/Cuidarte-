package Components;

import UI.Authentication.MessageTypes;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Colors.MY_RED;
import static Utils.Swing.Fonts.MAIN_FONT;

public class MyMessage extends JPanel {

    private MessageTypes messageType = MessageTypes.SUCCESS;
    private boolean show;
    private JLabel messageLabel;

    public MyMessage() {
        setOpaque(false);
        setVisible(false);
        initMessageComponents();
    }

    private void initMessageComponents() {
        messageLabel = new JLabel("Mensaje");
        messageLabel.setFont(MAIN_FONT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(messageLabel, BorderLayout.CENTER);
    }

    public void showMessage(MessageTypes messageType, String message) {
        this.messageType = messageType;
        messageLabel.setText(message);

        if (messageType == MessageTypes.SUCCESS) {
            messageLabel.setIcon(new ImageIcon(getClass().getResource("/General/success.png")));
        } else {
            messageLabel.setIcon(new ImageIcon(getClass().getResource("/General/error.png")));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2D.setColor(Color.WHITE);
        if (messageType == MessageTypes.SUCCESS) {
            messageLabel.setForeground(MAIN_APP_COLOUR); // TODO: Colour
        } else {
            messageLabel.setForeground(MY_RED);
        }

        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f)); // Opacity

        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        // Draws the border of the rectangle
        graphics2D.setComposite(AlphaComposite.SrcOver);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        super.paintComponent(graphics);
    }

    // Getters and Setters

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
