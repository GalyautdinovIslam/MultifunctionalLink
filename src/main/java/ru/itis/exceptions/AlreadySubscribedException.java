package ru.itis.exceptions;

public class AlreadySubscribedException extends Throwable {
    @Override
    public String getMessage() {
        return "Подписка уже оформлена.";
    }
}
