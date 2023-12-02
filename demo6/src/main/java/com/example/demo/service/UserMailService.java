package com.example.demo.service;

import com.example.demo.model.postgres.User;

import java.util.Optional;

public interface UserMailService {
    Optional<User> forgot(String email);
    String registration(User user);
    void sendEmail(String to, String text, String subject);
}
