package ru.itis.exceptions;

public class SamePasswordException extends Throwable {
    @Override
    public String getMessage() {
        return "Новый пароль не должен совпадать со старым.";
    }
}
