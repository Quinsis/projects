package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByResetPasswordCode(String code);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByName(String name);
}
