package Authentication.Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Utils.Colors.*;
import static Utils.Fonts.MAIN_FONT;

public class PanelCover extends JPanel {

    private JButton actionButton;
    private ActionListener actionlistener;

    public PanelCover() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        initButton();
    }

    private void initButton() {
        actionButton = new JButton("Test Button 1");
        actionButton.setFont(MAIN_FONT); // TODO: Fonts
        actionButton.setForeground(Color.RED);
        actionButton.setFocusPainted(false);
        actionButton.setBackground(Color.YELLOW); // TODO: Appropriate colour
        actionButton.addActionListener(e -> {
            if (this.actionlistener != null) {
                this.actionlistener.actionPerformed(e);
            }
        });
        this.add(actionButton);
    }

    private void jButtonActionPerformed(ActionEvent e) {
        this.actionlistener.actionPerformed(e);
    }

    // Paints the coloured background for the Login / Register
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientPaint = new GradientPaint(0,0,MAIN_APP_COLOUR,100,100, SECONDARY_APP_COLOUR);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addEvent(ActionListener event) {
        this.actionlistener = event;
    }
}
