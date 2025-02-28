package Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static Utils.Swing.Colors.HINT_GREY;
import static Utils.Swing.Fonts.MAIN_FONT;

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
    // Paints the TextField
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(230, 245, 241));
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        paintIcon(graphics);
        super.paintComponent(graphics);
    }
    // Adds the text to the TextField
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (getText().isEmpty()) {
            int userInputTextHeight = getHeight();
            // Casting Graphics to Graphics2D so we can enable antialiasing
            ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // Space between the border and the text
            Insets insets = getInsets();
            FontMetrics fontMetrics = graphics.getFontMetrics();
            graphics.setColor(HINT_GREY); // Placeholder text colour
            graphics.drawString(hintText, insets.left, userInputTextHeight / 2 + fontMetrics.getAscent() / 2 - 2);
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

    // Border surrounding the Icon
    private void setIconBorder() {
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

    // Getters and Setters

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
        setIconBorder();
    }

    public Icon getSuffixIcon() {
        return suffixIcon;
    }

    public void setSuffixIcon(Icon suffixIcon) {
        this.suffixIcon = suffixIcon;
        setIconBorder();
    }
}