<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Короткая ссылка">
    <h2>Через
        <span id="span">10</span>
        секунд Вы будете переведены по ссылке:
    </h2>
    <a href="${cutLink.getLink()}" id="cutLinkHref">
        <p id="cutLink">
            ${cutLink.getLink()}
        </p>
    </a>
</t:layout>

