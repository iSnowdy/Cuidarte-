package Calendar.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PanelDate extends JPanel {
    private int month;
    private int year;
    private final List<Cell> cellsList;

    public PanelDate(int month, int year) {
        this.cellsList = new ArrayList<Cell>();
        this.month = month;
        this.year = year;

        // 7x7 ---> 7 columns (days), 7 rows (days of the month)
        setLayout(new GridLayout(7, 7));
        initComponents();
        setDate();
    }

    private void initComponents() {
        String[] days = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        for (String day : days) {
            Cell cell = new Cell();
            cell.setText(day);
            cell.maskAsTitleCell(); // Flag to know it is a title cell
            add(cell);
        }

        // One month can have up to 6 weeks showing at once (4 of it, 1 prev, 1 next). So 42 days
        for (int i = 0; i < 42; i++) {
            Cell cell = new Cell();
            cellsList.add(cell);
            add(cell);
        }
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);

        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1; // index adjustment
        calendar.add(Calendar.DATE, -startDay); // So that the first Sunday is the Sunday before the 1st of the month

        Today today = new Today();
        today.updateToday();

        for (Cell cell : cellsList) {
            cell.setText(String.valueOf(calendar.get(Calendar.DATE))); // Day of the day
            cell.setDate(calendar.getTime());
            cell.currentMonth(calendar.get(Calendar.MONTH) == month - 1);

            if (today.isToday(new Today(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)))) {
                cell.setAsToday(); // Marks the cell as if it's today
            }

            calendar.add(Calendar.DATE, 1);
        }

        revalidate();
        repaint();
    }
}
