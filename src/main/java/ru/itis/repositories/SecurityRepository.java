package ru.itis.repositories;

import ru.itis.models.Account;

import java.util.Optional;

public interface SecurityRepository {

    Optional<Account> findByRecoveryCode(String recoveryCode);

    void createRecoveryCode(Account account, String recoveryCode);

    void deleteRecoveryCode(Long id);

    Optional<Account> findBySignUpCode(String code);

    void createSignUpCode(Account account, String code);

    void deleteSignUpCode(Long id);
}
