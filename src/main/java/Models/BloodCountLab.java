package Models;

public class BloodCountLab {
    private int
            bloodCountID, diagnosticTestID;
    private int platelets;
    private double
            erythrocytes, hemoglobin, hematocrit,
            vcm, hcm, chcm, ade, vpm, adp,
            plateletcrit, leukocytes, neutrophils, lymphocytes, monocytes, eosinophils, basophils;

    public BloodCountLab(int idHemograma, int idPrueba,
                         double redBloodCells, double hemoglobina, double hematocrito,
                         double vcm, double hcm, double chcm, double ade, int plaquetas, double vpm, double adp,
                         double plaquetocrito, double leucocitos, double neutrofilos, double linfocitos,
                         double monocitos, double eosinofilos, double basofilos) {
        this.bloodCountID = idHemograma;
        this.diagnosticTestID = idPrueba;
        this.erythrocytes = redBloodCells;
        this.hemoglobin = hemoglobina;
        this.hematocrit = hematocrito;
        this.vcm = vcm;
        this.hcm = hcm;
        this.chcm = chcm;
        this.ade = ade;
        this.platelets = plaquetas;
        this.vpm = vpm;
        this.adp = adp;
        this.plateletcrit = plaquetocrito;
        this.leukocytes = leucocitos;
        this.neutrophils = neutrofilos;
        this.lymphocytes = linfocitos;
        this.monocytes = monocitos;
        this.eosinophils = eosinofilos;
        this.basophils = basofilos;
    }


    // Getters y Setters
    public int getBloodCountID() {
        return bloodCountID;
    }

    public void setBloodCountID(int bloodCountID) {
        this.bloodCountID = bloodCountID;
    }

    public int getDiagnosticTestID() {
        return diagnosticTestID;
    }

    public void setDiagnosticTestID(int diagnosticTestID) {
        this.diagnosticTestID = diagnosticTestID;
    }

    public int getPlatelets() {
        return platelets;
    }

    public void setPlatelets(int platelets) {
        this.platelets = platelets;
    }

    public double getErythrocytes() {
        return erythrocytes;
    }

    public void setErythrocytes(double erythrocytes) {
        this.erythrocytes = erythrocytes;
    }

    public double getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(double hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public double getHematocrit() {
        return hematocrit;
    }

    public void setHematocrit(double hematocrit) {
        this.hematocrit = hematocrit;
    }

    public double getVcm() {
        return vcm;
    }

    public void setVcm(double vcm) {
        this.vcm = vcm;
    }

    public double getHcm() {
        return hcm;
    }

    public void setHcm(double hcm) {
        this.hcm = hcm;
    }

    public double getChcm() {
        return chcm;
    }

    public void setChcm(double chcm) {
        this.chcm = chcm;
    }

    public double getAde() {
        return ade;
    }

    public void setAde(double ade) {
        this.ade = ade;
    }

    public double getVpm() {
        return vpm;
    }

    public void setVpm(double vpm) {
        this.vpm = vpm;
    }

    public double getAdp() {
        return adp;
    }

    public void setAdp(double adp) {
        this.adp = adp;
    }

    public double getPlateletcrit() {
        return plateletcrit;
    }

    public void setPlateletcrit(double plateletcrit) {
        this.plateletcrit = plateletcrit;
    }

    public double getLeukocytes() {
        return leukocytes;
    }

    public void setLeukocytes(double leukocytes) {
        this.leukocytes = leukocytes;
    }

    public double getNeutrophils() {
        return neutrophils;
    }

    public void setNeutrophils(double neutrophils) {
        this.neutrophils = neutrophils;
    }

    public double getLymphocytes() {
        return lymphocytes;
    }

    public void setLymphocytes(double lymphocytes) {
        this.lymphocytes = lymphocytes;
    }

    public double getMonocytes() {
        return monocytes;
    }

    public void setMonocytes(double monocytes) {
        this.monocytes = monocytes;
    }

    public double getEosinophils() {
        return eosinophils;
    }

    public void setEosinophils(double eosinophils) {
        this.eosinophils = eosinophils;
    }

    public double getBasophils() {
        return basophils;
    }

    public void setBasophils(double basophils) {
        this.basophils = basophils;
    }
}

