<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../inc/header.jsp"/>

<p>Now you are connected to linkedin, you can ask the following thinks:</p>
<ul>
    <li><a href="<c:url value="/connect/linkedin/connections" />">Connections</a></li>
    <li><a href="<c:url value="/connect/linkedin/profile" />">My Profile</a></li>
</ul>
<jsp:include page="../inc/footer.jsp"/>