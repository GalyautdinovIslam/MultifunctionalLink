package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.models.CutLink;

import java.util.Optional;

public interface CutLinkService {

    void createCut(CutLink cutLink);

    String generateCut();

    void isValid(String link) throws BadLinkException;

    Optional<CutLink> findByCut(String cut);

    Optional<CutLink> findById(Long id);
}
