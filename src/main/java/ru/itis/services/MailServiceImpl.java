package ru.itis.services;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailServiceImpl implements MailService {

    private final Session session;
    private final String from;

    public MailServiceImpl(Session session, String from) {
        this.session = session;
        this.from = from;
    }

    private Message prepareRecoveryMessage(Session session, String to, String pathForMail) {
        Message message = null;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Восстановление пароля");
            message.setText("Для восстановления пароля воспользуйтесь ссылкой ниже: \n\n" + pathForMail);
        } catch (MessagingException ignored) {
        }
        return message;
    }

    private Message prepareSuccessfulSignUpMessage(Session session, String to, String pathForMail) {
        Message message = null;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Регистрация");
            message.setText("Для завершения регистрации воспользуйтесь ссылкой ниже: \n\n" + pathForMail);
        } catch (MessagingException ignored) {
        }
        return message;
    }

    private Message prepareUnsuccessfulSignUpMessage(Session session, String to, String pathForMail) {
        Message message = null;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Неудачная регистрация");
            message.setText("Этот адрес электронной почты уже зарегистрирован. " +
                    "Чтобы восстановить пароль, воспользуйтесь ссылкой ниже: \n\n" + pathForMail);
        } catch (MessagingException ignored) {
        }
        return message;
    }

    private void sendMessage(Message message) {
        if (message != null) {
            try {
                Transport.send(message);
            } catch (MessagingException ignored) {
            }
        }
    }

    @Override
    public void sendRecoveryMessage(String to, String pathForMail) {
        sendMessage(prepareRecoveryMessage(session, to, pathForMail));
    }

    @Override
    public void sendSuccessfulSignUpMessage(String to, String pathForMail) {
        sendMessage(prepareSuccessfulSignUpMessage(session, to, pathForMail));
    }

    @Override
    public void sendUnsuccessfulSignUpMessage(String to, String pathForMail) {
        sendMessage(prepareUnsuccessfulSignUpMessage(session, to, pathForMail));
    }
}
