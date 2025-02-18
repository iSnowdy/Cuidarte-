package Interfaces;

import java.sql.Date;

public interface IAuthenticationService {
    boolean login(String email, String password);

    boolean register(String fullName, String email, String phoneNumber, Date birthday, String password);
}
