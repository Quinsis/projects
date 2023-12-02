package com.example.demo.service;

import com.example.demo.model.postgres.User;

import java.util.Optional;

public interface UserManagerService {
    Optional<User> activate(String code);
    Optional<User> reset(String code, String newPassword);
}
