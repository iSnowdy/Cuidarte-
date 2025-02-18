package Interfaces;

import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Exceptions.DatabaseUpdateException;
import Models.Clinic;

import java.util.List;
import java.util.Optional;

public interface IClinicService {
    boolean registerClinic(Clinic clinic) throws DatabaseInsertException;

    boolean updateClinic(Clinic clinic) throws DatabaseUpdateException;

    boolean deleteClinic(int clinicID) throws DatabaseDeleteException;

    Optional<Clinic> getClinicById(int clinicID) throws DatabaseQueryException;

    List<Clinic> getAllClinics() throws DatabaseQueryException;
}

