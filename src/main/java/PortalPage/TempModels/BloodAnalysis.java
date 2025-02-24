package PortalPage.TempModels;

import java.util.Date;

public class BloodAnalysis {
    private int id;
    private Patient patient;
    private String testResults;
    private String testType;
    private Date testDate;

    public BloodAnalysis(int id, Patient patient, String testResults, String testType, Date testDate) {
        this.id = id;
        this.patient = patient;
        this.testResults = testResults;
        this.testType = testType;
        this.testDate = testDate;
    }

    public int getId() { return id; }
    public Patient getTestPatient() { return patient; }
    public String getTestResults() { return testResults; }
    public String getTestType() { return testType; }
    public Date getTestDate() { return testDate; }

    @Override
    public String toString() {
        // Display date and test type for the list
        return String.format("%1$td/%1$tm/%1$tY - %2$s", testDate, testType);
    }
}
