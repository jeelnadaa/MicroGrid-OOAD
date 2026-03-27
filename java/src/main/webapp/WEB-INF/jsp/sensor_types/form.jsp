<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="${sensorType != null ? 'Edit' : 'Create'} Sensor Type" />
        </jsp:include>

        <div class="container mt-4">
            <h2><i class="bi bi-tags"></i> ${sensorType != null ? 'Edit' : 'Create'} Sensor Type</h2>
            <hr>

            <form method="post"
                action="${pageContext.request.contextPath}/sensor-types/${sensorType != null ? 'edit' : 'create'}">
                <c:if test="${sensorType != null}">
                    <input type="hidden" name="id" value="${sensorType.typeId}">
                </c:if>

                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" class="form-control" id="name" name="name" required
                        value="${sensorType != null ? sensorType.name : ''}">
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label">Description</label>
                    <textarea class="form-control" id="description" name="description"
                        rows="3">${sensorType != null ? sensorType.description : ''}</textarea>
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-circle"></i> ${sensorType != null ?
                    'Update' : 'Create'}</button>
                <a href="${pageContext.request.contextPath}/sensor-types" class="btn btn-secondary">Cancel</a>
            </form>
        </div>

        <jsp:include page="../includes/footer.jsp" />