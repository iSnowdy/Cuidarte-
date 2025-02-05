package MySQL.Interfaces;

import Exceptions.DatabaseInsertException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Generic implementations of a CRUD for MySQL. If any other data model needs further specifications, it will implement
// an additional interface that extends from this one

/**
 * Defines the behaviour of DAO's for the Database
 *
 * @param <T> Generic Object that we wish to persist into the Database
 */

public interface GenericOperations<T> {
    boolean save(T entity);

    boolean update(T entity);

    boolean delete(T entity);

    Optional<T> findById(int id);

    List<T> findAll();
}
