package Database.Models;

import java.sql.Timestamp;

public class MedicalReport {
    private int id;
    private String
            patientDNI, doctorDNI,
            medicalHistory, diagnosis, allergies, appointmentMotive, physicalExploration, treatment;
    private Timestamp visitDate;
    private float
            temperature, weight, height;
    private int systolic, diastolic, heartRate, saturation;

    public MedicalReport(String patientDNI, String doctorDNI,
                         String medicalHistory, String allergies, String appointmentMotive, String physicalExploration, String treatment,
                         int systolic, int diastolic, int heartRate, int saturation,
                         float temperature, float weight, float height,
                         Timestamp visitDate) {
        this.patientDNI = patientDNI;
        this.doctorDNI = doctorDNI;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
        this.appointmentMotive = appointmentMotive;
        this.physicalExploration = physicalExploration;
        this.treatment = treatment;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.heartRate = heartRate;
        this.saturation = saturation;
        this.temperature = temperature;
        this.weight = weight;
        this.height = height;
        this.visitDate = visitDate;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPatientDNI() {
        return patientDNI;
    }
    public void setPatientDNI(String patientDNI) {
        this.patientDNI = patientDNI;
    }

    public String getDoctorDNI() {
        return doctorDNI;
    }
    public void setDoctorDNI(String doctorDNI) {
        this.doctorDNI = doctorDNI;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getAllergies() {
        return allergies;
    }
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getAppointmentMotive() {
        return appointmentMotive;
    }
    public void setAppointmentMotive(String appointmentMotive) {
        this.appointmentMotive = appointmentMotive;
    }

    public String getPhysicalExploration() {
        return physicalExploration;
    }
    public void setPhysicalExploration(String physicalExploration) {
        this.physicalExploration = physicalExploration;
    }

    public String getTreatment() {
        return treatment;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Timestamp getVisitDate() {
        return visitDate;
    }
    public void setVisitDate(Timestamp visitDate) {
        this.visitDate = visitDate;
    }

    public float getTemperature() {
        return temperature;
    }
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }

    public int getSystolic() {
        return systolic;
    }
    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }
    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getSaturation() {
        return saturation;
    }
    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
