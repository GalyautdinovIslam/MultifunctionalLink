<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Статистика">
    <a href="${pageContext.request.contextPath}/stats/multi">Статистика по ссылкам профиля</a>
    <br>
    <a href="${pageContext.request.contextPath}/stats/cut">Статистика по коротким ссылкам</a>
</t:layout>


