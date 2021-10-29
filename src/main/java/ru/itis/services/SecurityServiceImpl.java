package ru.itis.services;

import ru.itis.exceptions.*;
import ru.itis.forms.AccountRegisterForm;
import ru.itis.models.Account;
import ru.itis.repositories.AccountRepository;
import ru.itis.repositories.SecurityRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityServiceImpl implements SecurityService {

    private final AccountRepository accountRepository;
    private final SecurityRepository securityRepository;

    public SecurityServiceImpl(AccountRepository accountRepository, SecurityRepository securityRepository) {
        this.accountRepository = accountRepository;
        this.securityRepository = securityRepository;
    }

    @Override
    public Optional<Account> checkRecoveryCode(String recoveryCode) {
        return securityRepository.findByRecoveryCode(recoveryCode);
    }

    @Override
    public String generateRecoveryCode(Account account) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digit = "0123456789";
        char[] alphabet = (upper + lower + digit).toCharArray();

        Random random = new SecureRandom();

        while (true) {
            char[] buf = new char[256];
            for(int i = 0; i < 256; i++){
                buf[i] = alphabet[random.nextInt(alphabet.length)];
            }

            String toValid = new String(buf);
            if (!checkRecoveryCode(toValid).isPresent()) {
                securityRepository.createRecoveryCode(account, toValid);
                return toValid;
            }
        }
    }

    @Override
    public void deleteRecoveryCode(String recoveryCode) {
        securityRepository.deleteRecoveryCode(recoveryCode);
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
    public void signup(HttpServletRequest request, AccountRegisterForm accountRegisterForm) throws BadEmailAddressException, BadNicknameException, NicknameAlreadyExistException, BadPasswordException, SignUpPasswordMismatchException, EmailAlreadyExistException {
        String email = accountRegisterForm.getEmail();
        String password = accountRegisterForm.getPassword();
        String rePassword = accountRegisterForm.getRePassword();
        String nickname = accountRegisterForm.getNickname();

        String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
                "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}" +
                "\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) throw new BadEmailAddressException();

        if (nickname == null ||
                nickname.length() > 20 ||
                nickname.length() < 1 ||
                nickname.replaceAll("[A-Za-z0-9]", "").length() > 0)
            throw new BadNicknameException();

        if(accountRepository.findByNickname(nickname).isPresent())
            throw new NicknameAlreadyExistException();

        if (password == null || password.length() > 32 || password.length() < 8 ||
                password.length() == password.replaceAll("[a-z]", "").length() ||
                password.length() == password.replaceAll("[A-Z]", "").length() ||
                password.length() == password.replaceAll("[0-9]", "").length() ||
                password.replaceAll("[A-Za-z0-9]", "").length() == 0)
            throw new BadPasswordException();

        if (!password.equals(rePassword))
            throw new SignUpPasswordMismatchException();

        if (accountRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistException();

        Account account = new Account(email, password, nickname);
        accountRepository.createAccount(account);
        request.getSession().setAttribute("authAccount", account);
    }

    @Override
    public void signin(HttpServletRequest request, String email, String password) throws IncorrectSignInDataException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if(!optionalAccount.isPresent()) throw new IncorrectSignInDataException();

        Account account = optionalAccount.get();
        if (password.equals(account.getPassword())) request.getSession().setAttribute("authAccount", account);
        else throw new IncorrectSignInDataException();
    }

    @Override
    public void logout(HttpServletRequest request) {
        request.getSession().setAttribute("authAccount", null);
        request.setAttribute("authAccount", null);
    }
}
