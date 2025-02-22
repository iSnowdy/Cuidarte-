package Calendar.Main;

import java.time.LocalDate;

public class TemporaryAppointment {
    private String doctor;
    private LocalDate date;
    private String time;

    public TemporaryAppointment(String doctor, LocalDate date, String time) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }


    @Override
    public String toString() {
        return "Cita con " + doctor + " el " + date + " a las " + time;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
