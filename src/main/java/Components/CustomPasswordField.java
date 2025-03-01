package Components;

import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.HINT_GREY;
import static Utils.Swing.Fonts.MAIN_FONT;

public class CustomPasswordField extends JPasswordField {
    private Icon prefixIcon;
    private final JLabel eyeIconLabel;
    private final ImageIconRedrawer iconRedrawer;
    private boolean isPasswordVisible;
    private String hintText = "";

    public CustomPasswordField() {
        iconRedrawer = new ImageIconRedrawer();
        isPasswordVisible = false; // Default state: hidden

        setEchoChar('•'); // Mask password
        setFont(MAIN_FONT);
        setForeground(HINT_GREY);
        setBorder(new EmptyBorder(10, 10, 10, 40)); // Space for icons
        setBackground(new Color(0, 0, 0, 0)); // Transparent background

        eyeIconLabel = new JLabel(getEyeIcon(false));
        eyeIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eyeIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePasswordVisibility();
            }
        });

        setLayout(new BorderLayout());
        add(eyeIconLabel, BorderLayout.EAST);
    }

    // Paints the background to match other input fields
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(230, 245, 241)); // Match other fields' background
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        paintIcon(graphics);
        super.paintComponent(graphics);
    }

    // Displays hint text when field is empty
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (getPassword().length == 0) {
            int userInputTextHeight = getHeight();
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets insets = getInsets();
            FontMetrics fontMetrics = g2.getFontMetrics();
            g2.setColor(HINT_GREY);
            g2.drawString(hintText, insets.left, userInputTextHeight / 2 + fontMetrics.getAscent() / 2 - 2);
        }
    }

    // Toggles password visibility
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        setEchoChar(isPasswordVisible ? (char) 0 : '•');
        eyeIconLabel.setIcon(getEyeIcon(isPasswordVisible));
    }

    // Returns appropriate eye icon
    private ImageIcon getEyeIcon(boolean isOpen) {
        String iconPath = isOpen ? "/LoginRegisterImgs/eye-open.png" : "/LoginRegisterImgs/eye-closed.png";
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconPath)));
        return iconRedrawer.redrawImageIcon(20, 20);
    }

    // Paints prefix icon (left side icon)
    private void paintIcon(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (prefixIcon != null) {
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            graphics2D.drawImage(prefix, 10, y, this);
        }
    }

    // Sets the left-side icon
    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        setBorder(new EmptyBorder(10, prefixIcon != null ? 35 : 10, 10, 40));
    }

    // Sets hint text
    public void setHintText(String hintText) {
        this.hintText = hintText;
    }
}
