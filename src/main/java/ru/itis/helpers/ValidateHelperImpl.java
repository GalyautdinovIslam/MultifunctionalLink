package ru.itis.helpers;

import ru.itis.exceptions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateHelperImpl implements ValidateHelper {
    @Override
    public void checkEmail(String email) throws BadEmailAddressException {
        String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
                "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}" +
                "\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) throw new BadEmailAddressException();
    }

    @Override
    public void checkPassword(String password) throws BadPasswordException {
        if (password == null || password.length() > 32 || password.length() < 8 ||
                password.length() == password.replaceAll("[a-z]", "").length() ||
                password.length() == password.replaceAll("[A-Z]", "").length() ||
                password.length() == password.replaceAll("[0-9]", "").length() ||
                password.replaceAll("[A-Za-z0-9]", "").length() == 0)
            throw new BadPasswordException();
    }

    @Override
    public void checkNickname(String nickname) throws BadNicknameException {
        if (nickname == null ||
                nickname.length() > 20 ||
                nickname.length() < 1 ||
                nickname.replaceAll("[A-Za-z0-9]", "").length() > 0)
            throw new BadNicknameException();
    }

    @Override
    public void checkAge(Integer age) throws BadAgeException {
        if (age == null || age < 1 || age > 120) throw new BadAgeException();
    }

    @Override
    public void checkRePassword(String password, String rePassword) throws PasswordMismatchException {
        if (!password.equals(rePassword))
            throw new PasswordMismatchException();
    }

    @Override
    public void checkLink(String link) throws BadLinkException {
        if (link == null || link.length() > 2048 || link.length() < 1) throw new BadLinkException();
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            throw new BadLinkException();
        }
    }

    @Override
    public void checkMultiName(String name) throws BadMultiNameException {
        if(name == null || name.length() > 64 || name.replaceAll("[A-Za-zА-Яа-яЁё0-9 ]", "").length() > 0)
            throw new BadMultiNameException();
    }
}
