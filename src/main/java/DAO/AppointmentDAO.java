package DAO;

import Exceptions.*;
import Models.Appointment;
import Models.Enums.AppointmentState;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                    entity.getAppointmentDate(),
                    entity.getAppointmentState().name(),  // Convert ENUM to String
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
                    entity.getAppointmentDate(),
                    entity.getAppointmentState().name(),
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

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("ID_Cita"),
                rs.getString("DNI_Paciente"),
                rs.getString("DNI_Medico"),
                rs.getInt("ID_Clinica"),
                rs.getDate("Fecha_Hora"),
                AppointmentState.fromString(rs.getString("Estado_Cita")),  // Convert String to ENUM
                rs.getString("Motivo_Consulta"),
                rs.getString("Observaciones_Medicas")
        );
    }
}
