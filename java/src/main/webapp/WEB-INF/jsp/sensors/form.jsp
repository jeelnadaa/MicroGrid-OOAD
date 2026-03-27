<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="${sensor != null ? 'Edit' : 'Create'} Sensor" />
        </jsp:include>

        <div class="container mt-4">
            <h2><i class="bi bi-cpu"></i> ${sensor != null ? 'Edit' : 'Create'} Sensor</h2>
            <hr>

            <form method="post"
                action="${pageContext.request.contextPath}/sensors/${sensor != null ? 'edit' : 'create'}">
                <c:if test="${sensor != null}">
                    <input type="hidden" name="id" value="${sensor.sensorId}">
                </c:if>

                <div class="mb-3">
                    <label for="model" class="form-label">Model</label>
                    <input type="text" class="form-control" id="model" name="model" required
                        value="${sensor != null ? sensor.model : ''}">
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="install_date" class="form-label">Install Date</label>
                        <input type="date" class="form-control" id="install_date" name="install_date" required
                            value="${sensor != null ? sensor.installDate : ''}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="status" class="form-label">Status</label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="ACTIVE" ${sensor !=null && sensor.status=='ACTIVE' ? 'selected' : '' }>Active
                            </option>
                            <option value="INACTIVE" ${sensor !=null && sensor.status=='INACTIVE' ? 'selected' : '' }>
                                Inactive</option>
                            <option value="MAINTENANCE" ${sensor !=null && sensor.status=='MAINTENANCE' ? 'selected'
                                : '' }>Maintenance</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="type_id" class="form-label">Sensor Type</label>
                        <select class="form-select" id="type_id" name="type_id" required>
                            <option value="">Select Type...</option>
                            <c:forEach var="st" items="${sensorTypes}">
                                <option value="${st.typeId}" ${sensor !=null && sensor.typeId==st.typeId ? 'selected'
                                    : '' }>${st.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="location_id" class="form-label">Location</label>
                        <select class="form-select" id="location_id" name="location_id" required>
                            <option value="">Select Location...</option>
                            <c:forEach var="loc" items="${locations}">
                                <option value="${loc.locationId}" ${sensor !=null && sensor.locationId==loc.locationId
                                    ? 'selected' : '' }>${loc.areaName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-circle"></i> ${sensor != null ?
                    'Update' : 'Create'}</button>
                <a href="${pageContext.request.contextPath}/sensors" class="btn btn-secondary">Cancel</a>
            </form>
        </div>

        <jsp:include page="../includes/footer.jsp" />