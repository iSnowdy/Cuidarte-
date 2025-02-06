package MySQL.Interfaces;

import javax.management.openmbean.InvalidOpenTypeException;
import java.util.List;
import java.util.Optional;

// Generic implementations of a CRUD for MySQL. If any other data model needs further specifications, it will implement
// an additional interface that extends from this one

/**
 * Defines the behaviour of DAO's for the Database
 *
 * @param <T> Generic Object that we wish to persist into the Database
 */

// TODO: Consider implementing a method to check whether an object that is about to be inserted into the DB is already
//       there or not. Example: call to findAll(). Comparator with each with a call to equals()

public interface GenericOperations<T> {
    boolean save(T entity);

    boolean update(T entity);

    boolean delete(T entity);

    Optional<T> findByDNI(String DNI);

    List<T> findAll();
}
