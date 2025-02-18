package Models.Enums;

public enum AppointmentState {
    PENDING("Pendiente"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelada"),
    ATTENDED("Atendida");

    private final String value;

    AppointmentState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AppointmentState fromString(String text) {
        for (AppointmentState state : AppointmentState.values()) {
            if (state.value.equalsIgnoreCase(text)) {
                return state;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + text + "]");
    }
}
