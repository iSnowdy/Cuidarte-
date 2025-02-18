package Interfaces;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Models.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    boolean registerPatient(Patient patient) throws DatabaseInsertException;

    boolean updatePatient(Patient patient) throws DatabaseUpdateException;

    boolean changePassword(String DNI, String oldPassword, String newPassword) throws DatabaseUpdateException;

    boolean deletePatient(String DNI) throws DatabaseDeleteException;

    Optional<Patient> getPatientByDNI(String DNI) throws DatabaseQueryException;

    List<Patient> getAllPatients() throws DatabaseQueryException;
}
