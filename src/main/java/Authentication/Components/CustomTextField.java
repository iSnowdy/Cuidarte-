package Authentication.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static Utils.Colors.HINT_GREY;
import static Utils.Fonts.MAIN_FONT;

public class CustomTextField extends JTextField {
    private Icon prefixIcon;
    private Icon suffixIcon;
    private String hintText = "";

    public CustomTextField() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(0, 0, 0, 0));
        setForeground(HINT_GREY);
        setFont(MAIN_FONT); // TODO: Font
        //setSelectionColor(new Color(75, 175, 152));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // ?
        graphics2D.setColor(new Color(230, 245, 241));
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        paintIcon(graphics);
        super.paintComponent(graphics);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (getText().isEmpty()) {
            int h = getHeight();
            ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets insets = getInsets();
            FontMetrics fm = graphics.getFontMetrics();
            graphics.setColor(new Color(200, 200, 200));
            graphics.drawString(hintText, insets.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    private void paintIcon(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (prefixIcon != null) {
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;
            graphics2D.drawImage(prefix, 10, y, this);
        }

        if (suffixIcon != null) {
            Image suffix = ((ImageIcon) suffixIcon).getImage();
            int y = (getHeight() - suffixIcon.getIconHeight()) / 2;
            graphics2D.drawImage(suffix, getWidth() - suffixIcon.getIconWidth() - 10, y, this);
        }
    }

    private void initBorder() {
        int leftSideIconSize = 15;
        int rightSideIconSize = 15;

        if (prefixIcon != null) {
            leftSideIconSize = prefixIcon.getIconWidth() + 15;
        }
        if (suffixIcon != null) {
            rightSideIconSize = suffixIcon.getIconWidth() + 15;
        }
        setBorder(new EmptyBorder(10, leftSideIconSize, 10, rightSideIconSize));
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public Icon getPrefixIcon() {
        return prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon) {
        this.prefixIcon = prefixIcon;
        initBorder();
    }

    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        initBorder();
    }
}