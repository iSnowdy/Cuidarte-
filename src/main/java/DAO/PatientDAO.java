package DAO;

import Exceptions.*;
import Models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class PatientDAO extends BaseDAO<Patient, String> {

    public PatientDAO() throws DatabaseOpeningException {
        super();
    }


    @Override
    public boolean save(Patient entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO pacientes " +
                "(DNI_Paciente, Nombre, Apellidos, Numero_Telefono, Email, Fecha_Nacimiento, Edad, Contraseña, Salt, Codigo_Verificacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getDNI(),
                    entity.getFirstName(),
                    entity.getSurname(),
                    entity.getPhoneNumber(),
                    entity.getEmail(),
                    entity.getDateOfBirth(),
                    entity.getAge(),
                    entity.getPassword(),
                    entity.getSalt(),
                    entity.getVerificationCode()
            );
            if (result) LOGGER.info("Inserted patient: " + entity.getDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting patient: " + entity.getDNI(), e);
            throw new DatabaseInsertException("Failed to insert patient");
        }
    }

    @Override
    public boolean update(Patient entity) throws DatabaseQueryException {
        String query =
                "UPDATE pacientes " +
                "SET Nombre = ?, Apellidos = ?, Numero_Telefono = ?, Email = ?, Fecha_Nacimiento = ?, " +
                    "Edad = ?, Contraseña = ?, Salt = ?, Codigo_Verificacion = ? " +
                "WHERE DNI_Paciente = ?";

        try {
            boolean result = executeUpdate(query,
                    entity.getDNI(),
                    entity.getFirstName(),
                    entity.getPhoneNumber(),
                    entity.getEmail(),
                    entity.getDateOfBirth(),
                    entity.getAge(),
                    entity.getPassword(),
                    entity.getSalt(),
                    entity.getVerificationCode(),
                    entity.getDNI()
            );
            if (result) LOGGER.info("Updated patient: " + entity.getDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating patient: " + entity.getDNI(), e);
            throw new DatabaseUpdateException("Failed to update patient");
        }
    }

    @Override
    public boolean delete(String dni) throws DatabaseDeleteException {
        String query = "DELETE FROM pacientes WHERE DNI_Paciente = ?";

        try {
            boolean result = executeUpdate(query, dni);
            if (result) LOGGER.info("Deleted patient with DNI: " + dni);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting patient with DNI: " + dni, e);
            throw new DatabaseDeleteException("Failed to delete patient");
        }
    }

    @Override
    public Optional<Patient> findById(String dni) throws DatabaseQueryException {
        String query = "SELECT * FROM pacientes WHERE DNI_Paciente = ?";

        try (ResultSet rs = executeQuery(query, dni)) {
            if (rs.next()) {
                LOGGER.info("Fetched patient with DNI: " + dni);
                return Optional.of(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching patient with DNI: " + dni, e);
            throw new DatabaseQueryException("Failed to execute SELECT query");
        }
        return Optional.empty();
    }

    public Optional<Patient> findByEmail(String email) throws DatabaseQueryException {
        String query = "SELECT * FROM pacientes WHERE Email = ?";

        try (ResultSet resultSet = executeQuery(query, email)) {
            if (resultSet.next()) {
                LOGGER.info("Fetched patient with email: " + email);
                return Optional.of(mapResultSetToPatient(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching patient with email: " + email, e);
            throw new DatabaseQueryException("Failed to execute SELECT by email");
        }
        return Optional.empty();
    }

    @Override
    public List<Patient> findAll() throws DatabaseQueryException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM pacientes";

        try (ResultSet rs = executeQuery(query)) {
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
            LOGGER.info("Fetched all patients successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching patients.", e);
            throw new DatabaseQueryException("Failed to execute SELECT query");
        }
        return patients;
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getString("DNI_Paciente"),
                rs.getString("Nombre"),
                rs.getString("Apellidos"),
                rs.getString("Numero_Telefono"),
                rs.getString("Email"),
                rs.getDate("Fecha_Nacimiento"),
                rs.getInt("Edad"),
                rs.getString("Contraseña"),
                rs.getInt("Salt"),
                rs.getInt("Codigo_Verificacion")
        );
    }
}
