package MySQL.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base implementation to manage queries in classes that extend it.
 *
 * @param <T> Generic Object. Depending on which class is extending, it will be a Patient, Doctor...
 */

public abstract class BaseImplementation<T> {
    protected final Connection connection;
    protected int tuplesAffected = 0;
    protected int generatedID;

    public BaseImplementation() {
        this.connection = ConnectionManager.getConnectionManager().getConnection();
    }

    /**
     * Executes an SQL INSERT, UPDATE or DELETE statement and stores how many tuples were affected.
     *
     * @param sql    The SQL query to execute (must be an INSERT, UPDATE, or DELETE statement).
     * @param params The parameters to bind in the PreparedStatement.
     * @return True if at least one row was affected and ID generated, false otherwise.
     */

    protected boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            tuplesAffected = statement.executeUpdate();

            return isPatientInserted(statement);
        } catch (SQLException e) {
            System.out.println("Error while executing update: " + e.getMessage() + "\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method that will recover the ID of the inserted Object in the database if the operation was
     * successful.
     *
     * @param statement PreparedStatement previously executed on the caller method.
     * @return True if the insert has been executed and the ID retrieved from the database, false otherwise.
     * @throws SQLException If a database access error occurs.
     */

    private boolean isPatientInserted(PreparedStatement statement) throws SQLException {
        if (tuplesAffected > 0) {
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedID = resultSet.getInt(1);
                    return true;
                }
            }
        }
        return false;
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
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }


    /**
     * Returns the amount of tuples affected in an operation.
     */

    public int getTuplesAffected() {
        return tuplesAffected;
    }

    public int getGeneratedID() {
        return generatedID;
    }
}
