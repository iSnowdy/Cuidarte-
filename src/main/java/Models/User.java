package Models;

import java.util.Objects;

public abstract class User {
    private int id;
    private String
            firstName, surname, phoneNumber, email;

    public User(int id, String firstName, String surname, String phoneNumber, String email) {
        this.id = id;
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
        return this.id == user.id; // Compares based on unique ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "---------------------------------" + "\n" +
                "Información del Usuario:\n" +
                "---------------------------------" + "\n" +
                "ID: " + id + "\n" +
                "Nombre: " + firstName + "\n" +
                "Apellidos: " + surname + "\n" +
                "Telefono: " + phoneNumber + "\n" +
                "Email: " + email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
