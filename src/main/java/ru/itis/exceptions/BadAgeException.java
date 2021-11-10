package ru.itis.exceptions;

public class BadAgeException extends Throwable {
    @Override
    public String getMessage() {
        return "Неверно указан возраст. Пожалуйста, укажите число от 1 до 120 включительно.";
    }
}
