package ru.itis.services;

import ru.itis.exceptions.BadLinkException;
import ru.itis.helpers.CodeGenerator;
import ru.itis.helpers.ValidateHelper;
import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.repositories.CutLinkRepository;

import java.util.Optional;

public class CutLinkServiceImpl implements CutLinkService {

    private final ValidateHelper validator;
    private final CodeGenerator codeGenerator;
    private final CutLinkRepository cutLinkRepository;

    public CutLinkServiceImpl(ValidateHelper validator, CodeGenerator codeGenerator, CutLinkRepository cutLinkRepository) {
        this.validator = validator;
        this.codeGenerator = codeGenerator;
        this.cutLinkRepository = cutLinkRepository;
    }

    @Override
    public void createCut(CutLink cutLink) {
        cutLinkRepository.createCut(cutLink);
    }

    @Override
    public String generateCut() {
        while (true) {
            String toValid = codeGenerator.generateCode(8);
            if (!cutLinkRepository.findByCut(toValid).isPresent()) {
                return toValid;
            }
        }
    }

    @Override
    public void deleteCut(CutLink cutLink) {
        cutLinkRepository.deleteCut(cutLink);
    }

    @Override
    public void deleteAllCutsByAccount(Account account) {
        cutLinkRepository.deleteAllCutByAccount(account);
    }

    @Override
    public void visit(CutLink cutLink) {
        cutLinkRepository.updateClicks(cutLink);
    }

    @Override
    public void isValid(String link) throws BadLinkException {
        validator.checkLink(link);
    }

    @Override
    public Optional<CutLink> findByCut(String cut) {
        return cutLinkRepository.findByCut(cut);
    }

    @Override
    public Optional<CutLink> findById(Long id) {
        return cutLinkRepository.findById(id);
    }
}
