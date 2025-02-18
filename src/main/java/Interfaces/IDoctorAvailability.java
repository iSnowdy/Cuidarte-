package Interfaces;

import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Models.DoctorAvailability;

import java.util.List;
import java.util.Optional;

public interface IDoctorAvailability {
    boolean setDoctorAvailability(DoctorAvailability doctorAvailability) throws DatabaseInsertException;

    Optional<DoctorAvailability> getDoctorAvailability(int availabilityID) throws DatabaseQueryException;

    List<DoctorAvailability> getAllDoctorAvailabilities() throws DatabaseQueryException;
}
