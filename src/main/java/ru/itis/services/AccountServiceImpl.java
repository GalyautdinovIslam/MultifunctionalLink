package ru.itis.services;

import ru.itis.exceptions.*;
import ru.itis.forms.ChangePasswordForm;
import ru.itis.forms.RecoveryPasswordForm;
import ru.itis.helpers.CodeGenerator;
import ru.itis.helpers.EncryptHelper;
import ru.itis.helpers.ValidateHelper;
import ru.itis.models.Account;
import ru.itis.repositories.AccountRepository;
import ru.itis.repositories.SecurityRepository;

import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private final EncryptHelper encryptHelper;
    private final ValidateHelper validateHelper;
    private final CodeGenerator codeGenerator;
    private final SecurityRepository securityRepository;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(EncryptHelper encryptHelper, ValidateHelper validateHelper,
                              CodeGenerator codeGenerator, SecurityRepository securityRepository,
                              AccountRepository accountRepository) {
        this.encryptHelper = encryptHelper;
        this.validateHelper = validateHelper;
        this.codeGenerator = codeGenerator;
        this.securityRepository = securityRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> checkRecoveryCode(String recoveryCode) {
        return securityRepository.findByRecoveryCode(encryptHelper.encryptCode(recoveryCode));
    }

    @Override
    public String generateRecoveryCode(String email) {

        Account account = accountRepository.findByEmail(email).get();

        while (true) {
            String toValid = codeGenerator.generateCode(128 + (int) Math.floor(Math.random() * 129));
            String encrypted = encryptHelper.encryptCode(toValid);

            if (!checkRecoveryCode(encrypted).isPresent()) {
                securityRepository.createRecoveryCode(account, encrypted);
                return toValid;
            }
        }
    }

    @Override
    public Optional<Account> checkSignUpCode(String signUpCode) {
        return securityRepository.findBySignUpCode(encryptHelper.encryptCode(signUpCode));
    }

    @Override
    public String generateSignUpCode(Account account) {
        while (true) {
            String toValid = codeGenerator.generateCode(128 + (int) Math.floor(Math.random() * 129));
            String encrypted = encryptHelper.encryptCode(toValid);

            if (!checkSignUpCode(encrypted).isPresent()) {
                securityRepository.createSignUpCode(account, encrypted);
                return toValid;
            }
        }
    }

    @Override
    public void deleteSignUpCode(Long id) {
        securityRepository.deleteSignUpCode(id);
    }

    @Override
    public void updateStatus(Long id) {
        accountRepository.updateStatus(id);
    }

    @Override
    public void changePassword(Account account, ChangePasswordForm changePasswordForm)
            throws BadOldPasswordException, BadNewPasswordException, PasswordMismatchException, SamePasswordException {
        String oldPassword = encryptHelper.encryptPassword(changePasswordForm.getOldPassword());
        String newPassword = changePasswordForm.getNewPassword();
        String reNewPassword = changePasswordForm.getReNewPassword();

        if (!account.getPassword().equals(oldPassword)) {
            throw new BadOldPasswordException();
        }

        try {
            validateHelper.checkPassword(newPassword);
        } catch (BadPasswordException e) {
            throw new BadNewPasswordException();
        }

        validateHelper.checkRePassword(newPassword, reNewPassword);

        String encrypted = encryptHelper.encryptPassword(newPassword);

        if (oldPassword.equals(encrypted)) throw new SamePasswordException();

        account.setPassword(encrypted);
        accountRepository.updatePassword(account);
    }

    @Override
    public void recoveryPassword(Account account, RecoveryPasswordForm recoveryPasswordForm)
            throws BadNewPasswordException, PasswordMismatchException {
        String newPassword = recoveryPasswordForm.getNewPassword();
        String reNewPassword = recoveryPasswordForm.getReNewPassword();

        try {
            validateHelper.checkPassword(newPassword);
        } catch (BadPasswordException e) {
            throw new BadNewPasswordException();
        }

        validateHelper.checkRePassword(newPassword, reNewPassword);

        String encrypted = encryptHelper.encryptPassword(newPassword);

        account.setPassword(encrypted);
        accountRepository.updatePassword(account);
        securityRepository.deleteRecoveryCode(account.getId());
    }

    @Override
    public void subscribe(Account who, Account subTo) throws AlreadySubscribedException {
        accountRepository.subscribe(who, subTo);
    }

    @Override
    public void unsubscribe(Account who, Account subTo) throws AlreadyUnsubscribedException {
        accountRepository.unsubscribe(who, subTo);
    }

    @Override
    public void updateAge(Account account) {
        accountRepository.updateAge(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.deleteAccount(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Optional<Account> findByNickname(String nickname) {
        return accountRepository.findByNickname(nickname);
    }

}
