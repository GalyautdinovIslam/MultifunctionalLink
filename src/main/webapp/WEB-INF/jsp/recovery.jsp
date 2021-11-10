<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Восстановление пароля">
    <h2>Восстановление пароля</h2>
    <p>
        Чтобы восстановить пароль воспользуйтесь формой ниже.
        После заполнения на Ваш адрес электронной почты будет отправлена инструкция по восстановлению.
    </p>
    <form action="${pageContext.request.contextPath}/recovery" method="POST">
        <label>
            Адрес электронной почты или никнейм:
            <input type="text" name="emailOrNickname" placeholder="Введите адрес электронной почты или никнейм" required>
        </label>
        <br><br>
        <input type="submit" value="Продолжить" />
    </form>
</t:layout>
