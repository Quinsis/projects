package com.example.demo.service.impl;

import com.example.demo.model.postgres.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserByResetPasswordCode(String code) {
        return userRepository.findByResetPasswordCode(code);
    }
    @Override
    public Optional<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByActivationCode(String activationCode) {
        return userRepository.findByActivationCode(activationCode);
    }
}
