package Interfaces;

import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Models.Appointment;
import Models.Enums.AppointmentState;

import java.util.List;
import java.util.Optional;

public interface IAppointmentService {
    Optional<Appointment> bookAppointment(Appointment appointment) throws DatabaseInsertException;

    boolean updateAppointmentState(int appointmentID, AppointmentState newState) throws DatabaseUpdateException;

    //List<Appointment> getAvailableAppointments(int clinicID, int specialityID) throws DatabaseQueryException;

    Optional<Appointment> getAppointment(int appointmentID) throws DatabaseQueryException;
}
