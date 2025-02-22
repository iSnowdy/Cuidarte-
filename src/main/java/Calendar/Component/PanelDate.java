package Calendar.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class PanelDate extends JPanel {
    private int month;
    private int year;
    private List<Integer> availableDays; // Available days for the selected doctor
    private final List<Cell> cellsList;
    private Consumer<LocalDate> onDateSelected; // Callback for notifying selection
    private Consumer<LocalDate> onAppointmentSelected; // To manage clicks on Cells
    private Set<LocalDate> appointmentDates;

    public PanelDate(int month, int year, List<Integer> availableDays, Consumer<LocalDate> onDateSelected, Set<LocalDate> appointmentDates, Consumer<LocalDate> onAppointmentClick) {
        this.month = month;
        this.year = year;
        this.availableDays = availableDays;
        this.onDateSelected = onDateSelected;
        this.appointmentDates = appointmentDates;
        this.onAppointmentSelected = onAppointmentClick;
        this.cellsList = new ArrayList<>();

        setLayout(new GridLayout(7, 7, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        setDate();
        setPreferredSize(new Dimension(350, 300));
    }

    // Initializes the grid structure
    private void initComponents() {
        addWeekdayHeaders();
        addDateCells();
    }

    private void addWeekdayHeaders() {
        String[] days = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        for (String day : days) {
            Cell cell = new Cell();
            cell.setText(day);
            cell.maskAsTitleCell();
            add(cell);
        }
    }

    // Creates date cells
    private void addDateCells() {
        for (int i = 0; i < 42; i++) {
            Cell cell = new Cell();
            cellsList.add(cell);
            add(cell);
        }
    }

    // Populates the calendar grid with dates
    private void setDate() {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        LocalDate startDate = firstDayOfMonth.minusDays(startDay);
        LocalDate today = LocalDate.now();

        for (Cell cell : cellsList) {
            LocalDate finalDate = startDate;
            cell.setText(String.valueOf(finalDate.getDayOfMonth()));
            cell.setDate(finalDate);
            cell.currentMonth(finalDate.getMonthValue() == month);

            if (appointmentDates.contains(finalDate)) {
                handleAppointmentCell(cell, finalDate, today);
            } else {
                handleAvailableCell(cell, finalDate);
            }

            if (finalDate.equals(today)) {
                cell.setAsToday();
            }

            startDate = startDate.plusDays(1);
        }

        revalidate();
        repaint();
    }

    // Handles UI behavior for appointment days
    private void handleAppointmentCell(Cell cell, LocalDate date, LocalDate today) {
        boolean isFuture = date.isAfter(today);
        cell.markAsAppointmentDay(isFuture);
        removeExistingListeners(cell);
        cell.addActionListener(e -> onAppointmentSelected.accept(date));
    }

    // Handles UI behavior for available days
    private void handleAvailableCell(Cell cell, LocalDate date) {
        boolean isAvailable = date.getMonthValue() == month && (availableDays != null && availableDays.contains(date.getDayOfMonth()));
        cell.setEnabled(isAvailable);
        cell.setForeground(isAvailable ? Color.BLACK : Color.LIGHT_GRAY);

        if (isAvailable) {
            cell.addActionListener(e -> onDateSelected.accept(date));
        }
    }

    // Removes existing listeners to prevent duplicates
    private void removeExistingListeners(Cell cell) {
        for (ActionListener al : cell.getActionListeners()) {
            cell.removeActionListener(al);
        }
    }

    public void refreshDate() {
        setDate();
    }
}
