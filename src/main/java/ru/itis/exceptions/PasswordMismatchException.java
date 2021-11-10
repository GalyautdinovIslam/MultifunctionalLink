package ru.itis.exceptions;

import ru.itis.exceptions.marks.InterfaceSignUpException;

public class PasswordMismatchException extends Throwable implements InterfaceSignUpException {
    @Override
    public String getMessage() {
        return "Пароли не совпадают.";
    }
}
