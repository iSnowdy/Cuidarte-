package PortalPage.TempModels;

// Simple class to represent a patient
public class Patient {
    private int id;
    private String name;
    private int age;
    private String address;
    private String phone;

    // Constructor with sample parameters
    public Patient(int id, String name, int age, String address, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    // Getters for the patient attributes
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
