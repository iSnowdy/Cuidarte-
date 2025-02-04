package MySQL.Interfaces;

import java.util.List;

// Generic implementations of a CRUD for MySQL. If any other data model needs further specifications, it will implement
// an additional interface that extends from this one

public interface GenericOperations<T> {
    int tupplesAffected = 0;

    void save(T entity);
    void update(T entity);
    void delete(T entity);
    T findById(int id);
    List<T> findAll();
}
