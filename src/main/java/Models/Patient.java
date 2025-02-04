package Models;

public class Patient extends User {
    private String dateOfBirth, password;
    private int age;
    private final int salt; // Maybe I will not use this

    public Patient(int id, String firstName, String surname, String phoneNumber, String email,
                         String dateofBirth, String password, int age, int salt) {
        super(id, firstName, surname, phoneNumber, email);
        this.dateOfBirth = dateofBirth;
        this.password = password;
        this.age = age;
        this.salt = salt;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String toString() {
        return
                super.toString() + "\n" +
                "Date of Birth: " + dateOfBirth + "\n" +
                "Age: " + age;
    }

    // Getters and Setters

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public int getSalt() {
        return salt;
    }
}
