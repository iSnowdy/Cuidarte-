package DAO;

import Exceptions.*;
import Models.MedicalReport;
import Utils.Utility.CustomLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicalReportDAO extends BaseDAO<MedicalReport, Integer> {
    private static final Logger LOGGER = CustomLogger.getLogger(MedicalReportDAO.class);

    public MedicalReportDAO() throws DatabaseOpeningException {
        super();
    }

    @Override
    public boolean save(MedicalReport entity) throws DatabaseInsertException {
        String query = "INSERT INTO historias_clinicas " +
                "(DNI_Paciente, DNI_Medico, Fecha_Visita, Temperatura, Sistolica, Diastolica, Frecuencia_Cardiaca, " +
                "Saturacion_O2, Peso, Altura, Antecedentes, Alergias, Diagnosis, Motivo_Consulta, Exploracion_Fisica, Tratamiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getPatientDNI(),
                    entity.getDoctorDNI(),
                    entity.getVisitDate(),
                    entity.getTemperature(),
                    entity.getSystolic(),
                    entity.getDiastolic(),
                    entity.getHeartRate(),
                    entity.getSaturation(),
                    entity.getWeight(),
                    entity.getHeight(),
                    entity.getMedicalHistory(),
                    entity.getAllergies(),
                    entity.getDiagnosis(),
                    entity.getAppointmentMotive(),
                    entity.getPhysicalExploration(),
                    entity.getTreatment()
            );
            if (result) LOGGER.info("Inserted medical report for patient: " + entity.getPatientDNI());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting medical report", e);
            throw new DatabaseInsertException("Failed to insert medical report.");
        }
    }

    @Override
    public boolean update(MedicalReport entity) throws DatabaseUpdateException {
        String query = "UPDATE historias_clinicas SET " +
                "DNI_Paciente = ?, DNI_Medico = ?, Fecha_Visita = ?, Temperatura = ?, Sistolica = ?, " +
                "Diastolica = ?, Frecuencia_Cardiaca = ?, Saturacion_O2 = ?, Peso = ?, Altura = ?, " +
                "Antecedentes = ?, Alergias = ?, Diagnosis = ?, Motivo_Consulta = ?, Exploracion_Fisica = ?, Tratamiento = ? " +
                "WHERE ID_Historia_Clinica = ?";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getPatientDNI(),
                    entity.getDoctorDNI(),
                    entity.getVisitDate(),
                    entity.getTemperature(),
                    entity.getSystolic(),
                    entity.getDiastolic(),
                    entity.getHeartRate(),
                    entity.getSaturation(),
                    entity.getWeight(),
                    entity.getHeight(),
                    entity.getMedicalHistory(),
                    entity.getAllergies(),
                    entity.getDiagnosis(),
                    entity.getAppointmentMotive(),
                    entity.getPhysicalExploration(),
                    entity.getTreatment(),
                    entity.getId()
            );
            if (result) LOGGER.info("Updated medical report ID: " + entity.getId());
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating medical report ID: " + entity.getId(), e);
            throw new DatabaseUpdateException("Failed to update medical report.");
        }
    }

    @Override
    public boolean delete(Integer id) throws DatabaseDeleteException {
        String query = "DELETE FROM historias_clinicas WHERE ID_Historia_Clinica = ?";

        try {
            boolean result = executeUpdate(query, id);
            if (result) LOGGER.info("Deleted medical report ID: " + id);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting medical report ID: " + id, e);
            throw new DatabaseDeleteException("Failed to delete medical report.");
        }
    }

    @Override
    public Optional<MedicalReport> findById(Integer id) throws DatabaseQueryException {
        String query = "SELECT * FROM historias_clinicas WHERE ID_Historia_Clinica = ?";

        try (ResultSet rs = executeQuery(query, id)) {
            if (rs.next()) {
                LOGGER.info("Fetched medical report ID: " + id);
                return Optional.of(mapResultSetToMedicalReport(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching medical report ID: " + id, e);
            throw new DatabaseQueryException("Failed to execute SELECT query.");
        }
        return Optional.empty();
    }

    @Override
    public List<MedicalReport> findAll() throws DatabaseQueryException {
        List<MedicalReport> reports = new ArrayList<>();
        String query = "SELECT * FROM historias_clinicas";

        try (ResultSet rs = executeQuery(query)) {
            while (rs.next()) {
                reports.add(mapResultSetToMedicalReport(rs));
            }
            LOGGER.info("Fetched all medical reports successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching medical reports.", e);
            throw new DatabaseQueryException("Failed to execute SELECT query.");
        }
        return reports;
    }

    public List<MedicalReport> findByPatientDNI(String dni) throws DatabaseQueryException {
        List<MedicalReport> reports = new ArrayList<>();
        String query = "SELECT * FROM historias_clinicas WHERE DNI_Paciente = ? ORDER BY Fecha_Visita DESC";

        try (ResultSet rs = executeQuery(query, dni)) {
            while (rs.next()) {
                reports.add(mapResultSetToMedicalReport(rs));
            }
            LOGGER.info("Fetched medical reports for patient DNI: " + dni);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching medical reports for patient DNI: " + dni, e);
            throw new DatabaseQueryException("Failed to execute SELECT query.");
        }
        return reports;
    }

    private MedicalReport mapResultSetToMedicalReport(ResultSet rs) throws SQLException {
        return new MedicalReport(
                rs.getString("DNI_Paciente"),
                rs.getString("DNI_Medico"),
                rs.getString("Antecedentes"),
                rs.getString("Alergias"),
                rs.getString("Motivo_Consulta"),
                rs.getString("Exploracion_Fisica"),
                rs.getString("Tratamiento"),
                rs.getInt("Sistolica"),
                rs.getInt("Diastolica"),
                rs.getInt("Frecuencia_Cardiaca"),
                rs.getInt("Saturacion_O2"),
                rs.getFloat("Temperatura"),
                rs.getFloat("Peso"),
                rs.getFloat("Altura"),
                rs.getTimestamp("Fecha_Visita")
        );
    }
}
