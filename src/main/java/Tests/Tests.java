package Tests;

import java.util.Properties;

public class Tests {
    public static void main(String[] args) {
        System.out.println("Email username: " + System.getenv("TEST_EMAIL_USERNAME"));
        System.out.println("Email password: " + System.getenv("TEST_EMAIL_PASSWORD"));
        System.out.println("MySQL username: " + System.getenv("MySQL_Username"));
        System.out.println("MySQL password: " + System.getenv("MySQL_Password"));

        //System.getenv().forEach((k, v) -> System.out.println("Env: " + k + "=" + v));

    }
}