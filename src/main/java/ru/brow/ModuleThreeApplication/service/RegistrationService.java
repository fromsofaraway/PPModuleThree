package ru.brow.ModuleThreeApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brow.ModuleThreeApplication.model.Role;
import ru.brow.ModuleThreeApplication.model.User;
import ru.brow.ModuleThreeApplication.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void register(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "ROLE_USER"));
        user.setRoles(roles);
        userRepository.save(user);
    }
}
