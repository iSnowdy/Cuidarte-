package Database.Models;

import java.util.Objects;

public abstract class User {
    private final String DNI;
    private String
            firstName, surname, phoneNumber, email;

    public User(String DNI, String firstName, String surname, String phoneNumber, String email) {
        this.DNI = DNI;
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public abstract String getRole();

    // Implementing it this way we can compare objects instead of their memory address
    @Override
    public boolean equals(Object object) {
        if (this == object) return true; // If both objects are the same in memory, they are equal

        if (object == null || getClass() != object.getClass()) return false;

        User user = (User) object;
        return this.DNI == user.DNI; // Compares based on unique ID
    }

    // Not sure if it will work
    @Override
    public int hashCode() {
        return Objects.hash(DNI);
    }

    @Override
    public String toString() {
        return "---------------------------------" + "\n" +
                "Información del Usuario:\n" +
                "---------------------------------" + "\n" +
                "ID: " + DNI + "\n" +
                "Nombre: " + firstName + "\n" +
                "Apellidos: " + surname + "\n" +
                "Telefono: " + phoneNumber + "\n" +
                "Email: " + email;
    }

    // Getters and Setters
    public String getDNI() {
        return DNI;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
