package DAO;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Models.TimeSlot;
import Models.Enums.TimeSlotStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TimeSlotDAO extends BaseDAO<TimeSlot, Integer> {

    public TimeSlotDAO() throws DatabaseQueryException {
        super();
    }


    // Saves a new time slot
    @Override
    public boolean save(TimeSlot entity) throws DatabaseInsertException {
        String query = "INSERT INTO franjas_horarias (ID_Disponibilidad, Hora, Estado) VALUES (?, ?, ?)";

        try {
            return executeUpdate(query,
                    entity.getAvailabilityID(),
                    entity.getHour(),
                    entity.getStatus().name()
            );
        } catch (SQLException e) {
            throw new DatabaseInsertException("Could not insert time slot", e);
        }
    }

    // Updates the status of a time slot
    @Override
    public boolean update(TimeSlot entity) throws DatabaseUpdateException {
        String query = "UPDATE franjas_horarias SET Estado = ? WHERE ID_Franja = ?";

        try {
            return executeUpdate(query, entity.getStatus().name(), entity.getId());
        } catch (SQLException e) {
            throw new DatabaseUpdateException("Could not update time slot status", e);
        }
    }

    // Deletes a time slot by ID
    @Override
    public boolean delete(Integer timeSlotID) throws DatabaseDeleteException {
        String query = "DELETE FROM franjas_horarias WHERE ID_Franja = ?";

        try {
            return executeUpdate(query, timeSlotID);
        } catch (SQLException e) {
            throw new DatabaseDeleteException("Could not delete time slot", e);
        }
    }

    // Finds a time slot by ID
    @Override
    public Optional<TimeSlot> findById(Integer timeSlotID) throws DatabaseQueryException {
        String query = "SELECT * FROM franjas_horarias WHERE ID_Franja = ?";

        try (ResultSet resultSet = executeQuery(query, timeSlotID)) {
            if (resultSet.next()) {
                return Optional.of(mapResultSetToTimeSlot(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseQueryException("Could not fetch time slot", e);
        }
        return Optional.empty();
    }

    // Retrieves all time slots
    @Override
    public List<TimeSlot> findAll() throws DatabaseQueryException {
        List<TimeSlot> timeSlots = new ArrayList<>();
        String query = "SELECT * FROM franjas_horarias";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                timeSlots.add(mapResultSetToTimeSlot(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseQueryException("Could not fetch all time slots", e);
        }
        return timeSlots;
    }

    // Retrieves available time slots for a specific availability ID
    public List<TimeSlot> getAvailableTimeSlots(int availabilityID) throws DatabaseQueryException {
        List<TimeSlot> timeSlots = new ArrayList<>();
        String query = "SELECT * FROM franjas_horarias WHERE ID_Disponibilidad = ? AND Estado = ?";

        try (ResultSet resultSet = executeQuery(query, availabilityID, TimeSlotStatus.DISPONIBLE.name())) {
            while (resultSet.next()) {
                timeSlots.add(mapResultSetToTimeSlot(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseQueryException("Could not fetch available time slots", e);
        }
        return timeSlots;
    }

    // Maps a ResultSet row to a TimeSlot object
    private TimeSlot mapResultSetToTimeSlot(ResultSet rs) throws SQLException {
        return new TimeSlot(
                rs.getInt("ID_Franja"),
                rs.getInt("ID_Disponibilidad"),
                rs.getTime("Hora"),
                TimeSlotStatus.fromString(rs.getString("Estado"))
        );
    }
}
