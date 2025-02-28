package Database.Models;

import java.sql.Date;
import java.util.Optional;

public class Patient extends User {
    private Date dateOfBirth;
    private String password; // It is hashed upon registering the patient to the DB
    private int age, verificationCode;
    private final int salt;

    public Patient(String DNI, String firstName, String surname, String phoneNumber, String email,
                   Optional<Date> dateOfBirth, int age, String password) {
        super(DNI, firstName, surname, phoneNumber, email);
        this.dateOfBirth = dateOfBirth.get();
        this.age = age;
        this.password = password;
        // TODO: Do not allow user registration if the code does not match the one generated here
        this.salt = generateRandomNumber();
        this.verificationCode = generateRandomNumber();
    }

    // Constructor to be able to build a Patient object when retrieving it from the DB
    // with an assigned verification code
    public Patient(String DNI, String firstName, String surname, String phoneNumber, String email,
                   Date dateOfBirth, int age, String password, int salt, int verificationCode) {
        super(DNI, firstName, surname, phoneNumber, email);
        this.dateOfBirth = Date.valueOf(dateOfBirth.toLocalDate());
        this.age = age;
        this.password = password;
        this.salt = salt;
        this.verificationCode = verificationCode;
    }

    // 6 digit verification code to be sent to the user's email upon registration and for the salting
    private int generateRandomNumber() {
        return (int) (Math.random() * 900000) + 100000;
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

    public int getVerificationCode() {
        return verificationCode;
    }
    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }
}
