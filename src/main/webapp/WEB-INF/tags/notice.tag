<%@ tag description="Standard Head Tag" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${messageMap.size() > 0}">
    <notices class="notices" id="notices">
        <c:forEach items="${messageMap.entrySet()}" var="entry">
            <c:if test="${entry.getValue()}">
                <true-notice class="true-notice">
                    ${entry.getKey()}
                </true-notice>
            </c:if>
            <c:if test="${!entry.getValue()}">
                <false-notice class="false-notice">
                    ${entry.getKey()}
                </false-notice>
            </c:if>
        </c:forEach>
    </notices>
</c:if>
