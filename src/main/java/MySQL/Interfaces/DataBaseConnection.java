package MySQL.Interfaces;

import MySQL.DataBase.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBaseConnection {
    void connectToDatabase() throws SQLException;
    void disconnectFromDatabase() throws SQLException;
    Connection getConnection();
}
