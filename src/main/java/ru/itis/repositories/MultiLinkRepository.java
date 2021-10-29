package ru.itis.repositories;

import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.models.MultiLink;

import java.util.List;
import java.util.Optional;

public interface MultiLinkRepository{

    void createMulti(MultiLink multiLink);

    void deleteMultiById(MultiLink multiLink);

    void deleteAllMultiByAccount(Account account);

    Optional<MultiLink> findById(Long id);

    List<MultiLink> findByAccount(Account account);

    List<MultiLink> findAll();
}
