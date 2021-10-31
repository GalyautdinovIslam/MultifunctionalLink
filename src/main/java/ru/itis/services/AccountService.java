package ru.itis.services;

import ru.itis.exceptions.BadNewPasswordException;
import ru.itis.exceptions.BadOldPasswordException;
import ru.itis.exceptions.PasswordMismatchException;
import ru.itis.exceptions.SamePasswordException;
import ru.itis.forms.ChangePasswordForm;
import ru.itis.forms.RecoveryPasswordForm;
import ru.itis.models.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> checkRecoveryCode(String recoveryCode);

    String generateRecoveryCode(String email);

    Optional<Account> checkSignUpCode(String signUpCode);

    String generateSignUpCode(Account account);

    void deleteSignUpCode(Long id);

    void updateStatus(Long id);

    void changePassword(Account account, ChangePasswordForm changePasswordForm)
            throws BadOldPasswordException, BadNewPasswordException, PasswordMismatchException, SamePasswordException;

    void recoveryPassword(Account account, RecoveryPasswordForm recoveryPasswordForm)
            throws BadNewPasswordException, PasswordMismatchException;

    void updateAge(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String nickname);
}
