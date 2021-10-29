package ru.itis.services;

import ru.itis.models.Account;
import ru.itis.models.MultiLink;
import ru.itis.repositories.MultiLinkRepository;

import java.util.List;
import java.util.Optional;

public class MultiLinkServiceImpl implements MultiLinkService {
    private final MultiLinkRepository multiLinkRepository;

    public MultiLinkServiceImpl(MultiLinkRepository multiLinkRepository) {
        this.multiLinkRepository = multiLinkRepository;
    }

    @Override
    public void createMulti(MultiLink multiLink) {
        multiLinkRepository.createMulti(multiLink);
    }

    @Override
    public void deleteMultiById(MultiLink multiLink) {
        multiLinkRepository.deleteMultiById(multiLink);
    }

    @Override
    public void deleteAllMultiByAccount(Account account) {
        multiLinkRepository.deleteAllMultiByAccount(account);
    }

    @Override
    public Optional<MultiLink> findById(Long id) {
        return multiLinkRepository.findById(id);
    }

    @Override
    public List<MultiLink> findByAccount(Account account) {
        return multiLinkRepository.findByAccount(account);
    }

    @Override
    public List<MultiLink> findAll() {
        return multiLinkRepository.findAll();
    }
}
