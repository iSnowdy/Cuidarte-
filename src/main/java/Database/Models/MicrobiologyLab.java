package Database.Models;

public class MicrobiologyLab {
    private int
            microbiologyId, testId;
    private String result;

    public MicrobiologyLab(int microbiologyId, int testId, String result) {
        this.microbiologyId = microbiologyId;
        this.testId = testId;
        this.result = result;
    }

    // Getters and Setters
    public int getMicrobiologyId() {
        return microbiologyId;
    }
    public void setMicrobiologyId(int microbiologyId) {
        this.microbiologyId = microbiologyId;
    }

    public int getTestId() {
        return testId;
    }
    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
