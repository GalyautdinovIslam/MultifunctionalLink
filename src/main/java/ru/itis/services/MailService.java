package ru.itis.services;

public interface MailService {
    void sendRecoveryMessage(String to, String pathForMail);
}
