package com.example.demo.controllers;

import com.example.demo.configurations.SecurityConfig;
import com.example.demo.models.User;
import com.example.demo.models.enums.Role;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ModelAndView profile(Model user, Principal principal) {
        user.addAttribute("user", userService.getUserByName(principal.getName()));
        return new ModelAndView("profile");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @PostMapping("/registration")
    public ModelAndView createUser(User user, Model model) {
        switch (userService.createUser(user)) {
            case -2:
                model.addAttribute("errorMessage", "Пользователь с такой почтой уже существует");
                    return new ModelAndView("registration");
            case -1:
                model.addAttribute("errorMessage", "Пользователь с таким именем пользователя уже существует");
                return new ModelAndView("registration");
            default:
                return new ModelAndView("redirect:/login");
        }
    }
}
