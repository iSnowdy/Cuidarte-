package Database.Models;

import java.sql.Timestamp;

public class Message {
    private int id;
    private String patientDNI, doctorDNI;
    private Timestamp date;
    private String messageContent;

    public Message(int id, String patientDNI, String doctorDNI, Timestamp date, String messageContent) {
        this.id = id;
        this.patientDNI = patientDNI;
        this.doctorDNI = doctorDNI;
        this.date = date;
        this.messageContent = messageContent;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientDNI() {
        return patientDNI;
    }

    public void setPatientDNI(String patientDNI) {
        this.patientDNI = patientDNI;
    }

    public String getDoctorDNI() {
        return doctorDNI;
    }

    public void setDoctorDNI(String doctorDNI) {
        this.doctorDNI = doctorDNI;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
