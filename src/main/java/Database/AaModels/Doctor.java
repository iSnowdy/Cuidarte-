package Database.AaModels;

public class Doctor extends User {
    private String speciality;
    private String description;

    public Doctor(String DNI, String firstName, String surname, String phoneNumber, String email,
                  String speciality, String description) {
        super(DNI, firstName, surname, phoneNumber, email);
        this.speciality = speciality;
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
                        "Doctor Speciality: " + speciality;
    }

    // Getters and Setters
    public String getSpeciality() {
        return speciality;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
