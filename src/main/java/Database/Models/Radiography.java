package Database.Models;

public class Radiography {
    private int
            radiographyId, testId;
    private String result;
    private String imageURL;

    public Radiography(int radiographyId, int testId, String result, String imageURL) {
        this.radiographyId = radiographyId;
        this.testId = testId;
        this.result = result;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public int getRadiographyId() {
        return radiographyId;
    }

    public void setRadiographyId(int radiographyId) {
        this.radiographyId = radiographyId;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
