package Interfaces;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;

import java.util.List;
import java.util.Optional;

/**
 * Defines the behaviour of DAO's for the Database
 *
 * @param <T>  Generic Object that we wish to persist into the Database
 * @param <ID> Generic Object that will be used to find {@code <T>}. Could be a {@code String}, {@code int}...
 */

public interface IGenericOperations<T, ID> {
    boolean save(T entity) throws DatabaseInsertException;

    boolean update(T entity) throws DatabaseQueryException;

    boolean delete(ID id) throws DatabaseDeleteException;

    Optional<T> findById(ID id) throws DatabaseQueryException;

    List<T> findAll() throws DatabaseQueryException;
}
