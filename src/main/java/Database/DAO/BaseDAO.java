package Database.DAO;

import Database.Configuration.ConnectionManager;
import Exceptions.DatabaseOpeningException;
import Interfaces.IGenericOperations;
import Utils.Utility.CustomLogger;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for Data Access Objects (DAO). Provides generic implementations for
 * database operations such as inserting, updating, deleting, and querying records.
 * <p>
 * This class ensures proper database connection management and error handling.
 * It should be extended by specific DAO implementations.
 * </p>
 *
 * @param <T>  The entity type handled by the DAO.
 * @param <ID> The identifier type of the entity.
 * @see IGenericOperations
 */
public abstract class BaseDAO<T, ID> implements IGenericOperations<T, ID> {

    /**
     * Logger instance for logging database operations and errors.
     */
    protected static final Logger LOGGER = CustomLogger.getLogger(BaseDAO.class);

    /**
     * Database connection instance used for executing queries.
     */
    protected final Connection connection;

    /**
     * Constructs a {@code BaseDAO} and initializes the database connection.
     * <p>
     * Retrieves a database connection using {@link ConnectionManager#getInstance()}.
     * If the connection is null, a {@link DatabaseOpeningException} is thrown.
     * </p>
     *
     * @throws DatabaseOpeningException if the database connection cannot be established.
     */
    public BaseDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
            if (connection == null) {
                throw new DatabaseOpeningException("Database connection is null");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to establish the database connection.", e);
            throw new DatabaseOpeningException("Failed to open the database connection");
        }
    }

    /**
     * Executes an update operation (INSERT, UPDATE, DELETE) in the database.
     * <p>
     * This method prepares a {@link PreparedStatement} with the provided SQL query
     * and parameters. It returns {@code true} if at least one row was affected.
     * </p>
     *
     * @param sql    The SQL query to execute.
     * @param params The parameters to set in the prepared statement.
     * @return {@code true} if the update was successful, {@code false} otherwise.
     * @throws SQLException if a database access error occurs.
     */
    protected boolean executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Executes a SELECT query and returns the result set.
     * <p>
     * This method prepares a {@link PreparedStatement} with the given SQL query
     * and parameters. The caller is responsible for closing the returned
     * {@link ResultSet} after use to prevent resource leaks.
     * </p>
     *
     * @param sql    The SQL query to execute.
     * @param params The parameters to set in the prepared statement.
     * @return A {@link ResultSet} containing the query results.
     * @throws SQLException if a database access error occurs.
     */
    protected ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }
}
