<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../inc/header.jsp"/>
<jsp:include page="../inc/navigation.jsp"/>
<table>
    <c:forEach items="${tweets}" var="tweet">
        <tr>
            <td><c:out value='${tweet.fromUser}'/></td>
            <td><c:out value='${tweet.text}'/></td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="../inc/footer.jsp"/>