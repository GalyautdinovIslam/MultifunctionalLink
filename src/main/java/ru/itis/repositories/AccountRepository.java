package ru.itis.repositories;

import ru.itis.exceptions.AlreadySubscribedException;
import ru.itis.exceptions.AlreadyUnsubscribedException;
import ru.itis.models.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account>{

    Account createAccount(Account account);

    void updatePassword(Account account);

    void updateAge(Account account);

    void updateStatus(Long id);

    void subscribe(Account who, Account subTo) throws AlreadySubscribedException;

    void unsubscribe(Account who, Account subTo) throws AlreadyUnsubscribedException;

    void deleteAccount(Account account);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByNickname(String nickname);
}
