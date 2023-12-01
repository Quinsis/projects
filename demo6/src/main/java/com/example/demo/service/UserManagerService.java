package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Optional;

public interface UserManagerService {
    String createUser(User user);
    boolean resetPassword(String code, String newPassword);
    Optional<User> forgotPassword(String email);
    Optional<User> activateUser(String code);
    void sendEmail(String to, String text, String subject);
}
