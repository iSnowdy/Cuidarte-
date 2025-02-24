package PortalPage.TempModels;

import Models.Patient;

import java.util.Date;

public class ClinicalHistory {
    private int id;
    private Models.Patient patient;
    private String details;
    private String doctorName;
    private String specialty;
    private Date date;

    public ClinicalHistory(int id, Models.Patient patient, String details, String doctorName, String specialty, Date date) {
        this.id = id;
        this.patient = patient;
        this.details = details;
        this.doctorName = doctorName;
        this.specialty = specialty;
        this.date = date;
    }

    public int getId() { return id; }
    public Patient getTestPatient() { return patient; }
    public String getDetails() { return details; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialty() { return specialty; }
    public Date getDate() { return date; }

    @Override
    public String toString() {
        // Example: "10/01/2024 - Dr. García (Cardiología)"
        return String.format("%1$td/%1$tm/%1$tY - %2$s (%3$s)", date, doctorName, specialty);
    }
}
