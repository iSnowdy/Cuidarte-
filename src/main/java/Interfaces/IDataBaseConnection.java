package Interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataBaseConnection {
    void connectToDatabase() throws SQLException;

    void disconnectFromDatabase() throws SQLException;

    Connection getConnection();
}
