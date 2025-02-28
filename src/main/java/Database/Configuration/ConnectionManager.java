package Database.Configuration;

import Exceptions.DatabaseClosingException;
import Interfaces.IDataBaseConnection;
import Utils.Utility.CustomLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class responsible for managing the connection driver to the database.
 * It provides a thread-safe connection instance for MySQL.
 */

public class ConnectionManager implements IDataBaseConnection {
    private static volatile ConnectionManager instance;
    private Connection connection;
    private static final Logger LOGGER = CustomLogger.getLogger(ConnectionManager.class);

    // Private constructor to prevent instantiation
    private ConnectionManager() {
    }

    /**
     * Provides a thread-safe singleton instance of ConnectionManager.
     *
     * @return Singleton instance of ConnectionManager.
     */

    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void connectToDatabase() throws SQLException {
        String server = "localhost";
        String port = "3306";
        String databaseName = "CuidarteDB";
        String username = System.getenv("MYSQL_USERNAME");
        String password = System.getenv("MYSQL_PASSWORD");

        if (username == null || password == null) {
            throw new SQLException("Database credentials are not set in environment variables.");
        }

        String connectionString = String.format(
                "jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false",
                server, port, databaseName);

        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            LOGGER.info("Database connection established successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to the database.", e);
            throw e;
        }
    }

    @Override
    public void disconnectFromDatabase() throws SQLException, DatabaseClosingException {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection.", e);
                throw new DatabaseClosingException("Failed to close database connection.");
            }
        } else {
            throw new DatabaseClosingException("No active database connection found.");
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
