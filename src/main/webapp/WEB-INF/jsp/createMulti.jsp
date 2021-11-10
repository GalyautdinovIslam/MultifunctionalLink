<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Сохранение новой ссылки">
    <h2>Добавление ссылки в профиль</h2>
    <form id="toValidate" action="${pageContext.request.contextPath}/create/multi" method="POST">
        <label>
            Название ссылки:
            <input type="text" name="name" placeholder="Введите название" required>
        </label>
        <br><br>
        <label>
            Ваша ссылка:
            <input data-rule="link" type="text" name="link" placeholder="Введите ссылку" required>
        </label>
        <br><br>
        <input type="submit" value="Сохранить ссылку" />
    </form>
</t:layout>

