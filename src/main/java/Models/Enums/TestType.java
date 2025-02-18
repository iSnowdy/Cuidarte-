package Models.Enums;

public enum TestType {
    BLOOD_LAB("Hemograma"),
    BIOCHEMISTRY_LAB("Bioquímica"),
    IMMUNOLOGY_LAB("Inmunologia"),
    MICROBIOLOGY_LAB("Microbiología");

    private String value;

    TestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
