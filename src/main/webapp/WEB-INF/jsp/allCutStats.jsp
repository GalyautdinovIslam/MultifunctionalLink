<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Статистика по коротким ссылкам">
    <h2>Ваши сокращенные ссылки</h2>
    <c:if test="${not empty cutLinks}">
        <p>Всего ${cutLinks.size()} ссылок:</p>
        <c:forEach items="${cutLinks}" var="cut">
            <a href="${pageContext.request.contextPath}/stats/cut?cut=${cut.getCut()}">${cut.getCut()}</a>
            <br>
        </c:forEach>
    </c:if>
    <c:if test="${empty cutLinks}">
        <p>У Вас нет сокращенных ссылок.</p>
    </c:if>
</t:layout>


