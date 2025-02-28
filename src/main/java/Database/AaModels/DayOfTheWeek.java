package Database.AaModels;

public enum DayOfTheWeek {
    MONDAY("Lunes"),
    TUESDAY("Martes"),
    WEDNESDAY("Miércoles"),
    THURSDAY("Jueves"),
    FRIDAY("Viernes"),
    SATURDAY("Sábado"),
    SUNDAY("Domingo");

    private final String value;

    DayOfTheWeek(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DayOfTheWeek fromString(String text) {
        for (DayOfTheWeek day : DayOfTheWeek.values()) {
            if (day.value.equalsIgnoreCase(text)) {
                return day;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + text + "]");
    }
}
