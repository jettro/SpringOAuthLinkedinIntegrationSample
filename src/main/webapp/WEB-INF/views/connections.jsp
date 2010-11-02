<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table>
    <c:forEach items="${names}" var="name">
        <tr>
            <td><c:out value='${name}'/></td>
        </tr>
    </c:forEach>
</table>