package Database.Models.Enums;

public enum TimeSlotStatus {
    DISPONIBLE("Disponible"),
    RESERVADA("Reservada");

    private final String value;

    TimeSlotStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TimeSlotStatus fromString(String text) {
        for (TimeSlotStatus slotStatus : TimeSlotStatus.values()) {
            if (slotStatus.value.equalsIgnoreCase(text)) {
                return slotStatus;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + text + "]");
    }
}
