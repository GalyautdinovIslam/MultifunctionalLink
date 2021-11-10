package ru.itis.exceptions;

import ru.itis.exceptions.marks.InterfaceSignUpException;

public class BadEmailAddressException extends Throwable implements InterfaceSignUpException {
    @Override
    public String getMessage() {
        return "Введён некорректный адрес электронной почты.";
    }
}
