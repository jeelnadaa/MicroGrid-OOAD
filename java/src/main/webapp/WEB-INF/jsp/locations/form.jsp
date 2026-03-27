<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="${location != null ? 'Edit' : 'Create'} Location" />
        </jsp:include>

        <div class="container mt-4">
            <h2><i class="bi bi-geo-alt"></i> ${location != null ? 'Edit' : 'Create'} Location</h2>
            <hr>

            <form method="post"
                action="${pageContext.request.contextPath}/locations/${location != null ? 'edit' : 'create'}">
                <c:if test="${location != null}">
                    <input type="hidden" name="id" value="${location.locationId}">
                </c:if>

                <div class="mb-3">
                    <label for="area_name" class="form-label">Area Name</label>
                    <input type="text" class="form-control" id="area_name" name="area_name" required
                        value="${location != null ? location.areaName : ''}">
                </div>
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="latitude" class="form-label">Latitude</label>
                        <input type="number" step="0.000001" class="form-control" id="latitude" name="latitude" required
                            value="${location != null ? location.latitude : ''}">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="longitude" class="form-label">Longitude</label>
                        <input type="number" step="0.000001" class="form-control" id="longitude" name="longitude"
                            required value="${location != null ? location.longitude : ''}">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="elevation" class="form-label">Elevation (m)</label>
                        <input type="number" step="0.01" class="form-control" id="elevation" name="elevation"
                            value="${location != null ? location.elevation : '0.0'}">
                    </div>
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-circle"></i> ${location != null ?
                    'Update' : 'Create'}</button>
                <a href="${pageContext.request.contextPath}/locations" class="btn btn-secondary">Cancel</a>
            </form>
        </div>

        <jsp:include page="../includes/footer.jsp" />