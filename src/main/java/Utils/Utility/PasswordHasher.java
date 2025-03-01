package Utils.Utility;

/*
Generates SHA-256 has for the given password with salting
 */

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class PasswordHasher {
    public static String hashPassword(String password, int salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Combines the password + salt (salt to String and appends)
            String saltedPassword = password + salt;
            byte[] hashedBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

            // Hex to String
            return HexFormat.of().formatHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    public static boolean verifyPassword(String enteredPassword, int salt, String storedHash) {
        String enteredHash = hashPassword(enteredPassword, salt);

        System.out.println("Stored Hash (DB): " + storedHash);
        System.out.println("Entered Hash: " + enteredHash);
        System.out.println("Salt: " + salt);
        System.out.println("Comparison Result: " + enteredHash.equals(storedHash));

        return enteredHash.equals(storedHash);
    }
}
