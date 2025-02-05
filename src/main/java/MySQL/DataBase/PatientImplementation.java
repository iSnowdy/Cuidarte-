package MySQL.DataBase;

import Models.Patient;
import MySQL.Interfaces.GenericOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Implementation class for performing CRUD operations on "pacientes" table in the database.
 * <p>
 * It implements GenericOperations por Patient type of entity. Extends for the base implementations
 * of queryUpdate() and queryExecute().
 */

public class PatientImplementation extends BaseImplementation<Patient> implements GenericOperations<Patient> {

    /**
     * Saves a new patient into the database.
     *
     * @param entity The patient entity to be saved.
     * @return True if the entity was successfully saved, false otherwise.
     */

    @Override
    public boolean save(Patient entity) {
        String sql =
                "INSERT INTO pacientes " +
                        "(DNI_Paciente, Nombre, Apellidos, Numero_Telefono, Email, Fecha_Nacimiento, Edad,  Contraseña, Salt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return executeUpdate(
                sql,
                entity.getDNI(),
                entity.getFirstName(),
                entity.getSurname(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getDateOfBirth(),
                entity.getAge(),
                entity.getPassword(),
                entity.getSalt()
        );
    }

    /**
     * Updates all the information of an existing patient in the database. Some fields may not be updated.
     *
     * @param entity The patient entity to be updated.
     * @return True if the entity was successfully updated, false otherwise.
     */

    @Override
    public boolean update(Patient entity) {
        String sql =
                "UPDATE pacientes " +
                "SET Nombre = ?, Apellidos = ?, Fecha_Nacimiento = ?, Edad = ?, Numero_Telefono = ? " +
                "WHERE DNI_Paciente = ?";

        return executeUpdate(
                sql,
                entity.getFirstName(),
                entity.getSurname(),
                entity.getDateOfBirth(),
                entity.getAge(),
                entity.getPhoneNumber(),
                entity.getDNI()
        );
    }

    /**
     * Deletes an existing patient from the database.
     *
     * @param entity The patient entity to be deleted.
     * @return True if it was successfully deleted, false otherwise.
     */

    @Override
    public boolean delete(Patient entity) {
        String sql =
                "DELETE FROM pacientes " +
                "WHERE DNI_Paciente = ?";

        return executeUpdate(
                sql,
                entity.getDNI()
        );
    }

    /**
     * Finds a specific patient given its DNI.
     *
     * @param DNI The ID of the patient to be retrieved from the database.
     * @return Optional<Patient> An Optional containing the found patient if present. Otherwise, it will be an empty
     * Optional. Implementation should check if it is empty or not using isPresent(), isFalse() or ifPresent().
     */

    @Override
    public Optional<Patient> findByDNI(String DNI) {
        String sql =
                "SELECT * FROM pacientes " +
                "WHERE DNI_Paciente = ?";

        try (ResultSet resultSet = executeQuery(sql, DNI)) {
            if (resultSet.next()) {
                Patient patientFound = mapResultSetToPatient(resultSet);
                return Optional.of(patientFound);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving Patient (DNI: " + DNI + ") by DNI.\n " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * Retrieves all patients from the database.
     *
     * @return List<Patient> A list containing all the patients, or empty if no patients were found.
     */

    @Override
    public List<Patient> findAll() {
        String sql =
                "SELECT * FROM pacientes";

        List<Patient> patients = new ArrayList<Patient>();

        try (ResultSet resultSet = executeQuery(sql)) {
            while (resultSet.next()) {
                Patient patient = mapResultSetToPatient(resultSet);
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            System.out.println("Error retrieving all patients: " + e.getMessage());
            e.printStackTrace();
        }
        return patients; // Returns an empty list
    }

    /**
     * Auxiliary method to map a given ResultSet obtained from a SELECT query to the Patient object.
     *
     * @param resultSet The ResultSet that contains the information in the database of the object.
     * @return Patient The object that is being mapped from the database to memory.
     * @throws SQLException If a database access error occurs.
     */

    private Patient mapResultSetToPatient(ResultSet resultSet) throws SQLException {
        return new Patient(
                resultSet.getString("DNI_Paciente"),
                resultSet.getString("Nombre"),
                resultSet.getString("Apellidos"),
                resultSet.getString("Numero_Telefono"),
                resultSet.getString("Email"),
                resultSet.getDate("Fecha_Nacimiento"),
                resultSet.getInt("Edad"),
                resultSet.getString("Contraseña"),
                resultSet.getInt("Salt")
        );
    }
}
