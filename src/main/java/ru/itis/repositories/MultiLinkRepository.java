package ru.itis.repositories;

import ru.itis.models.Account;
import ru.itis.models.MultiLink;

import java.util.List;
import java.util.Optional;

public interface MultiLinkRepository {

    void createMulti(MultiLink multiLink);

    void deleteMulti(MultiLink multiLink);

    void deleteAllMultiByAccount(Account account);

    void updateClicks(MultiLink multiLink);

    Optional<MultiLink> findByAccountAndName(Account account, String name);

    Optional<MultiLink> findById(Long id);

    List<MultiLink> findByAccount(Account account);

    List<MultiLink> findAll();
}
