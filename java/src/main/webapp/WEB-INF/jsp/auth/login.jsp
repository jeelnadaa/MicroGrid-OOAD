<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Login - Microclimate Sensor Grid" />
        </jsp:include>

        <div class="container">
            <div class="row justify-content-center mt-5">
                <div class="col-md-6 col-lg-5">
                    <div class="card shadow-lg">
                        <div class="card-header bg-primary text-white text-center py-4">
                            <i class="bi bi-cloud-sun" style="font-size: 3rem;"></i>
                            <h3 class="mb-0 mt-2">Microclimate Sensor Grid</h3>
                            <p class="mb-0">Management System</p>
                        </div>
                        <div class="card-body p-4">
                            <h4 class="text-center mb-4">Login to Your Account</h4>

                            <form method="post" action="${pageContext.request.contextPath}/login">
                                <div class="mb-3">
                                    <label for="username" class="form-label"><i class="bi bi-person"></i>
                                        Username</label>
                                    <input type="text" class="form-control" id="username" name="username" required
                                        autofocus placeholder="Enter your username">
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label"><i class="bi bi-lock"></i> Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required
                                        placeholder="Enter your password">
                                </div>
                                <div class="mb-3 form-check">
                                    <input type="checkbox" class="form-check-input" id="remember" name="remember">
                                    <label class="form-check-label" for="remember">Remember me</label>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="bi bi-box-arrow-in-right"></i> Login
                                    </button>
                                </div>
                            </form>

                            <hr class="my-4">
                            <p class="text-center mb-0">
                                Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign up
                                    here</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />