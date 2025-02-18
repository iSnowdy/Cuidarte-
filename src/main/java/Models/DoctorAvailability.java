package Models;

import Models.Enums.DayOfTheWeek;

import java.sql.Time;

public class DoctorAvailability {
    private int id;
    private String doctorDNI;
    private DayOfTheWeek[] availableDays;
    private Time
            startTime, endTime, appointmentDuration;

    public DoctorAvailability(String doctorDNI, DayOfTheWeek[] availableDays, Time startTime, Time endTime, Time appointmentDuration) {
        this.doctorDNI = doctorDNI;
        this.availableDays = availableDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentDuration = appointmentDuration;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDoctor() {
        return doctorDNI;
    }
    public void setDoctor(Doctor doctor) {
        this.doctorDNI = doctorDNI;
    }

    public DayOfTheWeek[] getAvailableDays() {
        return availableDays;
    }
    public void setAvailableDays(DayOfTheWeek[] availableDays) {
        this.availableDays = availableDays;
    }

    public Time getStartTime() {
        return startTime;
    }
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Time getAppointmentDuration() {
        return appointmentDuration;
    }
    public void setAppointmentDuration(Time appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }
}
