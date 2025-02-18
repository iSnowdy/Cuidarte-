package DAO;

import Exceptions.*;
import Models.DiagnosticTest;
import Models.Enums.TestType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class DiagnosticTestDAO extends BaseDAO<DiagnosticTest, Integer> {

    public DiagnosticTestDAO() throws DatabaseOpeningException {
        super();
    }

    @Override
    public boolean save(DiagnosticTest entity) throws DatabaseInsertException {
        String query =
                "INSERT INTO " +
                "pruebas_diagnosticas (ID_Historia_Clinica, Tipo_Prueba, Fecha_Realizacion, Resultados) " +
                "VALUES (?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getMedicalReportID(),
                    entity.getTestType().name(),
                    entity.getTestDate(),
                    entity.getResults()
            );

            if (result) LOGGER.info("Inserted diagnostic test: "
                    + entity.getTestType() + " for history ID: " + entity.getMedicalReportID());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting diagnostic test: " + entity.getTestType(), e);
            throw new DatabaseInsertException("Failed to insert diagnostic test");
        }
    }

    @Override
    public boolean update(DiagnosticTest entity) throws DatabaseQueryException {
        String query = "UPDATE pruebas_diagnosticas SET ID_Historia_Clinica = ?, Tipo_Prueba = ?, Fecha_Realizacion = ?, Resultados = ? WHERE ID_Prueba = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getMedicalReportID(),
                    entity.getTestType().name(),
                    entity.getTestDate(),
                    entity.getResults(),
                    entity.getDiagnosticTestID()
            );

            if (result) {
                LOGGER.info("Updated diagnostic test with ID: " + entity.getDiagnosticTestID());
            }
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating diagnostic test with ID: " + entity.getDiagnosticTestID(), e);
            throw new DatabaseUpdateException("Failed to update diagnostic test");
        }
    }

    @Override
    public boolean delete(Integer diagnosticTestID) throws DatabaseDeleteException {
        String query = "DELETE FROM pruebas_diagnosticas WHERE ID_Prueba = ?";

        try {
            boolean result = executeUpdate(query, diagnosticTestID);
            if (result) LOGGER.info("Deleted diagnostic test with ID: " + diagnosticTestID);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting diagnostic test with ID: " + diagnosticTestID, e);
            throw new DatabaseDeleteException("Failed to delete diagnostic test");
        }
    }

    @Override
    public Optional<DiagnosticTest> findById(Integer diagnosticTestID) throws DatabaseQueryException {
        String query = "SELECT * FROM pruebas_diagnosticas WHERE ID_Prueba = ?";

        try (ResultSet resultSet = executeQuery(query, diagnosticTestID)) {
            if (resultSet.next()) {
                LOGGER.info("Found the diagnostic test with ID: " + diagnosticTestID);
                return Optional.of(mapResultSetToDiagnosticTest(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not find the diagnostic test with ID: " + diagnosticTestID, e);
            throw new DatabaseQueryException("Could not find the diagnostic test");
        }
        return Optional.empty();
    }

    @Override
    public List<DiagnosticTest> findAll() throws DatabaseQueryException {
        List<DiagnosticTest> diagnosticTests = new ArrayList<>();
        String query = "SELECT * FROM pruebas_diagnosticas";

        try (ResultSet resultSet = executeQuery(query)) {
            while (resultSet.next()) {
                diagnosticTests.add(mapResultSetToDiagnosticTest(resultSet));
            }
            LOGGER.info("Fetched a total of " + diagnosticTests.size() + " diagnostic tests.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all diagnostic tests from database.", e);
            throw new DatabaseQueryException("Error fetching all diagnostic tests from database");
        }
        return diagnosticTests;
    }

    private DiagnosticTest mapResultSetToDiagnosticTest(ResultSet resultSet) throws SQLException {
        return new DiagnosticTest(
                resultSet.getInt("ID_Prueba"),
                resultSet.getInt("ID_Historia_Clinica"),
                TestType.fromString(resultSet.getString("Tipo_Prueba")), // Convert from MySQL String to ENUM
                resultSet.getDate("Fecha_Realizacion"),
                resultSet.getString("Resultados")
        );
    }
}
