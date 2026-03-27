<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
    <jsp:include page="../includes/header.jsp">
        <jsp:param name="pageTitle" value="500 - Server Error" />
    </jsp:include>

    <div class="container text-center mt-5">
        <h1 class="display-1 text-danger">500</h1>
        <h2>Internal Server Error</h2>
        <p class="lead">Something went wrong. Please try again later.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary"><i class="bi bi-house"></i> Go to
            Dashboard</a>
    </div>

    <jsp:include page="../includes/footer.jsp" />