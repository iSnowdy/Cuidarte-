package Services.DB;

import DAO.ClinicDAO;
import Exceptions.*;
import Interfaces.IClinicService;
import Models.Clinic;

import java.util.List;
import java.util.Optional;

public class ClinicServices implements IClinicService {
    private final ClinicDAO clinicDAO;

    public ClinicServices() throws DatabaseOpeningException {
        clinicDAO = new ClinicDAO();
    }


    @Override
    public boolean registerClinic(Clinic clinic) throws DatabaseInsertException {
        return clinicDAO.save(clinic);
    }

    @Override
    public boolean updateClinic(Clinic clinic) throws DatabaseUpdateException {
        return clinicDAO.update(clinic);
    }

    @Override
    public boolean deleteClinic(int clinicID) throws DatabaseDeleteException {
        return clinicDAO.delete(clinicID);
    }

    @Override
    public Optional<Clinic> getClinicById(int clinicID) throws DatabaseQueryException {
        return clinicDAO.findById(clinicID);
    }

    @Override
    public List<Clinic> getAllClinics() throws DatabaseQueryException {
        return clinicDAO.findAll();
    }
}
