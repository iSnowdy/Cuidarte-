package Database.Models;

import java.sql.Date;
import java.sql.Time;

public class DoctorAvailability {
    private int doctorAvailabilityID;
    private int clinicID;
    private String doctorDNI;
    private Date date;
    private Time startTime;
    private Time endTime;
    private int appointmentDuration;

    public DoctorAvailability(String doctorDNI, int clinicID, Date date, Time startTime, Time endTime, int appointmentDuration) {
        this.doctorDNI = doctorDNI;
        this.clinicID = clinicID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentDuration = appointmentDuration;
    }

    // Maps data from the DB to the DTO
    public DoctorAvailability(int doctorAvailabilityID, String doctorDNI, int clinicID, Date date, Time startTime, Time endTime, int appointmentDuration) {
        this.doctorAvailabilityID = doctorAvailabilityID;
        this.clinicID = clinicID;
        this.doctorDNI = doctorDNI;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentDuration = appointmentDuration;
    }

    // Getters and Setters
    public int getDoctorAvailabilityID() {
        return doctorAvailabilityID;
    }
    public void setDoctorAvailabilityID(int doctorAvailabilityID) {
        this.doctorAvailabilityID = doctorAvailabilityID;
    }

    public int getClinicID() {
        return clinicID;
    }
    public void setClinicID(int clinicID) {
        this.clinicID = clinicID;
    }

    public String getDoctorDNI() {
        return doctorDNI;
    }
    public void setDoctorDNI(String doctorDNI) {
        this.doctorDNI = doctorDNI;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
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

    public int getAppointmentDuration() {
        return appointmentDuration;
    }
    public void setAppointmentDuration(int appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }
}
