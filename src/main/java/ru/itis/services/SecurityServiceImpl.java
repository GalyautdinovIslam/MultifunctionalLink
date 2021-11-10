package ru.itis.services;

import ru.itis.exceptions.*;
import ru.itis.exceptions.marks.InterfaceSignUpException;
import ru.itis.forms.AccountSignInForm;
import ru.itis.forms.AccountSignUpForm;
import ru.itis.helpers.EncryptHelper;
import ru.itis.helpers.ValidateHelper;
import ru.itis.models.Account;
import ru.itis.repositories.AccountRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SecurityServiceImpl implements SecurityService {

    private final EncryptHelper encryptHelper;
    private final ValidateHelper validator;
    private final AccountRepository accountRepository;

    public SecurityServiceImpl(EncryptHelper encryptHelper, ValidateHelper validator, AccountRepository accountRepository) {
        this.encryptHelper = encryptHelper;
        this.validator = validator;
        this.accountRepository = accountRepository;
    }

    private void checkNicknameExisting(String nickname) throws NicknameAlreadyExistException {
        if (accountRepository.findByNickname(nickname).isPresent())
            throw new NicknameAlreadyExistException();
    }

    @Override
    public boolean isAuth(HttpServletRequest request) {
        return request.getAttribute("authAccount") != null;
    }

    @Override
    public Account getAuthAccount(HttpServletRequest request) {
        return (Account) request.getAttribute("authAccount");
    }

    @Override
    public void updateAuthAccount(HttpServletRequest request, Account account) {
        request.getSession().setAttribute("authAccount", account);
    }

    @Override
    public void signup(HttpServletRequest request, AccountSignUpForm accountSignUpForm, List<InterfaceSignUpException> exceptions) throws EmailAlreadyExistException, SignUpException {
        String email = accountSignUpForm.getEmail();
        String password = accountSignUpForm.getPassword();
        String rePassword = accountSignUpForm.getRePassword();
        String nickname = accountSignUpForm.getNickname();

        try {
            validator.checkEmail(email);
        } catch (BadEmailAddressException e) {
            exceptions.add(e);
        }

        try {
            validator.checkNickname(nickname);
        } catch (BadNicknameException e) {
            exceptions.add(e);
        }

        try {
            checkNicknameExisting(nickname);
        } catch (NicknameAlreadyExistException e) {
            exceptions.add(e);
        }

        try {
            validator.checkPassword(password);
        } catch (BadPasswordException e) {
            exceptions.add(e);
        }

        try {
            validator.checkRePassword(password, rePassword);
        } catch (PasswordMismatchException e) {
            exceptions.add(e);
        }

        if (exceptions.size() > 0) throw new SignUpException();

        if (accountRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistException();

        password = encryptHelper.encryptPassword(password);
        Account account = new Account(email, password, nickname);
        accountRepository.createAccount(account);
        request.setAttribute("justSignUp", account);
    }

    @Override
    public void signIn(HttpServletRequest request, AccountSignInForm accountSignInForm) throws IncorrectSignInDataException {
        String email = accountSignInForm.getEmail();
        String password = encryptHelper.encryptPassword(accountSignInForm.getPassword());

        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if (!optionalAccount.isPresent()) {
            throw new IncorrectSignInDataException();
        }

        Account account = optionalAccount.get();

        if (password.equals(account.getPassword())) {
            login(request, account);
        } else {
            throw new IncorrectSignInDataException();
        }
    }

    @Override
    public void login(HttpServletRequest request, Account account) {
        request.getSession().setAttribute("authAccount", account);
    }

    @Override
    public void logout(HttpServletRequest request) {
        request.getSession().setAttribute("authAccount", null);
        request.setAttribute("authAccount", null);
    }

}
