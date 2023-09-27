package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.enums.Role;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public int createUser(User user) {
        if (userRepository.findByName(user.getName()) != null) return -1;
        if (userRepository.findByEmail(user.getEmail()) != null) return -2;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new info with name {}", user.getName());
        userRepository.save(user);
        return 0;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public void banUser(int id) {
        for (User user : userRepository.findAll()) {
            if (user.getId() == id) {
                user.setActive(false);
                userRepository.save(user);
                return;
            }
        }
    }

    public void unbanUser(int id) {
        for (User user : userRepository.findAll()) {
            if (user.getId() == id) {
                user.setActive(true);
                userRepository.save(user);
                return;
            }
        }
    }

    public void setOperator(int id) {
        for (User user : userRepository.findAll()) {
            if (user.getId() == id) {
                user.getRoles().add(Role.ROLE_OPERATOR);
                userRepository.save(user);
                return;
            }
        }
    }

    public void removeOperator(int id) {
        for (User user : userRepository.findAll()) {
            if (user.getId() == id) {
                user.getRoles().remove(Role.ROLE_OPERATOR);
                userRepository.save(user);
                return;
            }
        }
    }
}
