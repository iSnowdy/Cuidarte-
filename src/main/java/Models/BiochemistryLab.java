package Models;

public class BiochemistryLab {
    private int
            biochemistryID, diagnosticTestID;
    private int
            glucose,
            urea,
            totalCholesterol, hdlCholesterol, ldlCholesterol, triglycerides,
            gotAst, gptAlt, gammaGt,
            alkalinePhosphatase;
    private double
            creatinine,
            uricAcid, totalBilirubin;


    public BiochemistryLab(int biochemistryId, int testId, int glucose, int urea, double creatinine, double uricAcid, int totalCholesterol, int hdlCholesterol, int ldlCholesterol, int triglycerides, int gotAst, int gptAlt, int gammaGt, int alkalinePhosphatase, double totalBilirubin) {
        this.biochemistryID = biochemistryId;
        this.diagnosticTestID = testId;
        this.glucose = glucose;
        this.urea = urea;
        this.creatinine = creatinine;
        this.uricAcid = uricAcid;
        this.totalCholesterol = totalCholesterol;
        this.hdlCholesterol = hdlCholesterol;
        this.ldlCholesterol = ldlCholesterol;
        this.triglycerides = triglycerides;
        this.gotAst = gotAst;
        this.gptAlt = gptAlt;
        this.gammaGt = gammaGt;
        this.alkalinePhosphatase = alkalinePhosphatase;
        this.totalBilirubin = totalBilirubin;
    }


    // Getters and Setters
    public int getBiochemistryID() {
        return biochemistryID;
    }

    public void setBiochemistryID(int biochemistryID) {
        this.biochemistryID = biochemistryID;
    }

    public int getDiagnosticTestID() {
        return diagnosticTestID;
    }

    public void setDiagnosticTestID(int diagnosticTestID) {
        this.diagnosticTestID = diagnosticTestID;
    }

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public int getUrea() {
        return urea;
    }

    public void setUrea(int urea) {
        this.urea = urea;
    }

    public int getTotalCholesterol() {
        return totalCholesterol;
    }

    public void setTotalCholesterol(int totalCholesterol) {
        this.totalCholesterol = totalCholesterol;
    }

    public int getHdlCholesterol() {
        return hdlCholesterol;
    }

    public void setHdlCholesterol(int hdlCholesterol) {
        this.hdlCholesterol = hdlCholesterol;
    }

    public int getLdlCholesterol() {
        return ldlCholesterol;
    }

    public void setLdlCholesterol(int ldlCholesterol) {
        this.ldlCholesterol = ldlCholesterol;
    }

    public int getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(int triglycerides) {
        this.triglycerides = triglycerides;
    }

    public int getGotAst() {
        return gotAst;
    }

    public void setGotAst(int gotAst) {
        this.gotAst = gotAst;
    }

    public int getGptAlt() {
        return gptAlt;
    }

    public void setGptAlt(int gptAlt) {
        this.gptAlt = gptAlt;
    }

    public int getGammaGt() {
        return gammaGt;
    }

    public void setGammaGt(int gammaGt) {
        this.gammaGt = gammaGt;
    }

    public int getAlkalinePhosphatase() {
        return alkalinePhosphatase;
    }

    public void setAlkalinePhosphatase(int alkalinePhosphatase) {
        this.alkalinePhosphatase = alkalinePhosphatase;
    }

    public double getCreatinine() {
        return creatinine;
    }

    public void setCreatinine(double creatinine) {
        this.creatinine = creatinine;
    }

    public double getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(double uricAcid) {
        this.uricAcid = uricAcid;
    }

    public double getTotalBilirubin() {
        return totalBilirubin;
    }

    public void setTotalBilirubin(double totalBilirubin) {
        this.totalBilirubin = totalBilirubin;
    }
}

