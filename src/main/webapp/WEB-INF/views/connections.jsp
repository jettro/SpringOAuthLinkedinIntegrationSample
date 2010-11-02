<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="inc/header.jsp"/>
<jsp:include page="inc/navigation.jsp"/>
<table>
    <c:forEach items="${connections}" var="connection">
        <tr>
            <td><c:out value='${connection.firstName}'/> <c:out value='${connection.lastName}'/></td>
            <td><c:out value='${connection.headline}'/></td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="inc/footer.jsp"/>