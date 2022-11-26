package ru.brow.ModuleThreeApplication.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.brow.ModuleThreeApplication.model.User;
import ru.brow.ModuleThreeApplication.service.UserService;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> existingOrNotUser = userService.findByUsername(user.getUsername());

        if (existingOrNotUser.isPresent()) {
            errors.rejectValue("username", "", "Пользователь с таким ником уже существует");
        }

    }
}
