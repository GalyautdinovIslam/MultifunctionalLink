<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Создание короткой ссылки">
    <h2>Сократить ссылку</h2>
    <form id="toValidate" action="${pageContext.request.contextPath}/create/cut" method="POST">
        <label>
            Ваша ссылка:
            <input data-rule="link" type="text" name="link" placeholder="Введите ссылку" required>
        </label>
        <br><br>
        <input type="submit" value="Создать ссылку" />
    </form>
</t:layout>