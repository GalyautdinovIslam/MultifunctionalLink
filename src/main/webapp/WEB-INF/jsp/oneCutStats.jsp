<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Статистика по короткой ссылке">
    <h2>Статистика по ссылке ${cutLink.getCut()}:</h2>
    <input type="hidden" value="${linkToCopy}" id="link">
    <button id="copyButton">Скопировать</button>
    <p>
        Оригинальная ссылка:
        <a href="//${cutLink.getLink()}">
            ${cutLink.getLink()}
        </a>
    </p>
    <br>
    <p>Переходов по ссылке: ${cutLink.getClicks()}</p>
    <br>
    <p>Добавлена в профиль: ${cutLink.getAddedAt()}</p>

    <h2>Удаление ссылки</h2>
    <form action="${pageContext.request.contextPath}/deleteCut" method="post">
        <label>
            <input type="hidden" name="cut" value="${cutLink.getCut()}">
            <input type="submit" value="Удалить ссылку" />
        </label>
    </form>
</t:layout>

