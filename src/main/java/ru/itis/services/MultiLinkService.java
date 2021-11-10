package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.exceptions.BadMultiNameException;
import ru.itis.models.Account;
import ru.itis.models.MultiLink;

import java.util.Optional;

public interface MultiLinkService {

    MultiLink createMulti(MultiLink multiLink);

    void deleteMulti(MultiLink multiLink);

    void deleteALlMultiByAccount(Account account);

    void visit(MultiLink multiLink);

    Optional<MultiLink> findByAccountAndName(Account account, String name);

    Optional<MultiLink> findById(Long id);

    void isValid(String name, String link) throws BadLinkException, BadMultiNameException;
}
