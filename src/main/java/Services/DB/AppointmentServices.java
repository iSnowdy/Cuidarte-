package Services.DB;

import DAO.AppointmentDAO;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Interfaces.IAppointmentService;
import Models.Appointment;
import Models.Enums.AppointmentState;

import java.util.List;
import java.util.Optional;

public class AppointmentServices implements IAppointmentService {
    private final AppointmentDAO appointmentDAO;

    public AppointmentServices() throws DatabaseOpeningException {
        this.appointmentDAO = new AppointmentDAO();
    }


    @Override
    public Optional<Appointment> bookAppointment(Appointment appointment) throws DatabaseInsertException {
        appointment.setAppointmentState(AppointmentState.PENDING);
        if (appointmentDAO.save(appointment)) {
            return Optional.of(appointment);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateAppointmentState(int appointmentID, AppointmentState newState) throws DatabaseUpdateException {
        Optional<Appointment> optional = appointmentDAO.findById(appointmentID);
        if (optional.isEmpty()) throw new DatabaseUpdateException("Appointment with ID " + appointmentID + " not found");

        Appointment appointment = optional.get();
        // Business logic checks
        if (appointment.getAppointmentState() == AppointmentState.CANCELLED) {
            throw new DatabaseUpdateException("Appointment with ID " + appointmentID + " is cancelled, thus cannot be modified");
        }
        if (appointment.getAppointmentState() == AppointmentState.ATTENDED) {
            throw new DatabaseUpdateException("Appointment with ID " + appointmentID + " is attended, thus cannot be modified");
        }

        appointment.setAppointmentState(newState);
        return appointmentDAO.update(appointment);
    }

    @Override
    public Optional<Appointment> getAppointment(int appointmentID) throws DatabaseQueryException {
        return appointmentDAO.findById(appointmentID);
    }
}
