package MySQL.DataBase;

import Models.Doctor;
import MySQL.Interfaces.GenericOperations;

import java.util.List;
import java.util.Optional;

public class DoctorImplementation implements GenericOperations<Doctor> {

    @Override
    public boolean save(Doctor entity) {

    }

    @Override
    public boolean update(Doctor entity) {

    }

    @Override
    public boolean delete(Doctor entity) {

    }

    @Override
    public Optional<Doctor> findById(int id) {
        return null;
    }

    @Override
    public List<Doctor> findAll() {
        return List.of();
    }
}
