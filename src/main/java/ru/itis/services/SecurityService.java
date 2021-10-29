package ru.itis.services;

import ru.itis.exceptions.*;
import ru.itis.forms.AccountRegisterForm;
import ru.itis.models.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface SecurityService {

    Optional<Account> checkRecoveryCode(String recoveryCode);

    String generateRecoveryCode(Account account);

    void deleteRecoveryCode(String recoveryCode);

    boolean isAuth(HttpServletRequest request);

    Account getAuthAccount(HttpServletRequest request);

    void signup(HttpServletRequest request, AccountRegisterForm accountRegisterForm) throws BadEmailAddressException, BadNicknameException, NicknameAlreadyExistException, BadPasswordException, SignUpPasswordMismatchException, EmailAlreadyExistException;

    void signin(HttpServletRequest request, String email, String password) throws IncorrectSignInDataException;

    void logout(HttpServletRequest request);
}
