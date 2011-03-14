<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../inc/header.jsp"/>
<form action="<c:url value="/connect/linkedin" />" method="POST">
	<div class="formInfo">
		<h2>Connect to LinkedIn</h2>
		<p>Click the button to connect your account with your LinkedIn account.</p>
	</div>
	<input id="signin" type="submit"/>
</form>
<jsp:include page="../inc/footer.jsp"/>