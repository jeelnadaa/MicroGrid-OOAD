<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="${technician != null ? 'Edit' : 'Create'} Technician" />
        </jsp:include>

        <div class="container mt-4">
            <h2><i class="bi bi-person-badge"></i> ${technician != null ? 'Edit' : 'Create'} Technician</h2>
            <hr>

            <form method="post"
                action="${pageContext.request.contextPath}/technicians/${technician != null ? 'edit' : 'create'}">
                <c:if test="${technician != null}">
                    <input type="hidden" name="id" value="${technician.techId}">
                </c:if>

                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" class="form-control" id="name" name="name" required
                        value="${technician != null ? technician.name : ''}">
                </div>
                <div class="mb-3">
                    <label for="contact_no" class="form-label">Contact Number</label>
                    <input type="text" class="form-control" id="contact_no" name="contact_no"
                        value="${technician != null ? technician.contactNo : ''}">
                </div>
                <div class="mb-3">
                    <label for="specialization" class="form-label">Specialization</label>
                    <input type="text" class="form-control" id="specialization" name="specialization"
                        value="${technician != null ? technician.specialization : ''}">
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-circle"></i> ${technician != null ?
                    'Update' : 'Create'}</button>
                <a href="${pageContext.request.contextPath}/technicians" class="btn btn-secondary">Cancel</a>
            </form>
        </div>

        <jsp:include page="../includes/footer.jsp" />