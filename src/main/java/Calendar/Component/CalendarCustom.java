package Calendar.Component;

import Calendar.Swing.PanelSlide;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;

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
    private JPanel leftPanel;
    private JPanel rightPanel;

    public CalendarCustom() {
        this.iconRedrawer = new ImageIconRedrawer();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        thisMonth();

        initLeftPanel();  // Left panel that will contain critical information and appointment selection
        initRightPanel(); // Right panel containing the calendar

        // Split line between both panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(250); // Left panel size
        splitPane.setResizeWeight(0.3); // At least 30%
        splitPane.setEnabled(false); // Prevents user from moving panels

        add(splitPane, BorderLayout.CENTER);

        initClockUpdater();
    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(SECONDARY_APP_COLOUR);

        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBackground(SECONDARY_APP_COLOUR);

        timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        timerLabel.setForeground(Color.WHITE);

        typeLabel = new JLabel("", SwingConstants.CENTER);
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        typeLabel.setForeground(Color.WHITE);

        dateLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dateLabel.setForeground(Color.WHITE);

        headerPanel.add(timerLabel);
        headerPanel.add(typeLabel);
        headerPanel.add(dateLabel);

        leftPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());

        JPanel monthPanel = new JPanel(new BorderLayout());

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/previous.png")));
        ImageIcon backIcon = iconRedrawer.redrawImageIcon(20, 20);
        backButton = new JButton();
        backButton.setIcon(backIcon);
        styleNavigationButton(backButton);
        backButton.addActionListener(e -> goToPreviousMonth());

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource("/LandingPage/next.png")));
        ImageIcon nextIcon = iconRedrawer.redrawImageIcon(20, 20);
        nextButton = new JButton();
        nextButton.setIcon(nextIcon);
        styleNavigationButton(nextButton);
        nextButton.addActionListener(e -> goToNextMonth());

        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        monthYearLabel.setForeground(SECONDARY_APP_COLOUR);

        monthPanel.add(backButton, BorderLayout.WEST);
        monthPanel.add(monthYearLabel, BorderLayout.CENTER);
        monthPanel.add(nextButton, BorderLayout.EAST);
        rightPanel.add(monthPanel, BorderLayout.NORTH);

        slide = new PanelSlide();
        slide.setBackground(Color.WHITE);
        rightPanel.add(slide, BorderLayout.CENTER);

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

    // TODO: To Spanish?
    private void showMonthYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
        System.out.println("Month - year label: " + df.format(calendar.getTime()));
        monthYearLabel.setText(df.format(calendar.getTime()));
    }

    private void goToPreviousMonth() {
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        showMonthYear();
        PanelDate panelDate = new PanelDate(month, year);
        panelDate.setSize(slide.getSize());
        slide.show(panelDate, PanelSlide.AnimationType.TO_RIGHT);
    }

    private void goToNextMonth() {
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }
        showMonthYear();

        PanelDate panelDate = new PanelDate(month, year);
        panelDate.setSize(slide.getSize());
        slide.show(panelDate, PanelSlide.AnimationType.TO_LEFT);
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
