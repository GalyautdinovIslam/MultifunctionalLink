<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Войти">
    <h2>Войти</h2>
    <form action="${pageContext.request.contextPath}/signIn" method="POST">
        <label>
            Адрес электронной почты:
            <input type="email" name="email" placeholder="Введите адрес электронной почты" value="${email}" required>
        </label>
        <br><br>
        <label>
            Пароль:
            <input type="password" name="password" placeholder="Введите пароль" required>
        </label>
        <br><br>
        <input type="submit" value="Войти" />
        <a href="${pageContext.request.contextPath}/recovery">Забыли пароль?</a>
    </form>
</t:layout>
