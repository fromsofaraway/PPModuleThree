package ru.brow.PP_3_1_2.dao;


import ru.brow.PP_3_1_2.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void saveUser(User user);

    void deleteUser(long id);

    User getUserById(long id);

    void updateUser(long id, User updatedUser);

}
