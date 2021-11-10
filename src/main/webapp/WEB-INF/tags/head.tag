<%@ tag description="Standard Head Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>

    <link rel="icon" href="${pageContext.request.contextPath}/static/img/logo.png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/fonts/Lato/stylesheet.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">

    <script src="${pageContext.request.contextPath}/static/js/copyLink.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/closeNotices.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/goToLink.js"></script>
</head>