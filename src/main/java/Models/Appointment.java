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

    public Appointment(String patientDNI, String doctorDNI, int clinicID, Date appointmentDate, String description) {
        this.patientDNI = patientDNI;
        this.doctorDNI = doctorDNI;
        this.clinicID = clinicID;
        this.appointmentDate = appointmentDate;
        this.description = description;
        this.appointmentState = AppointmentState.PENDING;
    }

    // Constructors needed to map a ResultSet -> Appointment
    public Appointment(int id, String patientDNI, String doctorDNI,  int clinicID, Date appointmentDate, AppointmentState appointmentState, String description) {
        this.id = id;
        this.patientDNI = patientDNI;
        this.doctorDNI = doctorDNI;
        this.clinicID = clinicID;
        this.appointmentDate = appointmentDate;
        this.appointmentState = appointmentState;
        this.description = description;
    }

    public Appointment(int id, String patientDNI, String doctorDNI,  int clinicID, Date appointmentDate, AppointmentState appointmentState, String description, String doctorObservations) {
        this.id = id;
        this.patientDNI = patientDNI;
        this.doctorDNI = doctorDNI;
        this.clinicID = clinicID;
        this.appointmentDate = appointmentDate;
        this.appointmentState = appointmentState;
        this.description = description;
        this.doctorObservations = doctorObservations;
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
