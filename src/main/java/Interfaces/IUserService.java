package Interfaces;

import Models.User;

public interface IUserService {
    boolean updateUser(User user);
    boolean changePassword(String email, String oldPassword, String newPassword);
    boolean deleteUser(String DNI, String email);
}
