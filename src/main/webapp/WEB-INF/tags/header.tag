<%@ tag description="Standard Head Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="header">
    <div class="container">
        <div class="header-row">
            <div class="header-logo">
                <a href="${pageContext.request.contextPath}">
                    <img src="${pageContext.request.contextPath}/static/img/logo.png" alt="Logo">
                </a>
            </div>
            <div class="header-search">
                <form action="${pageContext.request.contextPath}/profile/" method="post">
                    <label>
                        <input type="text" name="search" placeholder="Введите никнейм пользователя">
                        <input type="submit" value="Поиск">
                    </label>
                </form>
            </div>
            <div class="header-right-block">
                <c:if test="${not empty authAccount}">
                    <a href="${pageContext.request.contextPath}/create" class="btn">Создать</a>
                    <a href="${pageContext.request.contextPath}/my" class="btn">${authAccount.getNickname()}</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn">Выйти</a>
                </c:if>
                <c:if test="${empty authAccount}">
                    <a href="${pageContext.request.contextPath}/signIn" class="btn">Войти</a>
                    <a href="${pageContext.request.contextPath}/signUp" class="btn">Зарегистрироваться</a>
                </c:if>
            </div>
        </div>
    </div>
</header>