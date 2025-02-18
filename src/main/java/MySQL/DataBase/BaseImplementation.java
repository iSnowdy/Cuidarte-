package MySQL.DataBase;

import DatabaseConfig.ConnectionManager;

import java.sql.*;

/**
 * Base implementation to manage queries in classes that extend it.
 *
 * @param <T> Generic Object. Depending on which class is extending, it will be a Patient, Doctor...
 */

// TODO: Use ConnectionManager's connection. Do not create another one here

public abstract class BaseImplementation<T> {
    protected final ConnectionManager connectionManager = ConnectionManager.getConnectionManager();
    protected int tuplesAffected = 0;

    public BaseImplementation() {

    }

    /**
     * Executes an SQL INSERT, UPDATE or DELETE statement.
     *
     * @param sql    The SQL query to execute (must be an INSERT, UPDATE, or DELETE statement).
     * @param params The parameters to bind in the PreparedStatement.
     * @return True if at least one row was affected, false otherwise.
     */

    protected boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            tuplesAffected = statement.executeUpdate();

            return tuplesAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error while executing update: " + e.getMessage() + "\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executes an SQL SELECT statement. The ResultSet is not closed on purpose so that classes that implement
     * it can map the ResultSet to an Object.
     *
     * @param sql    The SQL query that will be executed (INSERT type statement).
     * @param params The parameters to bind to the PreparedStatement.
     * @return ResultSet if it has any information in it, null otherwise.
     * @throws SQLException If a database access error occurs.
     */

    protected ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connectionManager.getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }

    public int getTuplesAffected() {
        return tuplesAffected;
    }
}
