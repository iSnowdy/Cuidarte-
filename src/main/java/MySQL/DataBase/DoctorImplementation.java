package MySQL.DataBase;

import Models.Doctor;
import Interfaces.GenericOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Implementation class for performing CRUD operations on "medicos" table in the database.
 * <p>
 * It implements GenericOperations por Doctor type of entity. Extends for the base implementations
 * of queryUpdate() and queryExecute().
 */

public class DoctorImplementation extends BaseImplementation<Doctor> implements GenericOperations<Doctor> {

    /**
     * Saves a new doctor into the database.
     *
     * @param entity The doctor entity to be persisted.
     * @return True if the query was executed successfully, false otherwise
     */

    @Override
    public boolean save(Doctor entity) {
        String sql =
                "INSERT INTO medicos " +
                "(DNI_Medico, Nombre, Apellidos, Numero_Telefono, Email, Especialidad) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        return executeUpdate(
                sql,
                entity.getDNI(),
                entity.getFirstName(),
                entity.getSurname(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getSpecialty()
        );
    }

    /**
     * Updates all the information related to an existing doctor in the database. Some fields may not be updated.
     *
     * @param entity The doctor to be updated.
     * @return True if the entity was updated, false otherwise
     */

    @Override
    public boolean update(Doctor entity) {
        String sql =
                "UPDATE medicos " +
                "SET Nombre = ?, Apellidos = ?, Numero_Telefono = ?, Email = ?, Especialidad = ? " +
                "WHERE DNI_Medico = ?";

        return executeUpdate(
                sql,
                entity.getFirstName(),
                entity.getSurname(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getSpecialty(),
                entity.getDNI()
        );
    }

    /**
     * Deletes an existing doctor entity from the database.
     *
     * @param entity The doctor to be deleted.
     * @return True if the operation was successful, false otherwise
     */

    @Override
    public boolean delete(Doctor entity) {
        String sql =
                "DELETE FROM medicos " +
                "WHERE DNI_Medico = ?";

        return executeUpdate(
                sql,
                entity.getDNI()
        );
    }

    /**
     * Finds a specific doctor in the database given their DNI.
     *
     * @param DNI The ID of the doctor to be retrieved from the database.
     * @return Optional<Patient> An Optional Object containing the doctor if it was present. Otherwise,
     * it will be an empty Optional. Implementation of this method should check if it is empty or not using
     * isPresent(), isFalse() or ifPresent().
     */

    @Override
    public Optional<Doctor> findPatientByDNI(String DNI) {
        String sql =
                "SELECT * FROM medicos " +
                "WHERE DNI_Medico = ?";

        try (ResultSet resultSet = executeQuery(sql, DNI)) {
            Doctor doctor = mapResultSetToDoctor(resultSet);
            return Optional.of(doctor);
        } catch (SQLException e) {
            System.out.println("Error retrieving Doctor (DNI: " + DNI + ") by DNI.\n " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Retrieves all the doctors from the database.
     *
     * @return List<Doctor> A list containing all the doctors found in the database. If none was found, it will be
     * an empty List.
     */

    @Override
    public List<Doctor> findAllPatients() {
        String sql =
                "SELECT * FROM medicos";

        List<Doctor> doctors = new ArrayList<Doctor>();

        try (ResultSet resultSet = executeQuery(sql)) {
            while (resultSet.next()) {
                Doctor doctor = mapResultSetToDoctor(resultSet);
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving all doctors.\n " + e.getMessage());
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Auxiliary method to map a given ResultSet obtained from a SELECT query to the Doctor object.
     *
     * @param resultSet The ResultSet that contains the information in the database of the object.
     * @return Patient The object that is being mapped from the database to memory.
     * @throws SQLException If a database access error occurs.
     */

    private Doctor mapResultSetToDoctor(ResultSet resultSet) throws SQLException {
        return new Doctor(
                resultSet.getString("DNI_Medico"),
                resultSet.getString("Nombre"),
                resultSet.getString("Apellidos"),
                resultSet.getString("Numero_Telefono"),
                resultSet.getString("Email"),
                resultSet.getString("Especialidad")
        );
    }
}
