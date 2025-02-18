package Calendar.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class Cell extends JButton {
    private Date date;
    private boolean title;
    private boolean isToday;
    private boolean isHovered;

    public Cell() {
        this.isToday = false;
        this.title = false;
        this.isHovered = false;

        setContentAreaFilled(false);
        setBorder(null);
        setHorizontalAlignment(JLabel.CENTER);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        addHoverEffect();
    }

    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!title) {
                    isHovered = true;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!title) {
                    isHovered = false;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            }
        });
    }

    // TODO: Consider additional styling. Such as bold and bigger text size
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

    public Date getDate() {
        return date;
    }

    public void currentMonth(boolean month) {
        if (month) {
            setForeground(Color.BLACK);
        } else {
            setForeground(new Color(169, 169, 169));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50); // Minimum size of the cell
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hover effect
        if (isHovered) {
            g2.setColor(new Color(230, 230, 230));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Stylizes the bottom line for days
        if (title) {
            g2.setColor(MAIN_APP_COLOUR);
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }

        // Highlights current day
        if (isToday) {
            g2.setColor(MAIN_APP_COLOUR);
            int x = getWidth() / 2 - 17;
            int y = getHeight() / 2 - 17;
            g2.fillRoundRect(x, y, 35, 35, 100, 100);
        }

        super.paintComponent(g);
    }

    // Getters y Setters
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
