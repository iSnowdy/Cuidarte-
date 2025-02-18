package Interfaces;

import Models.Appointment;

import java.util.List;
import java.util.Optional;

public interface IAppointmentService {
    boolean bookAppointment(Appointment appointment);

    boolean cancelAppointment(int appointmentId);

    List<Appointment> getAvailableAppointments(String clinicName, String speciality);

    Optional<Appointment> getAppointment(int appointmentId);
}
