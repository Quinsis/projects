package com.example.demo.service;

import com.example.demo.model.postgres.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByResetPasswordCode(String code);
    Optional<User> getUserByName(String name);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByActivationCode(String activationCode);
}
