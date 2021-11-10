package ru.itis.repositories;

import ru.itis.models.Account;
import ru.itis.models.CutLink;

import java.util.List;
import java.util.Optional;

public interface CutLinkRepository {

    CutLink createCut(CutLink cutLink);

    void deleteCut(CutLink cutLink);

    void deleteAllCutByAccount(Account account);

    void updateClicks(CutLink cutLink);

    Optional<CutLink> findById(Long id);

    Optional<CutLink> findByCut(String cut);

    List<CutLink> findByAccount(Account account);

    List<CutLink> findAll();
}
