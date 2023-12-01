package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagerServiceImpl implements UserManagerService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public String createUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent()) {
            return "Логин уже используется";
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Почта уже используется";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        this.userRepository.save(user);

        String message = "Здравствуйте, " + user.getName() + ".\n" +
                "\n" +
                "Спасибо за регистрацию в нашем приложении! Для завершения процесса регистрации и активации вашей учётной записи, пожалуйста, нажмите на следующую ссылку:\n" +
                "\n" +
                "http://localhost:8080/signup/activate?code=" + user.getActivationCode() + "\n" +
                "\n" +
                "Если вы не регистрировались в нашем приложении, проигнорируйте это письмо.\n" +
                "\n" +
                "С уважением,\n" +
                "Уразов Илья Владимирович!";

        sendEmail(user.getEmail(), message, "Подтверждение регистрации");
        return "";
    }

    @Override
    public Optional<User> activateUser(String code) {
        return userRepository.findByActivationCode(code).map(user -> {
            user.setActivationCode(null);
            user.setActive(true);
            return userRepository.save(user);
        });
    }

    @Override
    public boolean resetPassword(String code, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetPasswordCode(code);
        if (!optionalUser.isPresent()) return false;

        User user = optionalUser.get();
        user.setResetPasswordCode(null);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<User> forgotPassword(String email) {
        return userRepository.findByEmail(email).map(user -> {
            user.setResetPasswordCode(UUID.randomUUID().toString());
            String message = "Здравствуйте, " + user.getName() + ".\n" +
                    "\n" +
                    "Вы получили это письмо, потому что запросили восстановление пароля для вашей учётной записи в нашем приложении. Чтобы сбросить пароль и получить доступ к вашей учётной записи, пожалуйста, нажмите на следующую ссылку:\n" +
                    "\n" +
                    "http://localhost:8080/password/reset?code=" + user.getResetPasswordCode() + "\n" +
                    "\n" +
                    "Если вы не запрашивали сброс пароля, проигнорируйте это письмо.\n" +
                    "\n" +
                    "С уважением,\n" +
                    "Уразов Илья Владимирович!";

            sendEmail(user.getEmail(), message, "Восстановление пароля");
            return userRepository.save(user);
        });
    }

    @Override
    public void sendEmail(String to, String text, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("urazov.buj@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }
}
