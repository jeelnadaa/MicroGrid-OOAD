<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Sign Up - Microclimate Sensor Grid" />
        </jsp:include>

        <div class="container">
            <div class="row justify-content-center mt-5">
                <div class="col-md-6 col-lg-5">
                    <div class="card shadow-lg">
                        <div class="card-header bg-primary text-white text-center py-4">
                            <i class="bi bi-cloud-sun" style="font-size: 3rem;"></i>
                            <h3 class="mb-0 mt-2">Create Account</h3>
                        </div>
                        <div class="card-body p-4">
                            <form method="post" action="${pageContext.request.contextPath}/signup">
                                <div class="mb-3">
                                    <label for="full_name" class="form-label"><i class="bi bi-person"></i> Full
                                        Name</label>
                                    <input type="text" class="form-control" id="full_name" name="full_name"
                                        placeholder="Enter your full name">
                                </div>
                                <div class="mb-3">
                                    <label for="username" class="form-label"><i class="bi bi-at"></i> Username</label>
                                    <input type="text" class="form-control" id="username" name="username" required
                                        placeholder="Choose a username">
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label"><i class="bi bi-envelope"></i> Email</label>
                                    <input type="email" class="form-control" id="email" name="email" required
                                        placeholder="Enter your email">
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label"><i class="bi bi-lock"></i> Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required
                                        placeholder="Min 6 characters" minlength="6">
                                </div>
                                <div class="mb-3">
                                    <label for="confirm_password" class="form-label"><i class="bi bi-lock-fill"></i>
                                        Confirm Password</label>
                                    <input type="password" class="form-control" id="confirm_password"
                                        name="confirm_password" required placeholder="Repeat password">
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="bi bi-person-plus"></i> Create Account
                                    </button>
                                </div>
                            </form>
                            <hr class="my-4">
                            <p class="text-center mb-0">
                                Already have an account? <a href="${pageContext.request.contextPath}/login">Login
                                    here</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />