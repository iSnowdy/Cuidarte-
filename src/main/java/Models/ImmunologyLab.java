package Models;

public class ImmunologyLab {
    private int
            immunologyId, testId;
    private int
            tCD4Lymphocytes, tCD4LymphocytesCount, tCD8Lymphocytes, tCD8LymphocytesCount;
    private double cd4Cd8Ratio;

    public ImmunologyLab(int immunologyId, int testId, int tCD4Lymphocytes, int tCD4LymphocytesCount, int tCD8Lymphocytes, int tCD8LymphocytesCount, double cd4Cd8Ratio) {
        this.immunologyId = immunologyId;
        this.testId = testId;
        this.tCD4Lymphocytes = tCD4Lymphocytes;
        this.tCD4LymphocytesCount = tCD4LymphocytesCount;
        this.tCD8Lymphocytes = tCD8Lymphocytes;
        this.tCD8LymphocytesCount = tCD8LymphocytesCount;
        this.cd4Cd8Ratio = cd4Cd8Ratio;
    }

    // Getters and Setters
    public int getImmunologyId() {
        return immunologyId;
    }

    public void setImmunologyId(int immunologyId) {
        this.immunologyId = immunologyId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int gettCD4Lymphocytes() {
        return tCD4Lymphocytes;
    }

    public void settCD4Lymphocytes(int tCD4Lymphocytes) {
        this.tCD4Lymphocytes = tCD4Lymphocytes;
    }

    public int gettCD4LymphocytesCount() {
        return tCD4LymphocytesCount;
    }

    public void settCD4LymphocytesCount(int tCD4LymphocytesCount) {
        this.tCD4LymphocytesCount = tCD4LymphocytesCount;
    }

    public int gettCD8Lymphocytes() {
        return tCD8Lymphocytes;
    }

    public void settCD8Lymphocytes(int tCD8Lymphocytes) {
        this.tCD8Lymphocytes = tCD8Lymphocytes;
    }

    public int gettCD8LymphocytesCount() {
        return tCD8LymphocytesCount;
    }

    public void settCD8LymphocytesCount(int tCD8LymphocytesCount) {
        this.tCD8LymphocytesCount = tCD8LymphocytesCount;
    }

    public double getCd4Cd8Ratio() {
        return cd4Cd8Ratio;
    }

    public void setCd4Cd8Ratio(double cd4Cd8Ratio) {
        this.cd4Cd8Ratio = cd4Cd8Ratio;
    }
}
