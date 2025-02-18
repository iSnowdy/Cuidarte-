package Services.DB;

import Models.Patient;
import MySQL.DataBase.PatientImplementation;

import java.util.List;
import java.util.Optional;

public class PatientServices {
    private final PatientImplementation patientImplementation;

    public PatientServices() {
        this.patientImplementation = new PatientImplementation();
    }

    public boolean registerPatient(Patient patient) {
        return patientImplementation.save(patient);
    }

    public boolean deletePatient(Patient patient) {
        return patientImplementation.delete(patient);
    }

    public boolean modifyPatient(Patient patient) {
        return patientImplementation.update(patient);
    }

    public Optional<Patient> getPatient(String DNI) {
        return patientImplementation.findPatientByDNI(DNI);
    }

    public List<Patient> getAllPatients() {
        return patientImplementation.findAllPatients();
    }
}
