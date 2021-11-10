package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.models.Account;
import ru.itis.models.CutLink;

import java.util.Optional;

public interface CutLinkService {

    CutLink createCut(CutLink cutLink);

    String generateCut();

    void deleteCut(CutLink cutLink);

    void deleteAllCutsByAccount(Account account);

    void visit(CutLink cutLink);

    void isValid(String link) throws BadLinkException;

    Optional<CutLink> findByCut(String cut);

    Optional<CutLink> findById(Long id);
}
