package ru.itis.services;

import ru.itis.models.Account;
import ru.itis.models.CutLink;
import ru.itis.repositories.CutLinkRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CutLinkServiceImpl implements CutLinkService {
    private final CutLinkRepository cutLinkRepository;

    public CutLinkServiceImpl(CutLinkRepository cutLinkRepository) {
        this.cutLinkRepository = cutLinkRepository;
    }

    @Override
    public String generateCut(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digit = "0123456789";
        char[] alphabet = (upper + lower + digit).toCharArray();

        Random random = new SecureRandom();

        while (true) {
            char[] buf = new char[length];
            for(int i = 0; i < length; i++){
                buf[i] = alphabet[random.nextInt(alphabet.length)];
            }

            String toValid = new String(buf);
            if (!cutLinkRepository.findByCut(toValid).isPresent()) {
                return toValid;
            }
        }
    }

    @Override
    public void createCut(CutLink cutLink) {
        cutLinkRepository.createCut(cutLink);
    }

    @Override
    public void deleteCutById(CutLink cutLink) {
        cutLinkRepository.deleteCutById(cutLink);
    }

    @Override
    public void deleteAllCutByAccount(Account account) {
        cutLinkRepository.deleteAllCutByAccount(account);
    }

    @Override
    public Optional<CutLink> findById(Long id) {
        return cutLinkRepository.findById(id);
    }

    @Override
    public Optional<CutLink> findByCut(String cut) {
        return cutLinkRepository.findByCut(cut);
    }

    @Override
    public List<CutLink> findByAccount(Account account) {
        return cutLinkRepository.findByAccount(account);
    }

    @Override
    public List<CutLink> findAll() {
        return cutLinkRepository.findAll();
    }
}
