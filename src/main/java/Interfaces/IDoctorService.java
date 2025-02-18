package Interfaces;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Models.Doctor;

import java.util.List;
import java.util.Optional;

public interface IDoctorService {
    boolean registerDoctor(Doctor doctor) throws DatabaseInsertException;

    boolean updateDoctor(Doctor doctor) throws DatabaseUpdateException;

    boolean deleteDoctor(String DNI) throws DatabaseDeleteException;

    Optional<Doctor> getDoctorByDNI(String DNI) throws DatabaseQueryException;

    List<Doctor> getAllDoctors() throws DatabaseQueryException;

    List<Doctor> getDoctorsBySpecialty(String specialty) throws DatabaseQueryException;
}
