package Calendar.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Cell extends JButton {
    private Date date;

    private boolean title;
    private boolean isToday;

    public Cell() {
        this.isToday = false;
        this.title = false;

        setContentAreaFilled(false);
        setBorder(null);
        setHorizontalAlignment(JLabel.CENTER);
    }

    public void maskAsTitleCell() {
        this.title = true;
    }

    public void setAsToday() {
        this.isToday = true;
        setForeground(Color.WHITE);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void currentMonth(boolean month) {
        if (month) {
            setForeground(Color.RED); // If current month
        } else {
            setForeground(new Color(169, 169, 169));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (title) {
            graphics.setColor(new Color(213, 213, 213));
            graphics.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
        if (isToday) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(new Color(97, 49, 237));
            int x = getWidth() / 2 - 17;
            int y = getHeight() / 2 - 17;
            graphics2D.fillRoundRect(x, y, 35, 35, 100, 100);
        }
        super.paintComponent(graphics);
    }


    // Getters and Setters
    public boolean isTitle() {
        return title;
    }
    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isToday() {
        return isToday;
    }
    public void setToday(boolean today) {
        isToday = today;
    }
}
