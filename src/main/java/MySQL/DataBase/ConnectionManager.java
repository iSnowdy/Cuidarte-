package MySQL.DataBase;

import Exceptions.DatabaseClosingException;
import MySQL.Interfaces.DataBaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager implements DataBaseConnection {
    // Singleton; only one instance of this class will be shared across the entire project
    private static ConnectionManager connectionManager;
    private Connection connection;

    // To avoid other classes from implementing this class
    private ConnectionManager() {}

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
        String port = "5432";
        String databaseName = "CuidarteDB";
        String username = "root";
        String password = "root";

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
