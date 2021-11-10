<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Профиль пользователя ${profile.getNickname()}">
    <h2>
        Профиль пользователя ${profile.getNickname()}
    <c:if test="${profile.getAge() != 0}">
        (${profile.getAge()})
    </c:if>
    </h2>

    <c:if test="${not empty authAccount}">
        <c:if test="${isSub}">
            <form action="${pageContext.request.contextPath}/unsubscribe" method="post">
                <label>
                    <input type="hidden" name="profileToUnsub" value="${profile.getNickname()}">
                    <input type="submit" value="Отписаться">
                </label>
            </form>
        </c:if>
        <c:if test="${not isSub}">
            <form action="${pageContext.request.contextPath}/subscribe" method="post">
                <label>
                    <input type="hidden" name="profileToSub" value="${profile.getNickname()}">
                    <input type="submit" value="Подписаться">
                </label>
            </form>
        </c:if>
    </c:if>
    <c:if test="${profile.getMultiLinks().size() > 0}">
        <p><b>Ссылки пользователя:</b></p>
        <c:forEach items="${profile.getMultiLinks()}" var="multiLink">
            <form action="${pageContext.request.contextPath}/update" method="post">
                <input type="hidden" name="multiLinkInfo" value="${profile.getNickname()}!${multiLink.getName()}">
                <input type="submit" value="${multiLink.getName()}">
            </form>
            <br>
        </c:forEach>
    </c:if>
    <c:if test="${profile.getMultiLinks().size() == 0}">
        <p>У пользователя нет сохраненных ссылок.</p>
    </c:if>
    <t:subs />
</t:layout>
