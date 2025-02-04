package MySQL.DataBase;

import Models.Patient;
import MySQL.Interfaces.GenericOperations;

import java.util.List;

public class PatientImplementation implements GenericOperations<Patient> {

    @Override
    public void save(Patient entity) {

    }

    @Override
    public void update(Patient entity) {

    }

    @Override
    public void delete(Patient entity) {

    }

    @Override
    public Patient findById(int id) {
        return null;
    }

    @Override
    public List<Patient> findAll() {
        return List.of();
    }
}
