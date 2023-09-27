package com.example.demo.services;

import com.example.demo.models.Account;
import com.example.demo.models.Role;
import com.example.demo.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    public String createAccount(Account account) {
        if (this.accountRepository.findByName(account.getName()) != null) {
            return "Логин уже используется";
        } else if (this.accountRepository.findByEmail(account.getEmail()) != null) {
            return "Почта уже используется";
        } else if (this.accountRepository.findByPhone(account.getPhone()) != null) {
            return "Телефон уже используется";
        }

        Pattern pattern = Pattern.compile("\\+7\\d{10}$");
        Matcher matcher = pattern.matcher(account.getPhone());
        if (!matcher.find()) {
            return "Неверный формат телефона";
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setActivationCode(UUID.randomUUID().toString());
        account.getRoles().add(Role.Default);
        this.accountRepository.save(account);

        String message = "Здравствуйте, " + account.getName() + ".\n" +
                "\n" +
                "Спасибо за регистрацию в нашем приложении! Для завершения процесса регистрации и активации вашей учётной записи, пожалуйста, нажмите на следующую ссылку:\n" +
                "\n" +
                "http://localhost:8080/signup/activate?code=" + account.getActivationCode() + "\n" +
                "\n" +
                "Если вы не регистрировались в нашем приложении, проигнорируйте это письмо.\n" +
                "\n" +
                "С уважением,\n" +
                "Уразов Илья Владимирович!";
        mailService.sendEmail(account.getEmail(), message, "Подтверждение регистрации");

        return "";
    }

    public String forgotPassword(Account account) {
        try {
            this.accountRepository.findByEmail(account.getEmail());
            account.setResetPasswordCode(UUID.randomUUID().toString());
            this.accountRepository.save(account);
            String message = "Здравствуйте, " + account.getName() + ".\n" +
                    "\n" +
                    "Вы получили это письмо, потому что запросили восстановление пароля для вашей учётной записи в нашем приложении. Чтобы сбросить пароль и получить доступ к вашей учётной записи, пожалуйста, нажмите на следующую ссылку:\n" +
                    "\n" +
                    "http://localhost:8080/password/reset?code=" + account.getResetPasswordCode() + "\n" +
                    "\n" +
                    "Если вы не запрашивали сброс пароля, проигнорируйте это письмо.\n" +
                    "\n" +
                    "С уважением,\n" +
                    "Уразов Илья Владимирович!";
            mailService.sendEmail(account.getEmail(), message, "Восстановление пароля");
            return "";
        } catch (NullPointerException e) {
            return "Аккаунт с такой почтой не существует";
        }
    }

    public Account getAccountByName(String name) {
        return this.accountRepository.findByName(name);
    }

    public Account getAccountByEmail(String email) {
        return this.accountRepository.findByEmail(email);
    }

    public Account getAccountByResetPasswordCode(String code) {
        return this.accountRepository.findByResetPasswordCode(code);
    }

    public boolean activateAccount(String code) {
        Account account = accountRepository.findByActivationCode(code);
        if (account == null) {
            return false;
        }
        account.setActivationCode(null);
        account.setActive(true);
        accountRepository.save(account);
        return true;
    }

    public boolean resetPassword(String code, String newPassword) {
        Account account = accountRepository.findByResetPasswordCode(code);
        if (account == null) {
            return false;
        }
        account.setResetPasswordCode(null);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return true;
    }
}
