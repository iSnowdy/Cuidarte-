package DAO;

import DatabaseConfig.ConnectionManager;
import Exceptions.DatabaseOpeningException;
import Interfaces.IGenericOperations;
import Utils.Utility.CustomLogger;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseDAO <T, ID> implements IGenericOperations<T, ID> {
    protected static final Logger LOGGER = CustomLogger.getLogger(BaseDAO.class);
    protected final Connection connection;

    public BaseDAO() {
        try {
            this.connection = ConnectionManager.getInstance().getConnection();
            if (connection == null) throw new DatabaseOpeningException("Database connection is null");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to establish the database connection.", e);
            throw new DatabaseOpeningException("Failed to open the database connection");
        }
    }

    // INSERT, UPDATE, DELETE
    protected boolean executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // SELECT
    protected ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }
}
