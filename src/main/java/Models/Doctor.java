package Models;

public class Doctor extends User {
    private String specialty;
    private String description;

    public Doctor(String DNI, String firstName, String surname, String phoneNumber, String email,
                  String specialty, String description) {
        super(DNI, firstName, surname, phoneNumber, email);
        this.specialty = specialty;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
