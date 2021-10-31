package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.models.MultiLink;

import java.util.Optional;

public interface MultiLinkService {

    void createMulti(MultiLink multiLink);

    Optional<MultiLink> findById(Long id);

    void isValid(String link) throws BadLinkException;
}
