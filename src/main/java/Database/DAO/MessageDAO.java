package Database.DAO;

import Database.Models.Doctor;
import Database.Models.Message;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class MessageDAO extends BaseDAO<Message, Integer> {

    public MessageDAO() throws DatabaseOpeningException {
        super();
    }

    @Override
    public boolean save(Message entity) throws DatabaseInsertException {
        String query = "INSERT INTO mensajes (DNI_Paciente, DNI_Medico, Fecha_Mensaje, Contenido) VALUES (?, ?, ?, ?)";

        try {
            boolean result = executeUpdate(
                    query,
                    entity.getPatientDNI(),
                    entity.getDoctorDNI(),
                    entity.getDate(),
                    entity.getMessageContent()
            );
            return result;
        } catch (SQLException e) {
            throw new DatabaseInsertException("Failed to insert message");
        }
    }

    @Override
    public boolean update(Message entity) throws DatabaseQueryException {
        return false;
    }

    @Override
    public boolean delete(Integer id) throws DatabaseDeleteException {
        String query = "DELETE FROM mensajes WHERE ID_Mensaje = ?";

        try {
            boolean result = executeUpdate(query, id);
            if (result) LOGGER.info("Deleted message with ID: " + id);
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting message with ID: " + id, e);
            throw new DatabaseDeleteException("Failed to delete message");
        }
    }

    @Override
    public Optional<Message> findById(Integer id) throws DatabaseQueryException {
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() throws DatabaseQueryException {
        return List.of();
    }

    public List<Message> findMessagesByPatient(String patientDNI) throws DatabaseQueryException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM mensajes WHERE DNI_Paciente = ? ORDER BY Fecha_Mensaje DESC";

        try (ResultSet resultSet = executeQuery(query, patientDNI)) {
            while (resultSet.next()) {
                messages.add(mapResultSetToMessage(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseQueryException("Failed to retrieve messages for patient: " + patientDNI);
        }
        return messages;
    }

    private Message mapResultSetToMessage(ResultSet resultSet) throws SQLException {
        return new Message(
                resultSet.getInt("ID_Mensaje"),
                resultSet.getString("DNI_Paciente"),
                resultSet.getString("DNI_Medico"),
                resultSet.getTimestamp("Fecha_Mensaje"),
                resultSet.getString("Contenido")
        );
    }
}
