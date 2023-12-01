package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.mongodb.OfflineLink;
import com.example.demo.model.mongodb.OnlineLink;
import com.example.demo.model.mongodb.Schema;
import com.example.demo.service.ApiService;
import com.example.demo.service.impl.UserManagerServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserServiceImpl userService;
    private final UserManagerServiceImpl accountManagerService;
    private final ApiService apiService;

    @GetMapping("/error")
    public ModelAndView error() {
        return new ModelAndView("error");
    }

    @GetMapping("/")
    public ModelAndView index(Model model, Principal principal) {
        if (principal != null) {
            userService.getUserByName(principal.getName()).ifPresent(user -> model.addAttribute("user", user));
        }
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
    public ModelAndView addAccount(User user, Model model, RedirectAttributes redirectAttributes) {
        String response = accountManagerService.createUser(user);

        if (response.equals("")) {
            redirectAttributes.addFlashAttribute("activateMessage",
                    "На вашу почту отправлено письмо с подтверждением.");
            redirectAttributes.addFlashAttribute("activateMessageType", "alert");
            return new ModelAndView("redirect:/login");
        } else {
            model.addAttribute("errorMessage", response);
            model.addAttribute("failed", true);
            model.addAttribute("account", user);
            return new ModelAndView("signup");
        }
    }

    @GetMapping("/signup/activate")
    public ModelAndView activate(
            @Param(value = "code") String code,
            RedirectAttributes redirectAttributes
    ) {
        if (accountManagerService.activateUser(code).isPresent()) {
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
    public ModelAndView passwordForgot(
            @RequestParam("email") String email,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (accountManagerService.forgotPassword(email).isPresent()) {
            redirectAttributes.addFlashAttribute("activateMessage",
                    "На вашу почту отправлено письмо с инструкцией по восстановлению пароля.");
            redirectAttributes.addFlashAttribute("activateMessageType", "alert");
            return new ModelAndView("redirect:/password/forgot");
        } else {
            model.addAttribute("errorMessage", "Аккаунт с такой почтой не существует.");
            model.addAttribute("failed", true);
            model.addAttribute("email", email);
            return new ModelAndView("forgotPassword");
        }
    }

    @GetMapping("/password/reset")
    public ModelAndView passwordReset(
            @Param(value = "code") String code,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Optional<User> user = userService.getUserByResetPasswordCode(code);
        if (!user.isPresent()) {
            redirectAttributes.addFlashAttribute("activateMessage", "Неверный код восстановления.");
            redirectAttributes.addFlashAttribute("activateMessageType", "deny");
            return new ModelAndView("redirect:/password/forgot");
        }
        model.addAttribute("code", code);
        return new ModelAndView("resetPassword");
    }

    @PostMapping("/password/reset")
    public ModelAndView passwordReset(
            User user, @Param(value = "code") String code,
            RedirectAttributes redirectAttributes
    ) {
        if (accountManagerService.resetPassword(code, user.getPassword())) {
            redirectAttributes.addFlashAttribute("activateMessage", "Пароль успешно изменён.");
            redirectAttributes.addFlashAttribute("activateMessageType", "success");
        } else {
            redirectAttributes.addFlashAttribute("activateMessage", "Неверный код восстановления.");
            redirectAttributes.addFlashAttribute("activateMessageType", "deny");
        }
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/online/{code}")
    public ModelAndView online(@PathVariable("code") String code, Principal principal) {
        try {
            OnlineLink onlineLink = apiService.getOnlineLinkByCode(code);
            String schemaId = onlineLink.getSchemaId();

            Schema schema = apiService.getSchemaById(schemaId);

            ApiController apiController = new ApiController(userService, apiService);
            return apiController.importSchema(schema, principal);
        } catch (Exception e) {
            return new ModelAndView("error");
        }
    }

    @GetMapping("/offline/{code}")
    public ModelAndView offline(@PathVariable("code") String code, Principal principal) {
        try {
            OfflineLink offlineLink = apiService.getOfflineLinkByCode(code);
            Schema schema = offlineLink.getSchema();
            ApiController apiController = new ApiController(userService, apiService);
            return apiController.importSchema(schema, principal);
        } catch (Exception e) {
            return new ModelAndView("error");
        }
    }
}
