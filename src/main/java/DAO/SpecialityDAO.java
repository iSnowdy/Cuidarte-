package DAO;

import Exceptions.*;
import Models.Speciality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class SpecialityDAO extends BaseDAO<Speciality, Integer> {

    public SpecialityDAO() throws DatabaseOpeningException {
        super();
    }

    @Override
    public boolean save(Speciality entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO " +
                "especialidades (Nombre, Descripcion) " +
                "VALUES (?, ?)";

        try {
            boolean result = executeUpdate(query, entity.getName(), entity.getDescription());
            if (result) LOGGER.info("Inserted speciality: " + entity.getName());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting speciality: " + entity.getName(), e);
            throw new DatabaseInsertException("Failed to insert speciality");
        }
    }

    @Override
    public boolean update(Speciality entity) throws DatabaseQueryException {
        String query =
                "UPDATE especialidades " +
                "SET Nombre = ?, Descripcion = ? " +
                "WHERE ID_Especialidad = ?";

        try {
            boolean result = executeUpdate(query, entity.getName(), entity.getDescription(), entity.getId());
            if (result) LOGGER.info("Updated speciality: " + entity.getName());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating speciality: " + entity.getName(), e);
            throw new DatabaseUpdateException("Failed to update speciality");
        }
    }

    @Override
    public boolean delete(Integer specialityID) throws DatabaseDeleteException {
        String query = "DELETE FROM especialidades WHERE ID_Especialidad = ?";

        try {
            boolean result = executeUpdate(query, specialityID);
            if (result) LOGGER.info("Deleted speciality with ID: " + specialityID);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting speciality with ID: " + specialityID, e);
            throw new DatabaseDeleteException("Failed to delete speciality.");
        }
    }

    @Override
    public Optional<Speciality> findById(Integer specialityID) throws DatabaseQueryException {
        String query = "SELECT * FROM especialidades WHERE ID_Especialidad = " + specialityID;

        try (ResultSet resultSet = executeQuery(query, specialityID)) {
            if (resultSet.next()) {
                LOGGER.info("Found special with ID: " + specialityID);
                return Optional.of(mapResultSetToSpeciality(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to find speciality ID " + specialityID, e);
            throw new DatabaseQueryException("Failed to find speciality");
        }
        return Optional.empty();
    }

    @Override
    public List<Speciality> findAll() throws DatabaseQueryException {
        List<Speciality> specialities = new ArrayList<>();
        String query = "SELECT * FROM especialidades";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                specialities.add(mapResultSetToSpeciality(resultSet));
            }
            LOGGER.info("Found " + specialities.size() + " specialities");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all specialities", e);
            throw new DatabaseQueryException("Failed to retrieve all specialities");
        }
        return specialities;
    }

    private Speciality mapResultSetToSpeciality(ResultSet rs) throws SQLException {
        return new Speciality(
                rs.getInt("ID_Especialidad"),
                rs.getString("Nombre"),
                rs.getString("Descripcion")
        );
    }
}
