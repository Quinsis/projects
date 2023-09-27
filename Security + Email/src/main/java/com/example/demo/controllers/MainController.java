package com.example.demo.controllers;

import com.example.demo.models.Account;
import com.example.demo.services.AccountService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final AccountService accountService;

    @GetMapping("/")
    public ModelAndView index(Model model, Principal principal) {
        boolean isAuthenticated = principal != null;
        model.addAttribute("isAuthenticated", isAuthenticated);
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    public ModelAndView login(Model model) {
        model.addAttribute("error", "");
        return new ModelAndView("login");
    }

    @GetMapping("/signup")
    public ModelAndView signup(Model model) {
        model.addAttribute("errorMessage", "");
        model.addAttribute("failed", false);
        return new ModelAndView("signup");
    }

    @PostMapping("/signup")
    public ModelAndView addAccount(Account account, Model model, RedirectAttributes redirectAttributes) {
        account.setPhone("+7" + account.getPhone());
        String response = this.accountService.createAccount(account);
        if (!Objects.equals(response, "")) {
            model.addAttribute("errorMessage", response);
            model.addAttribute("failed", true);
            model.addAttribute("account", account);
            account.setPhone(account.getPhone().replace("+7", ""));
            return new ModelAndView("signup");
        } else {
            redirectAttributes.addFlashAttribute("activateMessage", "На вашу почту отправлено письмо с подтверждением.");
            redirectAttributes.addFlashAttribute("activateMessageType", "alert");
            return new ModelAndView("redirect:/login");
        }
    }

    @GetMapping("/signup/activate")
    public ModelAndView activate(@Param(value = "code") String code, RedirectAttributes redirectAttributes) {
        boolean isActivated = accountService.activateAccount(code);
        if (isActivated) {
            redirectAttributes.addFlashAttribute("activateMessage", "Учётная запись успешно активирована.");
            redirectAttributes.addFlashAttribute("activateMessageType", "success");
        } else {
            redirectAttributes.addFlashAttribute("activateMessage", "Неверный код подтверждения.");
            redirectAttributes.addFlashAttribute("activateMessageType", "deny");
        }

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/password/forgot")
    public ModelAndView passwordForgot() {
        return new ModelAndView("forgotPassword");
    }

    @PostMapping("/password/forgot")
    public ModelAndView passwordForgot(@RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes) {
        Account account = this.accountService.getAccountByEmail(email);
        String response = this.accountService.forgotPassword(account);
        if (response != "") {
            model.addAttribute("errorMessage", response);
            model.addAttribute("failed", true);
            model.addAttribute("email", email);
            return new ModelAndView("forgotPassword");
        } else {
            redirectAttributes.addFlashAttribute("activateMessage", "На вашу почту отправлено письмо с инструкцией по восстановлению пароля.");
            redirectAttributes.addFlashAttribute("activateMessageType", "alert");
            return new ModelAndView("redirect:/password/forgot");
        }
    }

    @GetMapping("/password/reset")
    public ModelAndView passwordReset(@Param(value = "code") String code, Model model, RedirectAttributes redirectAttributes) {
        Account account = accountService.getAccountByResetPasswordCode(code);
        if (account == null) {
            redirectAttributes.addFlashAttribute("activateMessage", "Неверный код восстановления.");
            redirectAttributes.addFlashAttribute("activateMessageType", "deny");
            return new ModelAndView("redirect:/password/forgot");
        }
        model.addAttribute("code", code);
        return new ModelAndView("resetPassword");
    }

    @PostMapping("/password/reset")
    public ModelAndView passwordReset(Account account, @Param(value = "code") String code, RedirectAttributes redirectAttributes) {
        boolean isUpdated = accountService.resetPassword(code, account.getPassword());
        if (isUpdated) {
            redirectAttributes.addFlashAttribute("activateMessage", "Пароль успешно изменён.");
            redirectAttributes.addFlashAttribute("activateMessageType", "success");
        } else {
            redirectAttributes.addFlashAttribute("activateMessage", "Неверный код восстановления.");
            redirectAttributes.addFlashAttribute("activateMessageType", "deny");
        }
        return new ModelAndView("redirect:/login");
    }
}
