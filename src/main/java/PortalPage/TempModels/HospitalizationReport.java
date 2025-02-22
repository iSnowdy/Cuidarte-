package PortalPage.TempModels;

import java.util.Date;

public class HospitalizationReport {
    private int id;
    private TestPatient testPatient;
    private Date admissionDate;
    private Date dischargeDate;
    private String diagnosis;
    private String procedures;
    private String notes;

    public HospitalizationReport(int id, TestPatient testPatient, Date admissionDate, Date dischargeDate, String diagnosis, String procedures, String notes) {
        this.id = id;
        this.testPatient = testPatient;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.diagnosis = diagnosis;
        this.procedures = procedures;
        this.notes = notes;
    }

    public int getId() { return id; }
    public TestPatient getTestPatient() { return testPatient; }
    public Date getAdmissionDate() { return admissionDate; }
    public Date getDischargeDate() { return dischargeDate; }
    public String getDiagnosis() { return diagnosis; }
    public String getProcedures() { return procedures; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        // Muestra la fecha de ingreso y el diagnóstico para el listado
        return String.format("%1$td/%1$tm/%1$tY - %2$s", admissionDate, diagnosis);
    }
}
