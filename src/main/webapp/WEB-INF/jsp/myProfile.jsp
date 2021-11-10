<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Мой профиль">
    <h2>Ваш профиль</h2>
    <a href="${pageContext.request.contextPath}/stats">Статистика</a>
    <a href="${pageContext.request.contextPath}/edit">Редактирование</a>
    <a href="${pageContext.request.contextPath}/settings">Настройки</a>
    <br>
    <c:if test="${profile.getMultiLinks().size() > 0}">
        <p><b>Ссылки:</b></p>
        <c:forEach items="${profile.getMultiLinks()}" var="multiLink">
            <a href="//${multiLink.getLink()}">${multiLink.getName()}</a>
            <br>
        </c:forEach>
    </c:if>
    <c:if test="${profile.getMultiLinks().size() == 0}">
        <p>У Вас нет ссылок.</p>
    </c:if>
    <t:subs />
</t:layout>


