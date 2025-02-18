package Services.DB;

import Interfaces.IAuthenticationService;

import java.sql.Connection;
import java.sql.Date;

public class AuthenticationService implements IAuthenticationService {
    private final Connection connection;

    public AuthenticationService(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean login(String email, String password) {
        // SQL query to check for user's credentials
        return false;
    }

    @Override
    public boolean register(String fullName, String email, String phoneNumber, Date birthday, String password) {
        // SQL query to register a new user
        return false;
    }
}
