package com.example.demo.repositories;

import com.example.demo.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);
    Account findByEmail(String email);
    @Query(value = "select * from Account account where account.phone = :phone", nativeQuery = true)
    Account findByPhone(@Param("phone") String phone);

    @Query(value = "select * from Account account where account.activation_code = :activationCode", nativeQuery = true)
    Account findByActivationCode(@Param("activationCode") String activationCode);

    @Query(value = "select * from Account account where account.reset_password_code = :resetPasswordCode", nativeQuery = true)
    Account findByResetPasswordCode(@Param("resetPasswordCode") String resetPasswordCode);
}