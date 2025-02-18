package Models.Enums;

public enum TestType {
    BLOOD_LAB("Hemograma"),
    BIOCHEMISTRY_LAB("Bioquímica"),
    IMMUNOLOGY_LAB("Inmunologia"),
    MICROBIOLOGY_LAB("Microbiología");

    private final String value;

    TestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TestType fromString(String text) {
        for (TestType testType : TestType.values()) {
            if (testType.value.equalsIgnoreCase(text)) {
                return testType;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + text + "]");
    }
}
