package ru.itis.services;

import ru.itis.exceptions.AlreadySubscribedException;
import ru.itis.exceptions.AlreadyUnsubscribedException;
import ru.itis.models.Account;
import ru.itis.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void createAccount(Account account) {
        accountRepository.createAccount(account);
    }

    @Override
    public void updatePassword(Account account) {
        accountRepository.updatePassword(account);
    }

    @Override
    public void updateAge(Account account) {
        accountRepository.updateAge(account);
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

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
