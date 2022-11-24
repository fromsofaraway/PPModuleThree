package ru.brow.ModuleThreeApplication.service;


import ru.brow.ModuleThreeApplication.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void saveUser(User user);

    void deleteUser(long id);

    User getUserById(long id);

    void updateUser(long id, User updatedUser);

}
