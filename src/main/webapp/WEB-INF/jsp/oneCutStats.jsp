<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Статистика по короткой ссылке">
    <h2>Статистика по ссылке ${cutLink.getCut()}:</h2>
    <c:if test="${linkToCopy.length() > linkToCopy.replaceAll(\"//\",\"\").length()}">
        <input type="hidden" value="${linkToCopy}" id="link">
    </c:if>
    <c:if test="${linkToCopy.length() == linkToCopy.replaceAll(\"//\",\"\").length()}">
        <input type="hidden" value="//${linkToCopy}" id="link">
    </c:if>
    <button id="copyButton">Скопировать</button>
    <p>
        Оригинальная ссылка:
        <c:if test="${cutLink.getLink().toString().length() > cutLink.getLink().toString().replaceAll(\"//\",\"\").length()}">
            <a href="${cutLink.getLink()}">${cutLink.getLink()}</a>
        </c:if>
        <c:if test="${cutLink.getLink().toString().length() == cutLink.getLink().toString().replaceAll(\"//\",\"\").length()}">
            <a href="//${cutLink.getLink()}">${cutLink.getLink()}</a>
        </c:if>
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

