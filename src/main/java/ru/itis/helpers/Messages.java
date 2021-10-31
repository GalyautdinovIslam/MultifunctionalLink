package ru.itis.helpers;

public enum Messages {
    NOT_AUTH("Войдите в Ваш аккаунт для выполнения этого действия."),
    ALREADY_AUTH("Вы уже авторизованы."),
    JUST_SIGN_UP("Для продолжения регистрации проверьте свою электронную почту."),
    SUCCESSFUL_CHANGE_PASSWORD("Пароль был успешно изменен."),
    CHECK_EMAIL_FOR_RECOVERY("Для продолжения восстановления пароля проверьте свою электронную почту."),
    BAD_LINK_ID("Данной ссылки не существует."),
    SUCCESSFUL_CHANGE_AGE("Возраст был успешно изменен."),
    SUCCESSFUL_SIGN_UP("Регистрация успешно пройдена.");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}
