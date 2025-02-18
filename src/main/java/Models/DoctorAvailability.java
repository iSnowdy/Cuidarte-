package Models;

import Models.Enums.DayOfTheWeek;

import java.sql.Time;
import java.util.List;

public class DoctorAvailability {
    private int doctorAvailabilityID;
    private int clinicID;
    private String doctorDNI;
    private List<DayOfTheWeek> availableDays;
    private Time startTime;
    private Time endTime;
    private int appointmentDuration;

    public DoctorAvailability(String doctorDNI, int clinicID, List<DayOfTheWeek> availableDays, Time startTime, Time endTime, int appointmentDuration) {
        this.doctorDNI = doctorDNI;
        this.clinicID = clinicID;
        this.availableDays = availableDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentDuration = appointmentDuration;
    }

    // Constructor needed to map a ResultSet -> DoctorAvailability and relationship with days
    public DoctorAvailability(int doctorAvailabilityID,  String doctorDNI, int clinicID, List<DayOfTheWeek> availableDays, Time startTime, Time endTime, int appointmentDuration) {
        this.doctorAvailabilityID = doctorAvailabilityID;
        this.clinicID = clinicID;
        this.doctorDNI = doctorDNI;
        this.availableDays = availableDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentDuration = appointmentDuration;
    }

    // Getters y Setters
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

    public List<DayOfTheWeek> getAvailableDays() {
        return availableDays;
    }
    public void setAvailableDays(List<DayOfTheWeek> availableDays) {
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

    public int getAppointmentDuration() {
        return appointmentDuration;
    }
    public void setAppointmentDuration(int appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }
}
