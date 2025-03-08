package Database.DAO;

import Exceptions.*;
import Database.Models.Appointment;
import Database.Models.Enums.AppointmentState;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class AppointmentDAO extends BaseDAO<Appointment, Integer> {

    public AppointmentDAO() throws DatabaseOpeningException {
        super();
    }


    @Override
    public boolean save(Appointment entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO " +
                        "citas_medicas (DNI_Paciente, DNI_Medico, ID_Clinica, Fecha_Hora, Estado_Cita, Motivo_Consulta, Observaciones_Medicas) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getPatientDNI(),
                    entity.getDoctorDNI(),
                    entity.getClinicID(),
                    entity.getAppointmentDateTime(),
                    entity.getAppointmentState().getValue(),  // Convert ENUM to String
                    entity.getDescription(),
                    entity.getDoctorObservations()
            );
            if (result) LOGGER.info("Inserted appointment for patient: " + entity.getPatientDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting appointment for patient: " + entity.getPatientDNI(), e);
            throw new DatabaseInsertException("Failed to insert appointment");
        }
    }

    @Override
    public boolean update(Appointment entity) throws DatabaseUpdateException {
        String query =
                "UPDATE citas_medicas " +
                        "SET DNI_Paciente = ?, DNI_Medico = ?, ID_Clinica = ?, Fecha_Hora = ?, Estado_Cita = ?, " +
                        "Motivo_Consulta = ?, Observaciones_Medicas = ? " +
                        "WHERE ID_Cita = ?";

        try {
            boolean result = executeUpdate(query,
                    entity.getPatientDNI(),
                    entity.getDoctorDNI(),
                    entity.getClinicID(),
                    entity.getAppointmentDateTime(),
                    entity.getAppointmentState().getValue(),
                    entity.getDescription(),
                    entity.getDoctorObservations(),
                    entity.getId()
            );

            if (result) LOGGER.info("Updated appointment with ID: " + entity.getId());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating appointment with ID: " + entity.getId(), e);
            throw new DatabaseUpdateException("Failed to update appointment");
        }
    }

    // Updates: the appointment itself, frees up the time slot, and occupies the new one
    public boolean updateAppointment(Appointment appointment, LocalDate newDate, String newTime) throws DatabaseUpdateException {
        String updateAppointmentQuery =
                "UPDATE citas_medicas " +
                        "SET Fecha_Hora = ?, Estado_Cita = ?, Motivo_Consulta = ?, Observaciones_Medicas = ? " +
                        "WHERE ID_Cita = ?";

        String freeOldTimeSlotQuery =
                "UPDATE franjas_horarias f " +
                        "JOIN disponibilidad_medico d ON f.ID_Disponibilidad = d.ID_Disponibilidad " +
                        "SET f.Estado = 'Disponible' " +
                        "WHERE d.DNI_Medico = ? " +
                        "AND d.Fecha = ? " +
                        "AND f.Hora = ?";

        String reserveNewTimeSlotQuery =
                "UPDATE franjas_horarias f " +
                        "JOIN disponibilidad_medico d ON f.ID_Disponibilidad = d.ID_Disponibilidad " +
                        "SET f.Estado = 'Reservada' " +
                        "WHERE d.DNI_Medico = ? " +
                        "AND d.Fecha = ? " +
                        "AND f.Hora = ?";

        try {
            connection.setAutoCommit(false);

            // Free up the old time slot
            executeUpdate(
                    freeOldTimeSlotQuery,
                    appointment.getDoctorDNI(),
                    Date.valueOf(appointment.getAppointmentDateTime().toLocalDateTime().toLocalDate()),
                    Time.valueOf(appointment.getAppointmentDateTime().toLocalDateTime().toLocalTime())
            );

            // Occupies the new one
            executeUpdate(
                    reserveNewTimeSlotQuery,
                    appointment.getDoctorDNI(),
                    Date.valueOf(newDate),
                    Time.valueOf(newTime)
            );

            // Finally, update the Appointment table
            executeUpdate(
                    updateAppointmentQuery,
                    Timestamp.valueOf(newDate.toString() + " " + newTime), // YYYY-MM-DD HH:MM:SS
                    appointment.getAppointmentState().getValue(),
                    appointment.getDescription(),
                    appointment.getDoctorObservations(),
                    appointment.getId()
            );

            connection.commit();
            LOGGER.info("Updated appointment ID: " + appointment.getId() +
                    " to new date: " + newDate + " at " + newTime);
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Failed to rollback transaction.", rollbackEx);
            }
            LOGGER.log(Level.SEVERE, "Error updating appointment ID: " + appointment.getId(), e);
            throw new DatabaseUpdateException("Failed to update appointment.");
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit mode.", autoCommitEx);
            }
        }
    }

    @Override
    public boolean delete(Integer appointmentID) throws DatabaseDeleteException {
        String query = "DELETE FROM citas_medicas WHERE ID_Cita = ?";

        try {
            boolean result = executeUpdate(query, appointmentID);
            if (result) LOGGER.info("Deleted appointment with ID: " + appointmentID);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting appointment with ID: " + appointmentID, e);
            throw new DatabaseDeleteException("Failed to delete appointment");
        }
    }

    @Override
    public Optional<Appointment> findById(Integer appointmentID) throws DatabaseQueryException {
        String query = "SELECT * FROM appointment WHERE ID_Cita = ?";

        try (ResultSet resultSet = executeQuery(query, appointmentID)) {
            if (resultSet.next()) {
                LOGGER.info("Found appointment with ID: " + appointmentID);
                return Optional.of(mapResultSetToAppointment(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve Appointment ID" + appointmentID, e);
            throw new DatabaseQueryException("Failed to retrieve Appointment");
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() throws DatabaseQueryException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM citas_medicas";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                appointments.add(mapResultSetToAppointment(resultSet));
            }
            LOGGER.info("Fetched a total of " + appointments.size() + " appointments from database.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all appointments.", e);
            throw new DatabaseQueryException("Error while retrieving all appointments from database");
        }
        return appointments;
    }

    // Extracts all the doctor's DNI that have had an appointment with that patient
    public List<String> findDoctorDNIsByPatient(String patientDNI) throws DatabaseQueryException {
        List<String> doctorDNIs = new ArrayList<>();
        String query = "SELECT DISTINCT DNI_Medico FROM citas_medicas WHERE DNI_Paciente = ?";

        try (ResultSet resultSet = executeQuery(query, patientDNI)) {
            while (resultSet.next()) {
                doctorDNIs.add(resultSet.getString("DNI_Medico"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctor DNIs for patient: " + patientDNI, e);
            throw new DatabaseQueryException("Error fetching doctor DNIs");
        }

        return doctorDNIs;
    }

    // Retrieves from the DB all the appointments of the given patient except those that have been cancelled
    public List<Appointment> findAppointmentsByPatient(String patientDNI) throws DatabaseQueryException {
        List<Appointment> appointments = new ArrayList<>();
        String query =
                "SELECT * FROM citas_medicas " +
                        "WHERE DNI_Paciente = ? AND Estado_Cita != 'Cancelada' " +
                        "ORDER BY Fecha_Hora ASC";

        try (ResultSet resultSet = executeQuery(query, patientDNI)) {
            while (resultSet.next()) {
                appointments.add(mapResultSetToAppointment(resultSet));
            }
            LOGGER.info("Loaded " + appointments.size() + " appointments for patient " + patientDNI);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading appointments for patient " + patientDNI, e);
            throw new DatabaseQueryException("Could not retrieve patient appointments.");
        }
        return appointments;
    }

    // Retrieves the appointments for a specific doctor on a given date
    public List<String> getBookedAppointments(String doctorDNI, java.sql.Date date) throws DatabaseQueryException {
        List<String> bookedAppointments = new ArrayList<>();

        String query =
                "SELECT TIME(Fecha_Hora) AS Hora " +
                        "FROM citas_medicas " +
                        "WHERE " +
                        "DNI_Medico = ? AND " +
                        "DATE(Fecha_Hora) = ? AND " +
                        "Estado_Cita != 'Cancelada'";

        try (ResultSet resultSet = executeQuery(query, doctorDNI, date)) {
            while (resultSet.next()) {
                bookedAppointments.add(resultSet.getString("Hora")); // AS Fecha_Hora -> Hora
            }
            LOGGER.info("Loaded " + bookedAppointments.size() +
                    " appointments for doctor with DNI " + doctorDNI + " on " + date);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching appointments for Doctor DNI: " + doctorDNI + " at: " + date, e);
            throw new DatabaseQueryException("Error fetching appointments for doctor");
        }
        return bookedAppointments;
    }

    // Retrieves all the appointments of a specific patient on a specific date
    public List<Appointment> getAppointmentsByPatientAndDate(String patientDNI, LocalDate date) throws DatabaseQueryException {
        List<Appointment> appointments = new ArrayList<>();

        String query =
                "SELECT * FROM citas_medicas " +
                        "WHERE DNI_Paciente = ? AND DATE(Fecha_Hora) = ?";

        try (ResultSet resultSet = executeQuery(query, patientDNI, java.sql.Date.valueOf(date))) {
            while (resultSet.next()) {
                appointments.add(mapResultSetToAppointment(resultSet));
            }
            LOGGER.info("Fetched " + appointments.size() + " appointments for patient " + patientDNI + " on " + date);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching appointments for patient " + patientDNI + " on " + date, e);
            throw new DatabaseQueryException("Could not fetch appointments for the patient on the specified date.");
        }

        return appointments;
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("ID_Cita"),
                rs.getString("DNI_Paciente"),
                rs.getString("DNI_Medico"),
                rs.getInt("ID_Clinica"),
                rs.getTimestamp("Fecha_Hora"),
                AppointmentState.fromString(rs.getString("Estado_Cita")),  // Convert String to ENUM
                rs.getString("Motivo_Consulta"),
                rs.getString("Observaciones_Medicas")
        );
    }

}