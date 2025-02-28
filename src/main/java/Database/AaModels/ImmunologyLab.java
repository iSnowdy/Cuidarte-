package Database.AaModels;

public class ImmunologyLab {
    private int
            immunologyId, testId;
    private int
            tcd4Lymphocytes, tcd4Lymphocytescount, tcd8Lymphocytes, tcd8Lymphocytescount;
    private double cd4Cd8Ratio;

    public ImmunologyLab(int immunologyId, int testId, int tcd4Lymphocytes, int tcd4Lymphocytescount, int tcd8Lymphocytes, int tcd8Lymphocytescount, double cd4Cd8Ratio) {
        this.immunologyId = immunologyId;
        this.testId = testId;
        this.tcd4Lymphocytes = tcd4Lymphocytes;
        this.tcd4Lymphocytescount = tcd4Lymphocytescount;
        this.tcd8Lymphocytes = tcd8Lymphocytes;
        this.tcd8Lymphocytescount = tcd8Lymphocytescount;
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

    public int getTcd4Lymphocytes() {
        return tcd4Lymphocytes;
    }

    public void setTcd4Lymphocytes(int tcd4Lymphocytes) {
        this.tcd4Lymphocytes = tcd4Lymphocytes;
    }

    public int getTcd4Lymphocytescount() {
        return tcd4Lymphocytescount;
    }

    public void setTcd4Lymphocytescount(int tcd4Lymphocytescount) {
        this.tcd4Lymphocytescount = tcd4Lymphocytescount;
    }

    public int getTcd8Lymphocytes() {
        return tcd8Lymphocytes;
    }

    public void setTcd8Lymphocytes(int tcd8Lymphocytes) {
        this.tcd8Lymphocytes = tcd8Lymphocytes;
    }

    public int getTcd8Lymphocytescount() {
        return tcd8Lymphocytescount;
    }

    public void setTcd8Lymphocytescount(int tcd8Lymphocytescount) {
        this.tcd8Lymphocytescount = tcd8Lymphocytescount;
    }

    public double getCd4Cd8Ratio() {
        return cd4Cd8Ratio;
    }

    public void setCd4Cd8Ratio(double cd4Cd8Ratio) {
        this.cd4Cd8Ratio = cd4Cd8Ratio;
    }
}
