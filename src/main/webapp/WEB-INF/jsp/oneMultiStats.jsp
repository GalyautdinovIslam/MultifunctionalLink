<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Статистика по профильной ссылке">
    <h2>Статистика по ссылке ${multiLink.getName()}:</h2>
    <p>Ссылка:
        <c:if test="${multiLink.getLink().toString().length() > multiLink.getLink().toString().replaceAll(\"//\",\"\").length()}">
            <a href="${multiLink.getLink()}">${multiLink.getLink()}</a>
        </c:if>
        <c:if test="${multiLink.getLink().toString().length() == multiLink.getLink().toString().replaceAll(\"//\",\"\").length()}">
            <a href="//${multiLink.getLink()}">${multiLink.getLink()}</a>
        </c:if>
    </p>
    <br>
    <p>Переходов по ссылке: ${multiLink.getClicks()}</p>
    <br>
    <p>Добавлена в профиль: ${multiLink.getAddedAt()}</p>

    <h2>Удаление ссылки</h2>
    <form action="${pageContext.request.contextPath}/deleteMulti" method="post">
        <label>
            <input type="hidden" name="name" value="${multiLink.getName()}">
            <input type="submit" value="Удалить ссылку" />
        </label>
    </form>
</t:layout>
