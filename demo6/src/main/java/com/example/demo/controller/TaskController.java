package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TaskController {
    @GetMapping("/role")
    public ModelAndView index() {
        return new ModelAndView("role");
    }

    @GetMapping("/join")
    public ModelAndView join() {
        return new ModelAndView("join");
    }

    @GetMapping("/teacher")
    public ModelAndView teacher(Model model, Principal principal) {
        model.addAttribute("user", principal);
        return new ModelAndView("teacher");
    }
}
