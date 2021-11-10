package ru.itis.exceptions;

public class BadMultiIdException extends Throwable {
    @Override
    public String getMessage() {
        return "Ссылка с таким id не существует, либо не доступна Вам.";
    }
}
