package Database.DAO;

import Database.Models.*;
import Database.Models.Enums.TestType;
import Exceptions.*;

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
    public boolean update(DiagnosticTest entity) throws DatabaseUpdateException {
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

    // Retrieves all diagnostic tests for a patient by DNI
    public List<DiagnosticTest> findByPatientDNI(String dni) throws DatabaseQueryException {
        List<DiagnosticTest> diagnosticTests = new ArrayList<>();
        String query = "SELECT pd.* FROM pruebas_diagnosticas pd " +
                "JOIN historias_clinicas hc ON pd.ID_Historia_Clinica = hc.ID_Historia_Clinica " +
                "WHERE hc.DNI_Paciente = ?";

        try (ResultSet resultSet = executeQuery(query, dni)) {
            while (resultSet.next()) {
                diagnosticTests.add(mapResultSetToDiagnosticTest(resultSet));
            }
            LOGGER.info("Found " + diagnosticTests.size() + " diagnostic tests for patient DNI: " + dni);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching diagnostic tests for patient: " + dni, e);
            throw new DatabaseQueryException("Could not fetch diagnostic tests");
        }
        return diagnosticTests;
    }

    // Retrieves detailed information for a specific diagnostic test based on type
    public Optional<Object> findDetailedTestByID(int testID, TestType testType) throws DatabaseQueryException {
        String query = "";

        switch (testType) {
            case BLOOD_LAB:
                query = "SELECT * FROM hemogramas WHERE ID_Prueba = ?";
                break;
            case BIOCHEMISTRY_LAB:
                query = "SELECT * FROM bioquimicas WHERE ID_Prueba = ?";
                break;
            case IMMUNOLOGY_LAB:
                query = "SELECT * FROM inmunologias WHERE ID_Prueba = ?";
                break;
            case MICROBIOLOGY_LAB:
                query = "SELECT * FROM microbiologias WHERE ID_Prueba = ?";
                break;
        }

        try (ResultSet resultSet = executeQuery(query, testID)) {
            if (resultSet.next()) {
                LOGGER.info("Detailed " + testType + " test found for test ID: " + testID);
                return Optional.of(mapDetailedTest(resultSet, testType));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching detailed " + testType + " test for ID: " + testID, e);
            throw new DatabaseQueryException("Could not fetch detailed test information");
        }
        return Optional.empty();
    }

    // Maps the result set to the appropriate detailed test object based on test type
    private Object mapDetailedTest(ResultSet rs, TestType testType) throws SQLException {
        switch (testType) {
            case BLOOD_LAB:
                return new BloodCountLab(
                        rs.getInt("ID_Hemograma"),
                        rs.getInt("ID_Prueba"),
                        rs.getDouble("Hematies"),
                        rs.getDouble("Hemoglobina"),
                        rs.getDouble("Hematocrito"),
                        rs.getDouble("VCM"),
                        rs.getDouble("HCM"),
                        rs.getDouble("CHCM"),
                        rs.getDouble("ADE"),
                        rs.getInt("Plaquetas"),
                        rs.getDouble("VPM"),
                        rs.getDouble("ADP"),
                        rs.getDouble("Plaquetocrito"),
                        rs.getDouble("Leucocitos"),
                        rs.getDouble("Neutrofilos"),
                        rs.getDouble("Linfocitos"),
                        rs.getDouble("Monocitos"),
                        rs.getDouble("Eosinofilos"),
                        rs.getDouble("Basofilos")
                );

            case BIOCHEMISTRY_LAB:
                return new BiochemistryLab(
                        rs.getInt("ID_Bioquimica"),
                        rs.getInt("ID_Prueba"),
                        rs.getInt("Glucemia"),
                        rs.getInt("Urea"),
                        rs.getDouble("Creatina"),
                        rs.getDouble("Acido_Urico"),
                        rs.getInt("Colesterol_Total"),
                        rs.getInt("Colesterol_HDL"),
                        rs.getInt("Colesterol_LDL"),
                        rs.getInt("Trigliceridos"),
                        rs.getInt("GOT_AST"),
                        rs.getInt("GPT_ALT"),
                        rs.getInt("Gamma_GT"),
                        rs.getInt("Fosfatasa_Alcalina"),
                        rs.getDouble("Bilirrubina_Total")
                );

            case IMMUNOLOGY_LAB:
                return new ImmunologyLab(
                        rs.getInt("ID_Inmunologia"),
                        rs.getInt("ID_Prueba"),
                        rs.getInt("Linfocitos_T_CD4"),
                        rs.getInt("Linfocitos_T_CD4_Recuento"),
                        rs.getInt("Linfocitos_T_CD8"),
                        rs.getInt("Linfocitos_T_CD8_Recuento"),
                        rs.getDouble("Indice_CD4CD8")
                );

            case MICROBIOLOGY_LAB:
                return new MicrobiologyLab(
                        rs.getInt("ID_Microbiologia"),
                        rs.getInt("ID_Prueba"),
                        rs.getString("Resultado")
                );

            default:
                return null;
        }
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
