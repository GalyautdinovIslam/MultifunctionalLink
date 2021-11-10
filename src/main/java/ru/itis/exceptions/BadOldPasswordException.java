package ru.itis.exceptions;

public class BadOldPasswordException extends BadPasswordException {
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
