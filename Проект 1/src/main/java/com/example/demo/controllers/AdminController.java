package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.models.enums.Role;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public ModelAndView admin(Model user, Principal principal, Model users, Model roles) {
        List<String> roleList = new ArrayList<>();
        for (Role role : Role.values()) {
            roleList.add(role.name());
        }

        user.addAttribute("user", userService.getUserByName(principal.getName()));
        users.addAttribute("users", userService.getUsers());
        roles.addAttribute("roles", roleList);
        return new ModelAndView("admin");
    }

    @GetMapping("/user/{id}/ban")
    public ModelAndView ban(@PathVariable int id) {
        userService.banUser(id);
        return new ModelAndView("redirect:/admin");
    }
    @GetMapping("/user/{id}/unban")
    public ModelAndView unban(@PathVariable int id) {
        userService.unbanUser(id);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/user/{id}/setOperator")
    public ModelAndView setOperator(@PathVariable int id) {
        userService.setOperator(id);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/user/{id}/removeOperator")
    public ModelAndView removeOperator(@PathVariable int id) {
        userService.removeOperator(id);
        return new ModelAndView("redirect:/admin");
    }
}
