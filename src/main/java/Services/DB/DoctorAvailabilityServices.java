package Services.DB;

import DAO.DoctorAvailabilityDAO;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Interfaces.IDoctorAvailability;
import Models.DoctorAvailability;

import java.util.List;
import java.util.Optional;

public class DoctorAvailabilityServices implements IDoctorAvailability {
    private final DoctorAvailabilityDAO doctorAvailabilityDAO;

    public DoctorAvailabilityServices() throws DatabaseOpeningException {
        this.doctorAvailabilityDAO = new DoctorAvailabilityDAO();
    }


    @Override
    public boolean setDoctorAvailability(DoctorAvailability doctorAvailability) throws DatabaseInsertException {
        return doctorAvailabilityDAO.save(doctorAvailability);
    }

    @Override
    public Optional<DoctorAvailability> getDoctorAvailability(int availabilityID) throws DatabaseQueryException {
        return doctorAvailabilityDAO.findById(availabilityID);
    }

    @Override
    public List<DoctorAvailability> getAllDoctorAvailabilities() throws DatabaseQueryException {
        return doctorAvailabilityDAO.findAll();
    }
}
