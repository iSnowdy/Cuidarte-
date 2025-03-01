package UI.Calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import static Utils.Swing.Colors.*;
import static Utils.Swing.Fonts.*;

public class Cell extends JButton {
    private LocalDate date;
    private boolean isTitle, isToday, isHovered, hasPastAppointment, hasFutureAppointment, isAvailableDay;

    public Cell() {
        initializeCell();
        addHoverEffect();
    }

    // Initializes cell properties
    private void initializeCell() {
        this.isToday = false;
        this.isTitle = false;
        this.isHovered = false;
        this.hasPastAppointment = false;
        this.hasFutureAppointment = false;
        this.isAvailableDay = false;

        setContentAreaFilled(false);
        setOpaque(false);
        setBorder(null);
        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Adds hover effect to change appearance when the mouse enters/exits
    private void addHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isTitle) {
                    isHovered = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isTitle) {
                    isHovered = false;
                    setCursor(Cursor.getDefaultCursor());
                    repaint();
                }
            }
        });
    }

    // Marks this cell as a title cell (day of the week)
    public void maskAsTitleCell() {
        this.isTitle = true;
        setForeground(MAIN_APP_COLOUR);
        setFont(DAYS_OF_THE_MONTH_FONT);
    }

    // Marks this cell if it represents a past or future appointment
    public void markAsAppointmentDay(boolean isFuture) {
        if (isFuture) {
            hasFutureAppointment = true;
        } else {
            hasPastAppointment = true;
        }
        repaint();
    }

    // Marks this cell if it represents a day where the doctor is available
    public void markAsAvailableDay() {
        this.isAvailableDay = true;
        repaint();
    }

    // Marks this cell if it represents today's date
    public void markAsToday() {
        this.isToday = true;
        repaint();
    }

    // Sets the date associated with this cell
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Returns the date associated with this cell
    public LocalDate getDate() {
        return date;
    }

    // Updates the appearance depending on whether the cell is in the current month
    public void currentMonth(boolean isCurrentMonth) {
        setForeground(isCurrentMonth ? Color.BLACK : new Color(169, 169, 169));
        setFont(DAY_NUMBER_FONT);
    }

    public void resetCell() {
        this.isToday = false;
        this.isHovered = false;
        this.hasPastAppointment = false;
        this.hasFutureAppointment = false;
        this.isAvailableDay = false;
        removeAllActionListeners();
    }

    private void removeAllActionListeners() {
        for (ActionListener al : getActionListeners()) {
            removeActionListener(al);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = prepareGraphics(g);

        drawHoverEffect(g2);
        drawTitleUnderline(g2);
        drawHighlight(g2);

        super.paintComponent(g);
    }

    private Graphics2D prepareGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }

    // Draws a hover effect when the mouse is over the cell
    private void drawHoverEffect(Graphics2D g2) {
        if (isHovered) {
            g2.setColor(new Color(230, 230, 230));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Draws an underline if the cell is a title cell (day of the week)
    private void drawTitleUnderline(Graphics2D g2) {
        if (isTitle) {
            g2.setColor(MAIN_APP_COLOUR);
            g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
    }

    // Draws highlights for today and appointment days
    private void drawHighlight(Graphics2D g2) {
        int x = getWidth() / 2 - 17;
        int y = getHeight() / 2 - 17;
        int size = 35;

        if (isToday) {
            drawRoundedHighlight(g2, SECONDARY_APP_COLOUR, x, y, size);
        }
        if (hasPastAppointment) {
            drawRoundedHighlight(g2, MY_RED, x, y, size);
        }
        if (hasFutureAppointment) {
            drawRoundedHighlight(g2, MAIN_APP_COLOUR, x, y, size);
        }
        if (isAvailableDay) {
            drawRoundedHighlight(g2, new Color(255, 255, 102, 150), x, y, size);
        }
    }

    // Draws a rounded rectangle highlight (used for today & appointments)
    private void drawRoundedHighlight(Graphics2D g2, Color color, int x, int y, int size) {
        g2.setColor(color);
        g2.fillRoundRect(x, y, size, size, 400, 400);
    }
}
