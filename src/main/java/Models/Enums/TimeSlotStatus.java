package Models.Enums;

public enum TimeSlotStatus {
    DISPONIBLE,
    RESERVADA;

    public static TimeSlotStatus fromString(String status) {
        try {
            return TimeSlotStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DISPONIBLE; // Default case
        }
    }
}
