package ru.itis.exceptions;

import ru.itis.exceptions.marks.InterfaceSignUpException;

public class BadNicknameException extends Throwable implements InterfaceSignUpException {
    @Override
    public String getMessage() {
        return "Ваш никнейм может состоять только из латинских букв и цифр. " +
                "Также он не должен превышать 20 символов в длину.";
    }
}
