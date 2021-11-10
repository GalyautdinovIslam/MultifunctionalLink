<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Регистрация">
    <h2>Регистрация</h2>
    <form id="toValidate" action="${pageContext.request.contextPath}/signUp" method="POST">
        <label>
            Адрес электронной почты:
            <input data-rule="email" type="email" name="email" placeholder="Введите адрес электронной почты" value="${email}" required>
        </label>
        <br><br>
        <label>
            Пароль:
            <input data-rule="password" type="password" name="password" placeholder="Введите пароль" required>
        </label>
        <br><br>
        <label>
            Повторите пароль:
            <input type="password" name="rePassword" placeholder="Повторите пароль" required>
        </label>
        <br><br>
        <label>
            Никнейм:
            <input data-rule="nickname" type="text" name="nickname" placeholder="Введите никнейм" value="${nickname}" required>
        </label>
        <br><br>
        <input type="submit" value="Зарегистрироваться" />
    </form>
</t:layout>