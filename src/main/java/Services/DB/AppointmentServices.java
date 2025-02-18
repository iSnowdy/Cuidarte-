package Services.DB;

import Interfaces.IAppointmentService;
import Models.Appointment;

import java.util.List;
import java.util.Optional;

public class AppointmentServices implements IAppointmentService {


    @Override
    public boolean bookAppointment(Appointment appointment) {
        // Insert Appointment into DB
        return false;
    }

    @Override
    public boolean cancelAppointment(int appointmentId) {
        // Delete Appointment from DB
        return false;
    }

    @Override
    public List<Appointment> getAvailableAppointments(String clinicName, String speciality) {
        // Fetch all appointments from DB
        return List.of();
    }

    @Override
    public Optional<Appointment> getAppointment(int appointmentId) {
        // Retrieves a singular Appointment entity
        return Optional.empty();
    }
}
