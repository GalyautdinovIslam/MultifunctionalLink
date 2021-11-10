<%@ tag description="Standard Layout Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>
<html>
    <t:head title="${title}"/>
    <body>
        <t:header/>
        <main class="main">
            <div class="container">
                <jsp:doBody/>
            </div>
        </main>
        <t:notice />
    </body>
</html>