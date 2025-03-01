package Database.DAO;

import Exceptions.*;
import Interfaces.IPatientService;
import Database.Models.Patient;
import Utils.Utility.PasswordHasher;
import Utils.Validation.MyValidator;

import java.util.List;
import java.util.Optional;

public class PatientServices implements IPatientService {
    private final PatientDAO patientDAO;

    public PatientServices() throws DatabaseOpeningException {
        this.patientDAO = new PatientDAO();
    }

    public boolean verifyIfPatientExists(Patient patient) {
        boolean patientByDNI = getPatientByDNI(patient.getDNI()).isPresent();
        boolean patientByEmail = getPatientByEmail(patient.getEmail()).isPresent();
        return patientByDNI && patientByEmail;
    }

    @Override
    public boolean registerPatient(Patient patient) throws DatabaseInsertException {
        if (!MyValidator.isValidPassword(patient.getPassword())) {
            throw new DatabaseInsertException("Password must be of at least 6 characters long " +
                    "and contain at least one number");
        }

        if (patientDAO.findById(patient.getDNI()).isPresent()) {
            throw new DatabaseInsertException("Patient with DNI " + patient.getDNI() + " already exists");
        }

        String hashedPassword = PasswordHasher.hashPassword(patient.getPassword(), patient.getSalt());
        // Overwrites the previous password and sets the salt properly
        patient.setPassword(hashedPassword);

        return patientDAO.save(patient);
    }

    @Override
    public boolean updatePatient(Patient patient) throws DatabaseUpdateException {
        return patientDAO.update(patient);
    }

    public boolean resetPassword(String email, String oldPassword, String newPassword) throws DatabaseUpdateException {
        Optional<Patient> patientOptional = patientDAO.findByEmail(email);
        if (patientOptional.isEmpty()) throw new DatabaseUpdateException("Patient not found");

        Patient patient = patientOptional.get();
        if (!PasswordHasher.verifyPassword(oldPassword, patient.getSalt(), patient.getPassword())) {
            throw new DatabaseUpdateException("Password does not match");
        }

        patient.setPassword(PasswordHasher.hashPassword(newPassword, patient.getSalt()));
        return patientDAO.update(patient);
    }

    @Override
    public boolean changePassword(String DNI, String oldPassword, String newPassword) throws DatabaseUpdateException {
        Optional<Patient> patientOptional = patientDAO.findById(DNI);
        if (patientOptional.isEmpty()) throw new DatabaseUpdateException("Patient not found");

        Patient patient = patientOptional.get();
        if (!PasswordHasher.verifyPassword(oldPassword, patient.getSalt(), patient.getPassword())) {
            throw new DatabaseUpdateException("Password does not match");
        }

        patient.setPassword(PasswordHasher.hashPassword(newPassword, patient.getSalt()));
        return patientDAO.update(patient);
    }

    @Override
    public boolean deletePatient(String DNI) throws DatabaseDeleteException {
        return patientDAO.delete(DNI);
    }

    @Override
    public Optional<Patient> getPatientByDNI(String DNI) throws DatabaseQueryException {
        return patientDAO.findById(DNI);
    }

    public Optional<Patient> getPatientByEmail(String email) throws DatabaseQueryException {
        return patientDAO.findByEmail(email);
    }

    @Override
    public List<Patient> getAllPatients() throws DatabaseQueryException {
        return patientDAO.findAll();
    }
}
