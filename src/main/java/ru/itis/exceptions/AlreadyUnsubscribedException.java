package ru.itis.exceptions;

public class AlreadyUnsubscribedException extends Throwable {
    @Override
    public String getMessage() {
        return "Подписка не существует.";
    }
}
