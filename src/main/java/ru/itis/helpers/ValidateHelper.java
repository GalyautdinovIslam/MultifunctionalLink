package ru.itis.helpers;

import ru.itis.exceptions.*;

public interface ValidateHelper {

    void checkEmail(String email) throws BadEmailAddressException;

    void checkPassword(String password) throws BadPasswordException;

    void checkNickname(String nickname) throws BadNicknameException;

    void checkAge(Integer age) throws BadAgeException;

    void checkRePassword(String password, String rePassword) throws PasswordMismatchException;

    void checkLink(String link) throws BadLinkException;

    void checkMultiName(String name) throws BadMultiNameException;
}
