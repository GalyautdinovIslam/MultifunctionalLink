package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.exceptions.BadMultiNameException;
import ru.itis.helpers.ValidateHelper;
import ru.itis.models.Account;
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
    public MultiLink createMulti(MultiLink multiLink) {
        return multiLinkRepository.createMulti(multiLink);
    }

    @Override
    public void deleteMulti(MultiLink multiLink) {
        multiLinkRepository.deleteMulti(multiLink);
    }

    @Override
    public void deleteALlMultiByAccount(Account account) {
        multiLinkRepository.deleteAllMultiByAccount(account);
    }

    @Override
    public void visit(MultiLink multiLink) {
        multiLinkRepository.updateClicks(multiLink);
    }

    @Override
    public Optional<MultiLink> findByAccountAndName(Account account, String name) {
        return multiLinkRepository.findByAccountAndName(account, name);
    }

    @Override
    public Optional<MultiLink> findById(Long id) {
        return multiLinkRepository.findById(id);
    }

    @Override
    public void isValid(String name, String link) throws BadLinkException, BadMultiNameException {
        validator.checkMultiName(name);
        validator.checkLink(link);
    }
}
