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
    public String show(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUser", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("newUser", new User());
        return "/admin/admin";
    }


//    @GetMapping("/addNewUser")
//    public String addNewUser(@ModelAttribute("newUser") User user, Model model) {
//        model.addAttribute("allRoles", roleService.findAll());
//        return "/admin/admin";
//    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @ModelAttribute("role") Role role,
                           Model model) {
        model.addAttribute("user", new User());
        userService.save(user);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}")
    public String editUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.findOne(id));
        return "redirect:/admin/";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @ModelAttribute("role") Role role,
                             @PathVariable("id") long id) {
        userService.update(id, user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }
}
