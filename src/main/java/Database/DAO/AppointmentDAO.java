package Database.DAO;

import Exceptions.*;
import Database.AaModels.Appointment;
import Database.AaModels.AppointmentState;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public boolean update(Appointment entity) throws DatabaseQueryException {
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

    public List<Appointment> findAppointmentsByPatient(String patientDNI) throws DatabaseQueryException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM citas_medicas WHERE DNI_Paciente = ? ORDER BY Fecha_Hora ASC";

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

    public List<Appointment> getAppointmentsByDoctorAndDate(String doctorDNI, LocalDate date) throws DatabaseQueryException {
        List<Appointment> appointments = new ArrayList<>();

        String query =
                "SELECT * FROM citas_medicas " +
                        "WHERE DNI_Medico = ? AND DATE(Fecha_Hora) = ? AND Estado_Cita != 'Cancelada'";

        try (ResultSet resultSet = executeQuery(query, doctorDNI, java.sql.Date.valueOf(date))) {
            while (resultSet.next()) {
                appointments.add(mapResultSetToAppointment(resultSet));
            }
            LOGGER.info("Loaded " + appointments.size() +
                    " appointments for doctor with DNI " + doctorDNI + " on " + date);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching appointments for Doctor DNI: " + doctorDNI + " at: " + date, e);
            throw new DatabaseQueryException("Error fetching appointments for doctor");
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
