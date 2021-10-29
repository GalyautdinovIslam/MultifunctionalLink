package ru.itis.services;

import ru.itis.repositories.CutLinkRepository;

public interface CutLinkService extends CutLinkRepository {
    String generateCut(int length);
}
