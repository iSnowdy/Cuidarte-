package Models;

import Models.Enums.TestType;

import java.sql.Date;

public class DiagnosticTest {
    private int
            diagnosticTestID, medicalReportID;
    private TestType testType;
    private Date testDate;
    private String results;

    public DiagnosticTest(int id, int medicalReportID, TestType testType, Date testDate, String results) {
        this.diagnosticTestID = id;
        this.medicalReportID = medicalReportID;
        this.testType = testType;
        this.testDate = testDate;
        this.results = results;
    }


    // Getters and Setters
    public int getDiagnosticTestID() {
        return diagnosticTestID;
    }
    public void setDiagnosticTestID(int diagnosticTestID) {
        this.diagnosticTestID = diagnosticTestID;
    }

    public int getMedicalReportID() {
        return medicalReportID;
    }
    public void setMedicalReportID(int medicalReportID) {
        this.medicalReportID = medicalReportID;
    }

    public TestType getTestType() {
        return testType;
    }
    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public Date getTestDate() {
        return testDate;
    }
    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public String getResults() {
        return results;
    }
    public void setResults(String results) {
        this.results = results;
    }
}
