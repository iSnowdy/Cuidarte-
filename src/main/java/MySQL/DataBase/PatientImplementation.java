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
     * @param entity The patient entity to be saved.
     * @return true if the entity was successfully saved, false otherwise.
     */

    @Override
    public boolean save(Patient entity) {
        String sql =
                "INSERT INTO pacientes " +
                "(Nombre, Apellidos, Fecha_Nacimiento, Edad, Numero_Telefono, Contraseña, Salt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        return executeUpdate(
                sql,
                entity.getFirstName(),
                entity.getSurname(),
                entity.getDateOfBirth(),
                entity.getAge(),
                entity.getPhoneNumber(),
                entity.getPassword(),
                entity.getSalt()
        );
    }

    /**
     * Updates all the information of an existing patient in the database.
     *
     * @param entity The patient entity to be updated.
     * @return true if the entity was successfully updated, false otherwise.
     */

    @Override
    public boolean update(Patient entity) {
        String sql =
                "UPDATE pacientes " +
                "SET Nombre = ?, Apellidos = ?, Fecha_Nacimiento = ?, Edad = ?, Numero_Telefono = ? " +
                "WHERE ID_Paciente = ?";

        return executeUpdate(
                sql,
                entity.getFirstName(),
                entity.getSurname(),
                entity.getDateOfBirth(),
                entity.getAge(),
                entity.getPhoneNumber()
        );
    }

    /**
     * Deletes an existing patient from the database.
     * @param entity The patient entity to be deleted.
     * @return true if it was successfully deleted, false otherwise.
     */

    @Override
    public boolean delete(Patient entity) {
        String sql =
                "DELETE FROM pacientes " +
                "WHERE ID_Paciente = ?";

        return executeUpdate(
                sql,
                entity.getId()
        );
    }

    /**
     * Finds a specific patient given its ID.
     * @param id The ID of the patient to be retrieved from the database.
     * @return Optional<Patient> An Optional containing the found patient if present. Otherwise, it will be an empty
            * Optional. Implementation should check if it is empty or not using isPresent(), isFalse() or ifPresent().
     */

    @Override
    public Optional<Patient> findById(int id) {
        String sql =
                "SELECT * FROM pacientes " +
                "WHERE ID_Paciente = ?";

        try (ResultSet resultSet = executeQuery(sql, id)) {
            if (resultSet.next()) {
                Patient patientFound = new Patient(
                        resultSet.getInt("ID_Paciente"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Apellidos"),
                        resultSet.getString("Numero_Telefono"),
                        resultSet.getString("Email"),
                        resultSet.getDate("Fecha_Nacimiento"),
                        resultSet.getString("Contraseña"),
                        resultSet.getInt("Edad"),
                        resultSet.getInt("Salt")
                );
                return Optional.of(patientFound);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving patient (" + id + ") by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * Retrieves all patients from the database.
     * @return List<Patient> A list containing all the patients, or empty if no patients were found.
     */

    @Override
    public List<Patient> findAll() {
        String sql =
                "SELECT * FROM pacientes";

        List<Patient> patients = new ArrayList<Patient>();

        try (ResultSet resultSet = executeQuery(sql)) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getInt("ID_Paciente"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Apellidos"),
                        resultSet.getString("Numero_Telefono"),
                        resultSet.getString("Email"),
                        resultSet.getDate("Fecha_Nacimiento"),
                        resultSet.getString("Contraseña"),
                        resultSet.getInt("Edad"),
                        resultSet.getInt("Salt")
                );
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            System.out.println("Error retrieving all patients: " + e.getMessage());
            e.printStackTrace();
        }
        return patients; // Returns an empty list
    }
}
