package PortalPage.TempModels;

import Models.Enums.TestType;
import Models.Patient;

import java.util.Date;

public class DiagnosticTest {
    private int id;
    private Models.Patient patient;
    private String testResults;
    private TestType testType;
    private Date testDate;

    public DiagnosticTest(int id, Models.Patient patient, String testResults, TestType testType, Date testDate) {
        this.id = id;
        this.patient = patient;
        this.testResults = testResults;
        this.testType = testType;
        this.testDate = testDate;
    }

    public int getId() { return id; }
    public Patient getTestPatient() { return patient; }
    public String getTestResults() { return testResults; }
    public TestType getTestType() { return testType; }
    public Date getTestDate() { return testDate; }

    @Override
    public String toString() {
        // Display date and test type in the list
        return String.format("%1$td/%1$tm/%1$tY - %2$s", testDate, testType.getValue());
    }
}
