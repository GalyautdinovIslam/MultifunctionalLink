package ru.itis.exceptions;

public class IncorrectSignInDataException extends Throwable {
    @Override
    public String getMessage() {
        return "Неверный адрес электронной почты или пароль.";
    }
}
