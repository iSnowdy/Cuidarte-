package Models;

import Models.Enums.AppointmentState;

import java.sql.Date;

public class Appointment {
    private int id;
    private String doctorDNI, patientDNI;
    private int clinicID;
    private Date appointmentDate;
    private AppointmentState appointmentState;
    private String description;
    private String doctorObservations;

    public Appointment(String doctorDNI, String patientDNI, int clinicID, Date appointmentDate, String description) {
        this.doctorDNI = doctorDNI;
        this.patientDNI = patientDNI;
        this.clinicID = clinicID;
        this.appointmentDate = appointmentDate;
        this.description = description;
        this.appointmentState = AppointmentState.PENDING;
    }



    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorDNI() {
        return doctorDNI;
    }
    public void setDoctorDNI(String doctorDNI) {
        this.doctorDNI = doctorDNI;
    }

    public String getPatientDNI() {
        return patientDNI;
    }
    public void setPatientDNI(String patientDNI) {
        this.patientDNI = patientDNI;
    }

    public int getClinicID() {
        return clinicID;
    }
    public void setClinicID(int clinicID) {
        this.clinicID = clinicID;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentState getAppointmentState() {
        return appointmentState;
    }
    public void setAppointmentState(AppointmentState appointmentState) {
        this.appointmentState = appointmentState;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctorObservations() {
        return doctorObservations;
    }
    public void setDoctorObservations(String doctorObservations) {
        this.doctorObservations = doctorObservations;
    }
}
