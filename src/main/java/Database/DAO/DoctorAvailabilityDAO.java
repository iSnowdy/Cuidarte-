package Database.DAO;

import Exceptions.*;
import Database.Models.DoctorAvailability;
import Database.Models.Enums.TimeSlotStatus;
import Database.Models.TimeSlot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class DoctorAvailabilityDAO extends BaseDAO<DoctorAvailability, Integer> {

    public DoctorAvailabilityDAO() throws DatabaseOpeningException {
        super();
    }

    @Override
    public boolean save(DoctorAvailability entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO disponibilidad_medico " +
                        "(DNI_Medico, ID_Clinica, Fecha, Hora_Inicio, Hora_Fin, Duracion_Cita) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getDoctorDNI(),
                    entity.getClinicID(),
                    entity.getDate(),
                    entity.getStartTime(),
                    entity.getEndTime(),
                    entity.getAppointmentDuration()
            );

            if (result) {
                LOGGER.info("Inserted doctor availability for doctor: " + entity.getDoctorDNI() +
                        " in clinic ID: " + entity.getClinicID() + " on " + entity.getDate());
            }
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting doctor availability for doctor: " +
                    entity.getDoctorDNI(), e);
            throw new DatabaseInsertException("Failed to insert doctor availability");
        }
    }

    @Override
    public boolean update(DoctorAvailability entity) throws DatabaseUpdateException {
        String query =
                "UPDATE disponibilidad_medico " +
                        "SET Fecha = ?, Hora_Inicio = ?, Hora_Fin = ?, Duracion_Cita = ? " +
                        "WHERE ID_Disponibilidad = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getDate(),
                    entity.getStartTime(),
                    entity.getEndTime(),
                    entity.getAppointmentDuration(),
                    entity.getDoctorAvailabilityID()
            );

            if (result) {
                LOGGER.info("Updated doctor availability with ID: " + entity.getDoctorAvailabilityID());
            }

            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating doctor availability with ID: " + entity.getDoctorAvailabilityID(), e);
            throw new DatabaseUpdateException("Failed to update doctor availability");
        }
    }

    public boolean updateTimeSlotStatus(String doctorDNI, LocalDate date, String time, TimeSlotStatus newStatus) throws DatabaseUpdateException {
        String query =
                "UPDATE franjas_horarias f " +
                "JOIN disponibilidad_medico d " +
                        "ON f.ID_Disponibilidad = d.ID_Disponibilidad " +
                "SET f.Estado = ? " +
                "WHERE d.DNI_Medico = ? " +
                    "AND d.Fecha = ? " +
                    "AND f.Hora = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    newStatus.getValue(),
                    doctorDNI,
                    date,
                    time
            );

            if (result) {
                LOGGER.info("Updated doctor availability with DNI: " +
                        doctorDNI + " to: " + newStatus.getValue() +
                        " on the: " + date + " - " + time);
            }
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating doctor availability with DNI: " + doctorDNI, e);
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
        String query = "SELECT * FROM disponibilidad_medico WHERE ID_Disponibilidad = ?";

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

    public List<DoctorAvailability> getDoctorAvailableDays(String doctorDNI, int clinicID) throws DatabaseQueryException {
        List<DoctorAvailability> availableDays = new ArrayList<>();

        String query =
                "SELECT * FROM disponibilidad_medico " +
                        "WHERE DNI_Medico = ? AND ID_Clinica = ?";

        try (ResultSet resultSet = executeQuery(query, doctorDNI, clinicID)) {
            while (resultSet.next()) {
                availableDays.add(mapResultSetToDoctorAvailability(resultSet));
            }
            LOGGER.info("Found " + availableDays.size() + " available days for doctor " + doctorDNI + " at clinic ID " + clinicID);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching available days for doctor " + doctorDNI + " at clinic ID: " + clinicID, e);
            throw new DatabaseQueryException("Could not fetch doctor availability");
        }

        return availableDays;
    }

    public List<String> findAvailableHoursByDoctorAndDate(String doctorDNI, int clinicID, LocalDate date) throws DatabaseQueryException {
        List<String> availableHours = new ArrayList<>();

        String query =
                "SELECT Hora_Inicio, Hora_Fin, Duracion_Cita " +
                        "FROM disponibilidad_medico " +
                        "WHERE DNI_Medico = ? AND ID_Clinica = ? AND Fecha = ?";

        try (ResultSet resultSet = executeQuery(query, doctorDNI, clinicID, java.sql.Date.valueOf(date))) {
            if (resultSet.next()) {
                Time startTime = resultSet.getTime("Hora_Inicio");
                Time endTime = resultSet.getTime("Hora_Fin");
                int duration = resultSet.getInt("Duracion_Cita");

                LocalTime start = startTime.toLocalTime();
                LocalTime end = endTime.toLocalTime();

                while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
                    availableHours.add(start.toString()); // "HH:mm"
                    start = start.plusMinutes(duration);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching available hours for doctor " + doctorDNI + " at clinic " + clinicID + " on " + date, e);
            throw new DatabaseQueryException("Could not fetch available hours");
        }
        return availableHours;
    }

    // Retrieves the available time slots of a doctor working on a specific day
    public List<TimeSlot> findAvailableTimeSlotsByDoctorAndDate(String doctorDNI, int clinicID, LocalDate date) throws DatabaseQueryException {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();

        String query =
                "SELECT f.ID_Franja, f.ID_Disponibilidad, f.Hora, f.Estado " +
                "FROM franjas_horarias f " +
                "INNER JOIN disponibilidad_medico d ON f.ID_Disponibilidad = d.ID_Disponibilidad " +
                "WHERE d.DNI_Medico = ? AND d.ID_Clinica = ? AND d.Fecha = ? AND f.Estado = ?";

        try (ResultSet resultSet = executeQuery(query, doctorDNI, clinicID, java.sql.Date.valueOf(date), TimeSlotStatus.DISPONIBLE.name())) {
            while (resultSet.next()) {
                availableTimeSlots.add(new TimeSlot(
                        resultSet.getInt("ID_Franja"),
                        resultSet.getInt("ID_Disponibilidad"),
                        resultSet.getTime("Hora"),
                        TimeSlotStatus.fromString(resultSet.getString("Estado"))
                ));
            }
            LOGGER.info("Found " + availableTimeSlots.size() + " available time slots for doctor " + doctorDNI + " at clinic " + clinicID + " on " + date);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching available time slots for doctor " + doctorDNI + " at clinic " + clinicID + " on " + date, e);
            throw new DatabaseQueryException("Could not fetch available time slots");
        }
        return availableTimeSlots;
    }

    public List<DoctorAvailability> findAvailabilityByClinicAndDoctor(int clinicID, String doctorDNI) throws DatabaseQueryException {
        List<DoctorAvailability> availabilities = new ArrayList<>();
        String query =
                "SELECT * FROM disponibilidad_medico " +
                        "WHERE ID_Clinica = ? AND DNI_Medico = ?";

        try (ResultSet resultSet = executeQuery(query, clinicID, doctorDNI)) {
            while (resultSet.next()) {
                availabilities.add(mapResultSetToDoctorAvailability(resultSet));
            }
            LOGGER.info("Found " + availabilities.size() + " availability slots for doctor "
                    + doctorDNI + " in clinic ID " + clinicID);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching availability for doctor " + doctorDNI + " in clinic " + clinicID, e);
            throw new DatabaseQueryException("Could not fetch doctor availability");
        }
        return availabilities;
    }

    private DoctorAvailability mapResultSetToDoctorAvailability(ResultSet resultSet) throws SQLException {
        return new DoctorAvailability(
                resultSet.getInt("ID_Disponibilidad"),
                resultSet.getString("DNI_Medico"),
                resultSet.getInt("ID_Clinica"),
                resultSet.getDate("Fecha"),
                resultSet.getTime("Hora_Inicio"),
                resultSet.getTime("Hora_Fin"),
                resultSet.getInt("Duracion_Cita")
        );
    }
}
