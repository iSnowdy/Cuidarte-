package Models;

public class Doctor extends User {
    private String specialty;

    public Doctor(int id, String firstName, String surname, String phoneNumber, String email,
                  String specialty) {
        super(id, firstName, surname, phoneNumber, email);
        this.specialty = specialty;
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    @Override
    public String toString() {
        return
                super.toString() + "\n" +
                "Doctor Specialty: " + specialty;
    }

    // Getters and Setters
    public String getSpecialty() {
        return specialty;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
