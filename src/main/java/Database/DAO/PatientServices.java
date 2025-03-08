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
    public Optional<Patient> getPatientByDNI(String DNI) throws DatabaseQueryException {
        return patientDAO.findById(DNI);
    }

    public Optional<Patient> getPatientByEmail(String email) throws DatabaseQueryException {
        return patientDAO.findByEmail(email);
    }
}
