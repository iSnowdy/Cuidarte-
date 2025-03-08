package Database.DAO;

import Exceptions.*;
import Database.Models.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;

public class DoctorDAO extends BaseDAO<Doctor, String> {

    public DoctorDAO() throws DatabaseOpeningException {
        super();
    }


    @Override
    public boolean save(Doctor entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO medicos " +
                        "(DNI_Medico, Nombre, Apellidos, Numero_Telefono, Email, Especialidad, Descripcion) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(query,
                    entity.getDNI(),
                    entity.getFirstName(),
                    entity.getSurname(),
                    entity.getPhoneNumber(),
                    entity.getEmail(),
                    entity.getSpeciality(),
                    entity.getDescription()
            );
            if (result) LOGGER.info("Inserted doctor: " + entity.getDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting doctor: " + entity.getDNI(), e);
            throw new DatabaseInsertException("Failed to insert doctor");
        }
    }

    @Override
    public boolean update(Doctor entity) throws DatabaseUpdateException {
        String query =
                "UPDATE medicos " +
                        "SET Nombre = ?, Apellidos = ?, Numero_Telefono = ?, Email = ?, Especialidad = ?, Descripcion = ? " +
                        "WHERE DNI_Medico = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getFirstName(),
                    entity.getSurname(),
                    entity.getPhoneNumber(),
                    entity.getEmail(),
                    entity.getSpeciality(),
                    entity.getDescription(),
                    entity.getDNI()
            );
            if (result) LOGGER.info("Updated doctor: " + entity.getDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating doctor: " + entity.getDNI(), e);
            throw new DatabaseUpdateException("Failed to update doctor");
        }
    }

    @Override
    public boolean delete(String dni) throws DatabaseDeleteException {
        String query = "DELETE FROM medicos WHERE DNI_Medico = ?";

        try {
            boolean result = executeUpdate(query, dni);
            if (result) LOGGER.info("Deleted doctor with DNI: " + dni);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting doctor with DNI: " + dni, e);
            throw new DatabaseDeleteException("Failed to delete doctor");
        }
    }

    @Override
    public Optional<Doctor> findById(String dni) throws DatabaseQueryException {
        String query = "SELECT * FROM medicos WHERE DNI_Medico = ?";

        try (ResultSet resultSet = executeQuery(query, dni)) {
            if (resultSet.next()) {
                LOGGER.log(Level.INFO, "Doctor with DNI " + dni + " found.");
                return Optional.of(mapResultSetToDoctor(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching Doctor with DNI: " + dni, e);
            throw new DatabaseQueryException("Error fetching Doctor");
        }
        return Optional.empty();
    }

    public Optional<Doctor> findByName(String name, String surname) throws DatabaseQueryException {
        String query = "SELECT * FROM medicos WHERE Nombre = ? AND Apellidos = ?";

        try (ResultSet resultSet = executeQuery(query, name, surname)) {
            if (resultSet.next()) {
                LOGGER.log(Level.INFO, "Doctor with name " + name + " and surname " + surname + " found.");
                return Optional.of(mapResultSetToDoctor(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors with name " + name, e);
            throw new DatabaseQueryException("Error fetching doctor");
        }
        return Optional.empty();
    }

    public List<Doctor> findBySpeciality(String speciality) throws DatabaseQueryException {
        List<Doctor> doctorsBySpeciality = new ArrayList<>();
        String query = "SELECT * FROM medicos WHERE Especialidad = ?";

        try (ResultSet resultSet = executeQuery(query, speciality)) {
            while (resultSet.next()) {
                doctorsBySpeciality.add(mapResultSetToDoctor(resultSet));
            }
            LOGGER.info("Found " + doctorsBySpeciality.size() + " doctors with speciality: " + speciality);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors with speciality " + speciality, e);
            throw new DatabaseQueryException("Error fetching Doctor");
        }
        return doctorsBySpeciality;
    }

    public List<Doctor> findAllBosses() throws DatabaseQueryException {
        List<Doctor> bosses = new ArrayList<>();

        String query = "SELECT * FROM medicos WHERE Descripcion LIKE '%Jefe de%'";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                bosses.add(mapResultSetToDoctor(resultSet));
            }
            LOGGER.info("Found " + bosses.size() + " bosses");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching department head doctors", e);
            throw new DatabaseQueryException("Error fetching department head doctors");
        }
        return bosses;
    }

    @Override
    public List<Doctor> findAll() throws DatabaseQueryException {
        List<Doctor> doctors = new ArrayList<>();
        String query =
                "SELECT * FROM medicos";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                doctors.add(mapResultSetToDoctor(resultSet));
            }
            LOGGER.info("Successfully retrieved " + doctors.size() + " doctors.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all doctors", e);
            throw new DatabaseQueryException("Error fetching all doctors");
        }
        return doctors;
    }

    public List<Doctor> findDoctorsByDNIs(List<String> doctorDNIs) throws DatabaseQueryException {
        List<Doctor> doctors = new ArrayList<>();
        if (doctorDNIs.isEmpty()) return doctors;

        String placeholders = String.join(",", Collections.nCopies(doctorDNIs.size(), "?"));
        String query = "SELECT * FROM medicos WHERE DNI_Medico IN (" + placeholders + ")";

        try (ResultSet resultSet = executeQuery(query, doctorDNIs.toArray())) {
            while (resultSet.next()) {
                doctors.add(mapResultSetToDoctor(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors by DNIs", e);
            throw new DatabaseQueryException("Error fetching doctors");
        }

        return doctors;
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getString("DNI_Medico"),
                rs.getString("Nombre"),
                rs.getString("Apellidos"),
                rs.getString("Numero_Telefono"),
                rs.getString("Email"),
                rs.getString("Especialidad"),
                rs.getString("Descripcion")
        );
    }

    public List<Doctor> findDoctorsByClinicAndSpeciality(int clinicID, String speciality) throws DatabaseQueryException {
        List<Doctor> doctors = new ArrayList<>();

        String query =
                "SELECT * " +
                        "FROM medicos m " +
                        "JOIN disponibilidad_medico dm " +
                        "ON m.DNI_Medico = dm.DNI_Medico " +
                        "WHERE dm.ID_Clinica = ? AND m.Especialidad = ?";

        try (ResultSet resultSet = executeQuery(query, clinicID, speciality)) {
            while (resultSet.next()) {
                doctors.add(mapResultSetToDoctor(resultSet));
            }
            LOGGER.info("Successfully retrieved " + doctors.size() +
                    " doctors by clinic ID: " + clinicID + " and speciality: " + speciality);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors by clinic and speciality", e);
            throw new DatabaseQueryException("Error fetching doctors by clinic and speciality");
        }
        return doctors;
    }

    public Map<String, List<String>> getDoctorAvailableTimeSlots(int clinicID, java.sql.Date selectedDate) throws DatabaseQueryException {
        Map<String, List<String>> doctorAvailableTimeSlots = new HashMap<>();

        String query =
                "SELECT * " +
                        "FROM disponibilidad_medico dm " +
                        "WHERE dm.ID_Clinica = ?";

        try (ResultSet resultSet = executeQuery(query, clinicID)) {
            while (resultSet.next()) {
                String doctorDNI = resultSet.getString("DNI_Medico");
                // Generates available time slots
                List<String> timeSlots = generateTimeSlots(
                        resultSet.getString("Hora_Inicio"),
                        resultSet.getString("Hora_Fin"),
                        resultSet.getInt("Duracion_Cita")
                );

                try {
                    // Now we remove the booked slots from the slots we give back
                    List<String> bookedSlots = new AppointmentDAO().getBookedAppointments(doctorDNI, selectedDate);
                    timeSlots.removeAll(bookedSlots);
                } catch (DatabaseOpeningException | DatabaseQueryException e) {
                    LOGGER.log(Level.SEVERE, "Error fetching booked slots from AppointmentDAO", e);
                    throw new DatabaseQueryException("Error fetching booked slots");
                }

                doctorAvailableTimeSlots.put(doctorDNI, timeSlots);
            }
            LOGGER.info("Loaded doctors slots for clinic ID: " + clinicID);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors available time slots from clinic ID " + clinicID, e);
            throw new DatabaseQueryException("Error fetching doctors available time slots");
        }
        return doctorAvailableTimeSlots;
    }

    private List<String> generateTimeSlots(String start, String end, int duration) {
        List<String> timeSlots = new ArrayList<>();

        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        while (startTime.isBefore(endTime)) {
            timeSlots.add(startTime.toString());
            startTime = startTime.plusMinutes(duration);
        }
        return timeSlots;
    }
}