package Models.Enums;

public enum AppointmentState {
    PENDING("Pendiente"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelada"),
    ATTENDED("Atendida");

    public String value;

    AppointmentState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
