package com.example.demo.controllers;

import com.example.demo.models.Account;
import com.example.demo.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("")
    public ModelAndView account(Model model, Principal principal) {
        Account account = accountService.getAccountByName(principal.getName());
        model.addAttribute("account", account);
        model.addAttribute("avatarSign", account.getName().toUpperCase().charAt(0));
        return new ModelAndView("account");
    }
}
