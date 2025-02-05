package Models;

import java.util.Date;

public class Patient extends User {
    private Date dateOfBirth;
    private String password;
    private int age;
    private final int salt; // Maybe I will not use this

    public Patient(String DNI, String firstName, String surname, String phoneNumber, String email,
                   Date dateOfBirth, int age, String password, int salt) {
        super(DNI, firstName, surname, phoneNumber, email);
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.password = password;
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
        return dateOfBirth.toString();
    }

    public void setDateOfBirth(Date dateOfBirth) {
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
