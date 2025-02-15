package MySQL.DataBase;

import Exceptions.DatabaseClosingException;
import MySQL.Interfaces.DataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class (one instance only across the project) that is in charge of managing the connection driver
 * to the database.
 * <p>
 * It will open the Connection to MySQL. And share it when needed using getters.
 */

public class ConnectionManager implements DataBaseConnection {
    // Singleton; only one instance of this class will be shared across the entire project
    private static ConnectionManager connectionManager;
    private Connection connection;

    // To avoid other classes from implementing this class
    private ConnectionManager() {}

    /**
     * If the ConnectionManager class was not implemented yet, it will do it. Then, it will return the
     * class so the caller can use the Connection.
     *
     * @return ConnectionManager Returns the class itself. It has a private constructor, so this is needed.
     */

    public static ConnectionManager getConnectionManager() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }

    // TODO: Somehow make more secure the username and password?
    @Override
    public void connectToDatabase() throws SQLException {
        String server = "localhost";
        String port = "3306";
        String databaseName = "CuidarteDB";
        String username = System.getenv("MySQL_Username");
        String password = System.getenv("MySQL_Password");

        String connectionString = String.format("jdbc:mysql://%s:%s/%s", server, port, databaseName);

        connection = DriverManager.getConnection(connectionString, username, password);
    }

    @Override
    public void disconnectFromDatabase() throws SQLException, DatabaseClosingException {
        if (connection != null) {
            connection.close();
        } else {
            throw new DatabaseClosingException("No instance of MySQL connection");
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
