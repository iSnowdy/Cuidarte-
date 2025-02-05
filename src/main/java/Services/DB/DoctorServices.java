package Services.DB;

import Models.Doctor;
import MySQL.DataBase.DoctorImplementation;

import java.util.List;
import java.util.Optional;

public class DoctorServices {
    private final DoctorImplementation doctorImplementation;

    public DoctorServices() {
        this.doctorImplementation = new DoctorImplementation();
    }

    public boolean registerDoctor(Doctor doctor) {
        return doctorImplementation.save(doctor);
    }

    public boolean deleteDoctor(Doctor doctor) {
        return doctorImplementation.delete(doctor);
    }

    public boolean modifyDoctor(Doctor doctor) {
        return doctorImplementation.update(doctor);
    }

    public Optional<Doctor> getDoctor(int id) {
        return doctorImplementation.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorImplementation.findAll();
    }
}
