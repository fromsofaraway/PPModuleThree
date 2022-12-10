package ru.brow.ModuleThreeApplication.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.brow.ModuleThreeApplication.security.UserDetails;


@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping()
    public String index() {
        return "/user/user";
    }

    @ResponseBody
    @GetMapping("/current")
    public UserDetails getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}
