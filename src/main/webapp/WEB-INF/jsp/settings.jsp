<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Настройки">
    <h2>Изменение пароля</h2>
    <form action="${pageContext.request.contextPath}/settings" method="POST">
        <label>
            Старый пароль:
            <input type="password" name="oldPassword" placeholder="Введите старый пароль" required>
        </label>
        <br><br>
        <label>
            Новый пароль:
            <input type="password" name="newPassword" placeholder="Введите новый пароль" required>
        </label>
        <br><br>
        <label>
            Повторите пароль:
            <input type="password" name="reNewPassword" placeholder="Повторите пароль" required>
        </label>
        <br><br>
        <input type="submit" value="Изменить пароль" />
    </form>

    <h2>Удаление аккаунта</h2>
    <form action="${pageContext.request.contextPath}/deleteAccount" method="post">
        <p>Введите пароль, чтобы удалить свой аккаунт навсегда. Внимание, все сокращенные ссылки перестанут быть действительными!</p>
        <br>
        <label>
            Введите пароль:
            <input type="password" name="oldPassword" placeholder="Введите пароль" required>
        </label>
        <br><br>
        <input type="submit" value="Удалить аккаунт" />
    </form>
</t:layout>
