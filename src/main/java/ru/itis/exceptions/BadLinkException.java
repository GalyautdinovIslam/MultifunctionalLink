package ru.itis.exceptions;

public class BadLinkException extends Throwable {
    @Override
    public String getMessage() {
        return "Неверный формат ссылки.";
    }
}
