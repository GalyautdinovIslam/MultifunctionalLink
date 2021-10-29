package ru.itis.repositories;

import ru.itis.models.Account;
import ru.itis.models.CutLink;

import java.util.List;
import java.util.Optional;

public interface CutLinkRepository{

    void createCut(CutLink cutLink);

    void deleteCutById(CutLink cutLink);

    void deleteAllCutByAccount(Account account);

    Optional<CutLink> findById(Long id);

    Optional<CutLink> findByCut(String cut);

    List<CutLink> findByAccount(Account account);

    List<CutLink> findAll();
}
