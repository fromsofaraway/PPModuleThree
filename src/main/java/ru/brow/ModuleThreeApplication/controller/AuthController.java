package ru.brow.ModuleThreeApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.brow.ModuleThreeApplication.model.User;
import ru.brow.ModuleThreeApplication.security.UserDetails;
import ru.brow.ModuleThreeApplication.service.RegistrationService;
import ru.brow.ModuleThreeApplication.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.register(user);
        return "redirect:/auth/login";
    }

    @ResponseBody
    @GetMapping("/logged")
    public UserDetails getLoggedUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}
