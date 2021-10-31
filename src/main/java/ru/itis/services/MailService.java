package ru.itis.services;

public interface MailService {

    void sendRecoveryMessage(String to, String pathForMail);

    void sendSuccessfulSignUpMessage(String to, String pathForMail);

    void sendUnsuccessfulSignUpMessage(String to, String pathForMail);
}
