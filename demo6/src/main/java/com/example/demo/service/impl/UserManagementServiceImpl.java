package com.example.demo.service.impl;

import com.example.demo.model.postgres.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagerService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> activate(String code) {
        return userRepository.findByActivationCode(code).map(user -> {
            user.setActivationCode(null);
            user.setActive(true);
            return userRepository.save(user);
        });
    }

    @Override
    public Optional<User> reset(String code, String newPassword) {
        return userRepository.findByResetPasswordCode(code).map(user -> {
            user.setResetPasswordCode(null);
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        });
    }
}
