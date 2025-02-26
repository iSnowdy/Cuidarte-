package Models;

import Models.Enums.TimeSlotStatus;

import java.sql.Time;

public class TimeSlot {
    private int id;
    private int availabilityID;
    private Time hour;
    private TimeSlotStatus status; // Disponible | Reservada

    public TimeSlot(int id, int availabilityID, Time hour, TimeSlotStatus status) {
        this.id = id;
        this.availabilityID = availabilityID;
        this.hour = hour;
        this.status = status;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public int getAvailabilityID() {
        return availabilityID;
    }

    public Time getHour() {
        return hour;
    }

    public TimeSlotStatus getStatus() {
        return status;
    }

    public void setStatus(TimeSlotStatus status) {
        this.status = status;
    }
}
