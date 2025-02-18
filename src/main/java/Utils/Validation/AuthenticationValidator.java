package Utils.Validation;

// DNI
// Nombre/Apellido 100
// N_TLF
// EMAIL
// Fecha Nacimiento Date.sql ()
// Contraseña Hash?
// Salt?
// Especialidad 50
// Direccion 100
// Temperatura, Peso, Altura DECIMAL(4,2) ---> Decimales ###,###
// Fecha Visita DATETIME
// Antecedentes, Motivo Consulta, Exploracion Fisica, Tratamiento 2000
// Alergias, Diagnosis 500
//


import Models.Patient;
import Services.DB.DoctorServices;
import Services.DB.PatientServices;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * Static utility class to validate any inputs taken from the user before inserting them to the DB
 */

public class AuthenticationValidator {


    private static final int NAME_SURNAME_ADDRESS_LENGTH = 100;
    private static final int SPECIALITY_LENGTH = 50;
    private static final int ALLERGIES_DIAGNOSIS_LENGTH = 500;
    private static final int RECORD_MOTIF_PHYSICAL_EXPLORATION_TREATMENT_LENGTH = 2000;

    private static boolean isValidTextLength(String text, int lengthToValidate) {
        return text != null && text.length() >= lengthToValidate;
    }

    // Returns an Optional of Date if the String format is correct and parsed (yyyy-MM-dd)
    public static Optional<Date> validateAndParseDate(String dobToValidate) {
        if (dobToValidate == null || dobToValidate.isEmpty()) {
            return Optional.empty();
        }

        if (dobToValidate.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
            return Optional.of(java.sql.Date.valueOf(dobToValidate));
        } else {
            return Optional.empty();
        }
    }

    // Login and register fields validation

    public static boolean isValidUserRegisterFormat(String dni, String name, String surname, String phoneNumber, String email) {
        return  isValidDNIFormat(dni) &&
                isValidTextLength(name, NAME_SURNAME_ADDRESS_LENGTH) &&
                isValidTextLength(surname, NAME_SURNAME_ADDRESS_LENGTH) &&
                isValidPhoneNumber(phoneNumber) &&
                isValidEmailAddress(email);
    }

    // Format for Spanish DNI/NIE
    private static boolean isValidDNIFormat(String DNIToValidate) {
        return DNIToValidate.matches("^[XYZ]?([0-9]{7,8})([A-Z])");
    }

    // Format for spanish phone number (starting with +34 or 6, 7, 8 or 9) and total of 9 digits long
    private static boolean isValidPhoneNumber(String phoneNumberToValidate) {
        return phoneNumberToValidate.matches("^(?:\\+34\\s?)?[6789]\\d{8}$");
    }

    // String for user address with a @ mandatory after it. Then domain and finally the extension
    private static boolean isValidEmailAddress(String emailAddressToValidate) {
        return emailAddressToValidate.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n");
    }


    // TODO: Should these methods be here?
    public static boolean verifyPatientCode(Patient patient, int code) {
        return patient.getVerificationCode() == code;
    }

    public static boolean checkForDuplicatePatient(Patient patient, List<Patient> allPatientsFromDB) {
        return allPatientsFromDB.stream().anyMatch(p -> p.getDNI().equals(patient.getDNI()));
    }
}
