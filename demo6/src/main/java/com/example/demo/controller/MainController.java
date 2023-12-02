package com.example.demo.controller;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.service.impl.SchemaServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserServiceImpl userService;
    private final SchemaServiceImpl schemaService;

    @GetMapping("/")
    public ModelAndView index(Model model, Principal principal) {
        if (principal != null) {
            userService.getUserByName(principal.getName()).ifPresent(user -> model.addAttribute("user", user));
        }
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/role")
    public ModelAndView index() {
        return new ModelAndView("role");
    }

    @GetMapping("/join")
    public ModelAndView join() {
        return new ModelAndView("join");
    }

    @GetMapping("/sandbox")
    public ModelAndView sandbox(Model model, Principal principal) {
        Long userId = userService.getUserByName(principal.getName()).get().getId();
        List<Schema> schemas = schemaService.getSchemasByUserId(userId).get();
        Collections.reverse(schemas);

        model.addAttribute("schemas", schemas);
        model.addAttribute("user", principal);
        return new ModelAndView("sandbox");
    }

    @GetMapping("/teacher")
    public ModelAndView teacher(Model model, Principal principal) {
        model.addAttribute("user", principal);
        return new ModelAndView("teacher");
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error");
    }
}
