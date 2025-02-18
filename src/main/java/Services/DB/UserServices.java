package Services.DB;

import Interfaces.IUserService;
import Models.User;

public class UserServices implements IUserService {

    @Override
    public boolean updateUser(User user) {
        // Updates user in the DB with the new object
        // Need to check if it exists first tho
        return false;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        // Updates password
        // Hashing?
        return false;
    }

    @Override
    public boolean deleteUser(String DNI, String email) {
        // Given DNI and email, deletes the User
        return false;
    }
}
