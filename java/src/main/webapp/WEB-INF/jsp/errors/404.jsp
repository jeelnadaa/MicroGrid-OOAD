<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
    <jsp:include page="../includes/header.jsp">
        <jsp:param name="pageTitle" value="404 - Not Found" />
    </jsp:include>

    <div class="container text-center mt-5">
        <h1 class="display-1 text-muted">404</h1>
        <h2>Page Not Found</h2>
        <p class="lead">The page you are looking for does not exist.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary"><i class="bi bi-house"></i> Go to
            Dashboard</a>
    </div>

    <jsp:include page="../includes/footer.jsp" />