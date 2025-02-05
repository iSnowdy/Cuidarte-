package MySQL.DataBase;

import java.sql.*;

/**
 * Base implementation to manage queries in classes that extend it.
 *
 * @param <T> Generic Object. Depending on which class is extending, it will be a Patient, Doctor...
 */

public abstract class BaseImplementation<T> {
    protected final Connection connection;
    protected int tuplesAffected = 0;
    protected String generatedID;

    public BaseImplementation() {
        Connection temporaryConnection;
        try {
            ConnectionManager.getConnectionManager().connectToDatabase();
            temporaryConnection = ConnectionManager.getConnectionManager().getConnection();
            System.out.println("Connected!");
        } catch (SQLException e) {
            System.out.println("Could not connect to the database in BaseImplementation");
            temporaryConnection = null;
            e.printStackTrace();
        }
        this.connection = temporaryConnection;
    }

    /**
     * Executes an SQL INSERT, UPDATE or DELETE statement.
     *
     * @param sql    The SQL query to execute (must be an INSERT, UPDATE, or DELETE statement).
     * @param params The parameters to bind in the PreparedStatement.
     * @return True if at least one row was affected, false otherwise.
     */

    protected boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

    public String getGeneratedID() {
        return generatedID;
    }
}
