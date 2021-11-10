<%@ tag description="Standard Layout Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="subs">
    <div class="subscriptions">
        <p><b>Подписки (${profile.getSubscriptions().size()}):</b></p>
        <c:if test="${profile.getSubscriptions().size() > 0}">
            <c:forEach items="${profile.getSubscriptions()}" var="s">
                <a href="${pageContext.request.contextPath}/profile/${s.getNickname()}">${s.getNickname()}</a>
                <br>
            </c:forEach>
        </c:if>
        <c:if test="${profile.getSubscriptions().size() == 0}">
            <p>У пользователя нет подписок.</p>
        </c:if>
    </div>
    <div class="subscribers">
        <p><b>Подписчики (${profile.getSubscribers().size()}):</b></p>
        <c:if test="${profile.getSubscribers().size() > 0}">
            <c:forEach items="${profile.getSubscribers()}" var="s">
                <a href="${pageContext.request.contextPath}/profile/${s.getNickname()}">${s.getNickname()}</a>
                <br>
            </c:forEach>
        </c:if>
        <c:if test="${profile.getSubscribers().size() == 0}">
            <p>На пользователя никто не подписан.</p>
        </c:if>
    </div>
</div>