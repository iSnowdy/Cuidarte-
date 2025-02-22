package Services.DB;

import DAO.DoctorDAO;
import Exceptions.*;
import Interfaces.IDoctorService;
import Models.Doctor;

import java.util.List;
import java.util.Optional;

public class DoctorServices implements IDoctorService {
    private final DoctorDAO doctorDAO;

    public DoctorServices() throws DatabaseOpeningException {
        this.doctorDAO = new DoctorDAO();
    }

    @Override
    public boolean registerDoctor(Doctor doctor) throws DatabaseInsertException {
        return doctorDAO.save(doctor);
    }

    @Override
    public boolean updateDoctor(Doctor doctor) throws DatabaseUpdateException {
        return doctorDAO.update(doctor);
    }

    @Override
    public boolean deleteDoctor(String DNI) throws DatabaseDeleteException {
        return doctorDAO.delete(DNI);
    }

    @Override
    public Optional<Doctor> getDoctorByDNI(String DNI) throws DatabaseQueryException {
        return doctorDAO.findById(DNI);
    }

    @Override
    public List<Doctor> getAllDoctors() throws DatabaseQueryException {
        return doctorDAO.findAll();
    }

    @Override
    public List<Doctor> getDoctorsBySpecialty(String specialty) throws DatabaseQueryException {
        return doctorDAO.findBySpeciality(specialty);
    }
}
