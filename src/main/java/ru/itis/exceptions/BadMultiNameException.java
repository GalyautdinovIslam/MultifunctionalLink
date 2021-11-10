package ru.itis.exceptions;

public class BadMultiNameException extends Throwable {
    @Override
    public String getMessage() {
        return "Название ссылки должно содержать только буквы кириллицы или латиницы, цифры и знак пробела. " +
                "Длина названия не должна превышать 64 символов.";
    }
}
