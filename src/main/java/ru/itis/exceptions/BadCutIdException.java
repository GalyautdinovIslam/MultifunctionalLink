package ru.itis.exceptions;

public class BadCutIdException extends Throwable {
    @Override
    public String getMessage() {
        return "Короткая ссылка с таким id не существует, либо не доступна Вам.";
    }
}
