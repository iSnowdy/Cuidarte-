package Models.Enums;

public enum DayOfTheWeek {
    MONDAY("Lunes"),
    TUESDAY("Martes"),
    WEDNESDAY("Miércoles"),
    THURSDAY("Jueves"),
    FRIDAY("Viernes"),
    SATURDAY("Sábado"),
    SUNDAY("Domingo");

    private String value;

    DayOfTheWeek(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
