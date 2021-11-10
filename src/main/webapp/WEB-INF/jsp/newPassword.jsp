<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Изменение пароль">
    <h2>Изменение пароля</h2>
    <p>
        Чтобы изменить пароль, воспользуйтесь формой ниже.
    </p>
    <form action="${pageContext.request.contextPath}/newPassword" method="POST">
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
</t:layout>


