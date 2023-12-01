package com.example.demo.controller;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.service.ApiService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sandbox")
public class SandboxController {
    private final UserService userService;
    private final ApiService apiService;

    @GetMapping("")
    public ModelAndView sandbox(Model model, Principal principal) {
        List<Schema> schemas = apiService
                .getSchemasByUserId(userService
                        .getUserByName(principal.getName())
                        .get()
                        .getId()
                );
        Collections.reverse(schemas);

        model.addAttribute("schemas", schemas);
        model.addAttribute("user", principal);

        return new ModelAndView("sandbox");
    }
}
