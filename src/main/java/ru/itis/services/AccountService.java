package ru.itis.services;

import ru.itis.exceptions.*;
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

    void subscribe(Account who, Account subTo) throws AlreadySubscribedException;

    void unsubscribe(Account who, Account subTo) throws AlreadyUnsubscribedException;

    void updateAge(Account account);

    void deleteAccount(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String nickname);
}
