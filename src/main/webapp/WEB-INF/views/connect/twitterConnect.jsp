<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../inc/header.jsp"/>
<form action="<c:url value="/connect/twitter" />" method="POST">
	<div class="formInfo">
		<h2>Connect to Twitter</h2>
		<p>Click the button to connect your account with your Twitter account.</p>
	</div>
	<input id="signin" type="submit"/>
</form>
<jsp:include page="../inc/footer.jsp"/>