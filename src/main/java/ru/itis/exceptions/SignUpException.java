package ru.itis.exceptions;

import ru.itis.exceptions.marks.InterfaceSignUpException;

public class SignUpException extends Throwable implements InterfaceSignUpException {
    @Override
    public String getMessage() {
        return "Ошибка при регистрации.";
    }
}
