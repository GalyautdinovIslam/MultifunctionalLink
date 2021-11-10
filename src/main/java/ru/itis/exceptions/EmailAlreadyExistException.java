package ru.itis.exceptions;

public class EmailAlreadyExistException extends Throwable {
    @Override
    public String getMessage() {
        return "Данный адрес электронной почты уже зарегистрирован.";
    }
}
