<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Редактирование">
    <h2>Редактирование профиля:</h2>
    <form action="${pageContext.request.contextPath}/edit" method="POST">
        <label>
            Ваш возраст:
            <input type="text" name="age" placeholder="Укажите возраст" required>
        </label>
        <br><br>
        <input type="submit" value="Сохранить" />
    </form>
</t:layout>
