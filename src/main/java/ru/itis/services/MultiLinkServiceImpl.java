package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.helpers.ValidateHelper;
import ru.itis.models.MultiLink;
import ru.itis.repositories.MultiLinkRepository;

import java.util.Optional;

public class MultiLinkServiceImpl implements MultiLinkService {

    private final MultiLinkRepository multiLinkRepository;
    private final ValidateHelper validator;

    public MultiLinkServiceImpl(MultiLinkRepository multiLinkRepository, ValidateHelper validator) {
        this.multiLinkRepository = multiLinkRepository;
        this.validator = validator;
    }

    @Override
    public void createMulti(MultiLink multiLink) {
        multiLinkRepository.createMulti(multiLink);
    }

    @Override
    public Optional<MultiLink> findById(Long id) {
        return multiLinkRepository.findById(id);
    }

    @Override
    public void isValid(String link) throws BadLinkException {
        validator.checkLink(link);
    }
}
