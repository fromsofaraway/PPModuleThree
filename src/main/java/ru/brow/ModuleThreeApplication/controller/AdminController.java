package ru.brow.ModuleThreeApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.brow.ModuleThreeApplication.model.Role;
import ru.brow.ModuleThreeApplication.model.User;
import ru.brow.ModuleThreeApplication.security.UserDetails;
import ru.brow.ModuleThreeApplication.service.RoleService;
import ru.brow.ModuleThreeApplication.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("")
    public String showAll(Model model) {
        model.addAttribute("users", userService.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails.getUser());
        return "users";
    }


    @GetMapping("/addNewUser")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", roleService.findAll());
        return "user-info";
    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @ModelAttribute("role") Role role) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @ModelAttribute("role") Role role,
                             @PathVariable("id") long id) {
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
