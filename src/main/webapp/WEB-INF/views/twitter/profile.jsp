<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../inc/header.jsp"/>
<jsp:include page="../inc/navigation.jsp"/>
<h2><c:out value='${profile.screenName}'/></h2>
<p><img src="${profile.profileImageUrl}" align="left" alt="Profile image url"/><c:out value='${profile.description}'/></p>
<jsp:include page="../inc/footer.jsp"/>