package DAO;

import Exceptions.*;
import Models.DoctorAvailability;
import Models.Enums.DayOfTheWeek;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

// Queries both disponibilidad_medico & dias_disponibles_medico

public class DoctorAvailabilityDAO extends BaseDAO<DoctorAvailability, Integer> {

    public DoctorAvailabilityDAO() throws DatabaseOpeningException {
        super();
    }

    @Override
    public boolean save(DoctorAvailability entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO " +
                "doctor_availability (DNI_Medico, Hora_Inicio, Hora_Fin, Duracion_Cita) " +
                "VALUES (?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getDoctorDNI(),
                    entity.getStartTime(),
                    entity.getEndTime(),
                    entity.getAppointmentDuration()
            );

            if (result) {
                LOGGER.info("Inserted doctor availability for doctor: " + entity.getDoctorDNI());

                // Retrieve the last inserted ID
                int availabilityId = getLastInsertedId();
                saveAvailableDays(availabilityId, entity.getAvailableDays());
            }

            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting doctor availability for doctor: " + entity.getDoctorDNI(), e);
            throw new DatabaseInsertException("Failed to insert doctor availability");
        }
    }

    @Override
    public boolean update(DoctorAvailability entity) throws DatabaseQueryException {
        String query =
                "UPDATE disponibilidad_medico " +
                "SET DNI_Medico = ?, Hora_Inicio = ?, Hora_Fin = ?, Duracion_Cita = ? " +
                "WHERE ID_Disponibilidad = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getDoctorDNI(),
                    entity.getStartTime(),
                    entity.getEndTime(),
                    entity.getAppointmentDuration(),
                    entity.getDoctorAvailabilityID()
            );

            if (result) {
                LOGGER.info("Updated doctor availability with ID: " + entity.getDoctorAvailabilityID());

                // Delete old days and insert new ones
                deleteAvailableDays(entity.getDoctorAvailabilityID());
                saveAvailableDays(entity.getDoctorAvailabilityID(), entity.getAvailableDays());
            }

            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating doctor availability with ID: " + entity.getDoctorAvailabilityID(), e);
            throw new DatabaseUpdateException("Failed to update doctor availability");
        }
    }

    @Override
    public boolean delete(Integer doctorAvailabilityID) throws DatabaseDeleteException {
        String query = "DELETE FROM disponibilidad_medico WHERE ID_Disponibilidad = ?";

        try {
            boolean result = executeUpdate(query, doctorAvailabilityID);
            if (result) {
                LOGGER.info("Deleted doctor availability with ID: " + doctorAvailabilityID);
            }
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting doctor availability with ID: " + doctorAvailabilityID, e);
            throw new DatabaseDeleteException("Failed to delete doctor availability");
        }
    }

    @Override
    public Optional<DoctorAvailability> findById(Integer doctorAvailabilityID) throws DatabaseQueryException {
        String query = "SELECT * FROM disponibilidad_medico WHERE ID_Disponibilidad = " + doctorAvailabilityID;

        try (ResultSet resultSet = executeQuery(query, doctorAvailabilityID)) {
            if (resultSet.next()) {
                LOGGER.info("Fetched doctor availability with ID:" + doctorAvailabilityID);
                return Optional.of(mapResultSetToDoctorAvailability(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not find the doctor availability for ID " + doctorAvailabilityID, e);
            throw new DatabaseQueryException("Could not find the doctor availability");
        }
        return Optional.empty();
    }

    @Override
    public List<DoctorAvailability> findAll() throws DatabaseQueryException {
        List<DoctorAvailability> availabilities = new ArrayList<>();
        String query = "SELECT * FROM disponibilidad_medico";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                availabilities.add(mapResultSetToDoctorAvailability(resultSet));
            }
            LOGGER.info("Fetched all doctors availabilities. Total of: " + availabilities.size());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all doctor availabilities.", e);
            throw new DatabaseQueryException("Could not find all doctors availabilities");
        }
        return availabilities;
    }

    private DoctorAvailability mapResultSetToDoctorAvailability(ResultSet resultSet) throws SQLException {
        int availabilityId = resultSet.getInt("ID_Availability");
        List<DayOfTheWeek> availableDays = getAvailableDays(availabilityId);

        return new DoctorAvailability(
                availabilityId,
                resultSet.getString("DNI_Medico"),
                resultSet.getInt("ID_Clinica"),
                availableDays,
                resultSet.getTime("Hora_Inicio"),
                resultSet.getTime("Hora_Fin"),
                resultSet.getInt("Duracion_Cita")
        );
    }

    // TODO: I don't think this is right

    private void saveAvailableDays(int availabilityID, List<DayOfTheWeek> availableDays) throws SQLException {
        String query =
                "INSERT INTO " +
                "dias_disponibles_medico (ID_Dias_Disponibles, Dia_Semana) " +
                "VALUES (?, ?)";
        for (DayOfTheWeek day : availableDays) {
            executeUpdate(query, availabilityID, day.getValue());
        }
        LOGGER.info("Saved all available days for doctor availability ID: " + availabilityID);
    }

    private void deleteAvailableDays(int availabilityID) throws SQLException {
        String query =
                "DELETE FROM dias_disponibles_medico " +
                "WHERE ID_Dias_Disponibles = ?";

        boolean result = executeUpdate(query, availabilityID);
        if (result) LOGGER.info("Deleted available days for doctor availability ID: " + availabilityID);
        else LOGGER.info("No available days found to delete for doctor availability with ID: " + availabilityID);
    }

    private List<DayOfTheWeek> getAvailableDays(int availabilityID) throws SQLException {
        List<DayOfTheWeek> availableDays = new ArrayList<>();
        String query = "SELECT Dia_Semana FROM dias_disponibles_medico WHERE ID_Dias_Disponibles = ?";

        try (ResultSet resultSet = executeQuery(query, availabilityID)) {
            while (resultSet.next()) {
                availableDays.add(DayOfTheWeek.fromString(resultSet.getString("Dia_Semana")));
            }
        }
        return availableDays;
    }

    // Every connection has their own LAST_INSERT_ID(), and is unique to theirs. So we do not have to worry about
    // another connection's ID due to it not being concurrent. And since we just inserted that entity, we are sure
    // to retrieve its auto-generated ID
    private int getLastInsertedId() throws SQLException {
        String query = "SELECT LAST_INSERT_ID() as last_id";
        try (ResultSet resultSet = executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("last_id");
            }
        }
        throw new SQLException("Could not find last inserted ID");
    }
}
