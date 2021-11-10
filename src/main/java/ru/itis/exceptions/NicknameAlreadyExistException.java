package ru.itis.exceptions;

import ru.itis.exceptions.marks.InterfaceSignUpException;

public class NicknameAlreadyExistException extends Throwable implements InterfaceSignUpException {
    @Override
    public String getMessage() {
        return "Данный никнейм уже занят.";
    }
}
