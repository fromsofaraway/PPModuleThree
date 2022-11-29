package ru.brow.ModuleThreeApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.brow.ModuleThreeApplication.security.UserDetails;
import ru.brow.ModuleThreeApplication.service.RoleService;
import ru.brow.ModuleThreeApplication.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("currentUser", userService.findOne(userDetails.getUser().getId()));
        return "/user/user";
    }
}
