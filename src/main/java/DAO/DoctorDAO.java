package DAO;

import Exceptions.*;
import Models.Doctor;
import Models.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class DoctorDAO extends BaseDAO<Doctor, String> {

    public DoctorDAO() throws DatabaseOpeningException {
        super();
    }


    @Override
    public boolean save(Doctor entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO medicos " +
                        "(DNI_Medico, Nombre, Apellidos, Numero_Telefono, Email, Especialidad) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(query,
                    entity.getDNI(),
                    entity.getFirstName(),
                    entity.getSurname(),
                    entity.getPhoneNumber(),
                    entity.getEmail(),
                    entity.getSpecialty()
            );
            if (result) LOGGER.info("Inserted doctor: " + entity.getDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting doctor: " + entity.getDNI(), e);
            throw new DatabaseInsertException("Failed to insert doctor");
        }
    }

    @Override
    public boolean update(Doctor entity) throws DatabaseQueryException {
        String query =
                "UPDATE medicos " +
                        "SET Nombre = ?, Apellidos = ?, Numero_Telefono = ?, Email = ?, Especialidad = ? " +
                        "WHERE DNI_Medico = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getFirstName(),
                    entity.getSurname(),
                    entity.getPhoneNumber(),
                    entity.getEmail(),
                    entity.getSpecialty(),
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

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        return new Doctor(
                rs.getString("DNI_Medico"),
                rs.getString("Nombre"),
                rs.getString("Apellidos"),
                rs.getString("Numero_Telefono"),
                rs.getString("Email"),
                rs.getString("Especialidad")
        );
    }
}
