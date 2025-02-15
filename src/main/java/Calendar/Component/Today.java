package Calendar.Component;

import java.util.Calendar;

public class Today {
    private int year;
    private int month;
    private int day;

    public Today() {
        updateToday();
    }

    public Today(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public boolean isToday(Today dateToCompare) {
        return this.day == dateToCompare.getDay() &&
                this.month == dateToCompare.getMonth() &&
                this.year == dateToCompare.getYear();
    }

    public void updateToday() {
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // Getters y Setters
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
}
