<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="inc/header.jsp"/>
<jsp:include page="inc/navigation.jsp"/>
<table>
    <tr>
        <td>First name</td>
        <td><c:out value="${profile.firstName}" /></td>
    </tr>
    <tr>
        <td>Last name</td>
        <td><c:out value="${profile.lastName}" /></td>
    </tr>
    <tr>
        <td>headline</td>
        <td><c:out value="${profile.headline}" /></td>
    </tr>
    <tr>
        <td>PublicProfileUrls</td>
        <td><c:out value="${profile.publicProfileUrl}" /></td>
    </tr>
    <tr>
        <td>Industry</td>
        <td><c:out value="${profile.industry}" /></td>
    </tr>
</table>
<jsp:include page="inc/footer.jsp"/>