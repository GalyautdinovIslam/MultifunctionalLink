<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Статистика по ссылкам профиля">
    <h2>Ссылки Вашего профиля</h2>
    <c:if test="${not empty multiLinks}">
        <p>Всего ${multiLinks.size()} ссылок:</p>
        <c:forEach items="${multiLinks}" var="multi">
            <a href="${pageContext.request.contextPath}/stats/multi?name=${multi.getName()}">${multi.getLink()}</a>
            <br>
        </c:forEach>
    </c:if>
    <c:if test="${empty multiLinks}">
        <p>В Вашем профиле нет ссылок.</p>
    </c:if>
</t:layout>


