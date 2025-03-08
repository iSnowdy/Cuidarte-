package Interfaces;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Database.Models.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    boolean registerPatient(Patient patient) throws DatabaseInsertException;
    Optional<Patient> getPatientByDNI(String DNI) throws DatabaseQueryException;
}
