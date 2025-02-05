package Tests;

import Models.Doctor;
import Models.Patient;
import Services.DB.DoctorServices;
import Services.DB.PatientServices;
import java.time.LocalDate;
import java.sql.Date;

/**
 * Hello world!
 */

public class App {
    public static void main(String[] args) {
        PatientServices patientServices = new PatientServices();
        DoctorServices doctorServices = new DoctorServices();

        System.out.println("Creating new patients");
        System.out.println("Inserting...");
        Patient p1 = new Patient("12345678A", "John", "Doe", "123456789", "john.doe@example.com",
                Date.valueOf(LocalDate.of(1990, 6, 15)), 33,"password123",  5);
        Patient p2 = new Patient("87654321B", "Jane", "Smith", "987654321", "jane.smith@example.com",
                Date.valueOf(LocalDate.of(1985, 11, 20)), 38,"securePass!",  6);
        Patient p3 = new Patient("23456789C", "Carlos", "García", "654321987", "carlos.garcia@example.com",
                Date.valueOf(LocalDate.of(1995, 3, 8)), 29,"qwerty2023",  9);
        Patient p4 = new Patient("98765432D", "Emily", "Johnson", "321654987", "emily.johnson@example.com",
                Date.valueOf(LocalDate.of(1988, 8, 3)), 35,"helloWorld",  10);
        Patient p5 = new Patient("34567890E", "Liam", "Brown", "789123456", "liam.brown@example.com",
                Date.valueOf(LocalDate.of(1992, 12, 25)), 31,"password456",  23);

        patientServices.registerPatient(p1);
        patientServices.registerPatient(p2);
        patientServices.registerPatient(p3);

        System.out.println("-------------------------------");
        System.out.println("Creating new doctors");

        Doctor d1 = new Doctor("67890123F", "Alice", "Williams", "111222333", "alice.williams@example.com", "Cardiology");
        Doctor d2 = new Doctor("45678901G", "Bob", "Miller", "444555666", "bob.miller@example.com", "Dermatology");
        Doctor d3 = new Doctor("56789012H", "Charlie", "Davis", "777888999", "charlie.davis@example.com", "Neurology");
        Doctor d4 = new Doctor("78901234J", "Diana", "Martinez", "000111222", "diana.martinez@example.com", "Pediatrics");
        Doctor d5 = new Doctor("89012345K", "Edward", "Taylor", "333444555", "edward.taylor@example.com", "Orthopedics");

        doctorServices.registerDoctor(d1);
        doctorServices.registerDoctor(d2);
        doctorServices.registerDoctor(d3);

        System.out.println("Updating...");
        p1.setSurname("Modified by Me");
        patientServices.modifyPatient(p1);

        System.out.println("DNI to delete: " + p2.getDNI());
        System.out.println(p2);
        patientServices.deletePatient(p2);
    }
}
