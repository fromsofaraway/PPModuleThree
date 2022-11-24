package ru.brow.ModuleThreeApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brow.ModuleThreeApplication.dao.UserDao;
import ru.brow.ModuleThreeApplication.model.User;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    @Override
    public void saveUser(User user) {
        userDao.saveUser(user);
    }
    @Override
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }
    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public void updateUser(long id, User updatedUser) {
        userDao.updateUser(id, updatedUser);
    }
}
