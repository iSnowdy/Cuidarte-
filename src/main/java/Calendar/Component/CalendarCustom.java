package Calendar.Component;



import Calendar.Swing.PanelSlide;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarCustom extends JPanel {
    private int month;
    private int year;

    private JLabel timerLabel;
    private JLabel typeLabel;
    private JLabel dateLabel;
    private JLabel monthYearLabel;
    private PanelSlide slide;
    private JButton backButton;
    private JButton nextButton;
    private Timer clockTimer;

    private ImageIconRedrawer iconRedrawer;

    public CalendarCustom() {
        this.iconRedrawer = new ImageIconRedrawer();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Initializes to current date
        thisMonth();

        initHeaderPanel();
        initMonthPanel();
        initCalendarPanel();
        initClockUpdater();
    }

    // TODO: Styling
    private void initHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(97, 49, 237));

        timerLabel = new JLabel();
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        timerLabel.setForeground(new Color(201, 201, 201));

        typeLabel = new JLabel();
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        typeLabel.setForeground(new Color(201, 201, 201));

        dateLabel = new JLabel();
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dateLabel.setForeground(new Color(201, 201, 201));

        headerPanel.add(timerLabel);
        headerPanel.add(typeLabel);
        headerPanel.add(dateLabel);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void initMonthPanel() {
        JPanel monthPanel = new JPanel(new BorderLayout());


        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/previous.png")));
        ImageIcon backIcon = iconRedrawer.redrawImageIcon(30, 30);
        backButton = new JButton();
        backButton.setIcon(backIcon);
        styleNavigationButton(backButton);
        backButton.addActionListener(e -> goToPreviousMonth());

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/next.png")));
        ImageIcon nextIcon = iconRedrawer.redrawImageIcon(30, 30);
        nextButton = new JButton();
        nextButton.setIcon(nextIcon);
        styleNavigationButton(nextButton);
        nextButton.addActionListener(e -> goToNextMonth());

        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        monthYearLabel.setForeground(new Color(97, 49, 237));

        monthPanel.add(backButton, BorderLayout.WEST);
        monthPanel.add(monthYearLabel, BorderLayout.CENTER);
        monthPanel.add(nextButton, BorderLayout.EAST);
        add(monthPanel, BorderLayout.CENTER);
    }

    // The Panel that slides
    private void initCalendarPanel() {
        slide = new PanelSlide();
        slide.setBackground(Color.WHITE);
        add(slide, BorderLayout.SOUTH);

        slide.show(new PanelDate(month, year), PanelSlide.AnimationType.TO_RIGHT);
        showMonthYear();
    }

    private void styleNavigationButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initClockUpdater() {
        clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();
    }

    private void thisMonth() {
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    // Updates date
    private void showMonthYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
        monthYearLabel.setText(df.format(calendar.getTime()));
    }

    private void goToPreviousMonth() {
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        slide.removeAll();
        PanelDate panelDate = new PanelDate(month, year);
        slide.show(panelDate, PanelSlide.AnimationType.TO_RIGHT);
        showMonthYear();

        revalidate();
        repaint();
    }

    private void goToNextMonth() {
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }
        slide.removeAll();
        PanelDate panelDate = new PanelDate(month, year);
        slide.show(panelDate, PanelSlide.AnimationType.TO_LEFT);
        showMonthYear();

        revalidate();
        repaint();
    }

    private void updateClock() {
        Date date = new Date();
        SimpleDateFormat tf = new SimpleDateFormat("h:mm:ss aa");
        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd/MM/yyyy");

        String time = tf.format(date);
        timerLabel.setText(time.split(" ")[0]);
        typeLabel.setText(time.split(" ")[1]);
        dateLabel.setText(df.format(date));
    }
}
