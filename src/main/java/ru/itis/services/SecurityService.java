package ru.itis.services;

import ru.itis.exceptions.EmailAlreadyExistException;
import ru.itis.exceptions.IncorrectSignInDataException;
import ru.itis.exceptions.SignUpException;
import ru.itis.exceptions.marks.InterfaceSignUpException;
import ru.itis.forms.AccountSignInForm;
import ru.itis.forms.AccountSignUpForm;
import ru.itis.models.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SecurityService {

    boolean isAuth(HttpServletRequest request);

    Account getAuthAccount(HttpServletRequest request);

    void updateAuthAccount(HttpServletRequest request, Account account);

    void signup(HttpServletRequest request, AccountSignUpForm accountSignUpForm, List<InterfaceSignUpException> exceptions)
            throws SignUpException, EmailAlreadyExistException;

    void signIn(HttpServletRequest request, AccountSignInForm accountSignInForm) throws IncorrectSignInDataException;

    void login(HttpServletRequest request, Account account);

    void logout(HttpServletRequest request);
}
