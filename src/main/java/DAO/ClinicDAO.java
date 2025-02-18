package DAO;

import Exceptions.*;
import Models.Clinic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class ClinicDAO extends BaseDAO<Clinic, Integer> {

    public ClinicDAO() throws DatabaseOpeningException {
        super();
    }


    @Override
    public boolean save(Clinic entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO " +
                "clinicas (Nombre, Direccion, Email, Telefono) " +
                "VALUES (?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getName(),
                    entity.getAddress(),
                    entity.getEmail(),
                    entity.getPhoneNumber()
            );
            if (result) LOGGER.info("Inserted clinic: " + entity.getName());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting clinic: " + entity.getName(), e);
            throw new DatabaseInsertException("Failed to insert clinic");
        }
    }

    @Override
    public boolean update(Clinic entity) throws DatabaseQueryException {
        String query =
                "UPDATE clinicas " +
                "SET Nombre = ?, Direccion = ?, Email = ?, Telefono = ? " +
                "WHERE ID_Clinica = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getName(),
                    entity.getAddress(),
                    entity.getEmail(),
                    entity.getPhoneNumber(),
                    entity.getId()
            );
            if (result) LOGGER.info("Updated clinic: " + entity.getName());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating clinic: " + entity.getName(), e);
            throw new DatabaseUpdateException("Failed to update clinic");
        }
    }

    @Override
    public boolean delete(Integer clinicID) throws DatabaseDeleteException {
        String query = "DELETE FROM clinicas WHERE ID_Clinica = ?";

        try {
            boolean result = executeUpdate(query, clinicID);
            if (result) LOGGER.info("Deleted clinic with ID: " + clinicID);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting clinic with ID: " + clinicID, e);
            throw new DatabaseDeleteException("Failed to delete clinic");
        }
    }

    @Override
    public Optional<Clinic> findById(Integer clinicID) throws DatabaseQueryException {
        String query = "SELECT * FROM clinics WHERE ID_Clinica = " + clinicID;

        try (ResultSet resultSet = executeQuery(query, clinicID)) {
            if (resultSet.next()) {
                LOGGER.info("Successfully retrieved clinic with ID " + clinicID + " from database");
                return Optional.of(mapResultSetToClinic(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error trying to find the clinic with ID " + clinicID, e);
            throw new DatabaseQueryException("Error trying to select a clinic");
        }
        return Optional.empty();
    }

    @Override
    public List<Clinic> findAll() throws DatabaseQueryException {
        List<Clinic> clinics = new ArrayList<>();
        String query = "SELECT * FROM clinicas";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                clinics.add(mapResultSetToClinic(resultSet));
            }
            LOGGER.info("Successfully retrieved clinics (" + clinics.size() + ") from database");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error selecting all clinics", e);
            throw new DatabaseQueryException("Error selecting all clinics", e);
        }
        return clinics;
    }

    private Clinic mapResultSetToClinic(ResultSet rs) throws SQLException {
        return new Clinic(
                rs.getInt("ID_Clinica"),
                rs.getString("Nombre"),
                rs.getString("Direccion"),
                rs.getString("Email"),
                rs.getString("Telefono")
        );
    }
}
