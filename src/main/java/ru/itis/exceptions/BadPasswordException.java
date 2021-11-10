package ru.itis.exceptions;

import ru.itis.exceptions.marks.InterfaceSignUpException;

public class BadPasswordException extends Throwable implements InterfaceSignUpException {
    @Override
    public String getMessage() {
        return "Ваш пароль должен быть от 8 до 32 символов. " +
                "В нём как минимум должны присутствовать по одной " +
                "заглавной и прописной латинской букве и цифре. " +
                "Также должен присутствовать минимум один любой другой символ";
    }
}
