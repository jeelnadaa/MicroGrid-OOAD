<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Maintenance Events" />
        </jsp:include>

        <div class="container-fluid mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2><i class="bi bi-tools"></i> Maintenance Events</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/export/maintenance/csv"
                        class="btn btn-outline-success btn-sm"><i class="bi bi-download"></i> Export CSV</a>
                    <a href="${pageContext.request.contextPath}/maintenance/create" class="btn btn-primary"><i
                            class="bi bi-plus-circle"></i> Add Event</a>
                </div>
            </div>

            <form method="get" class="mb-3">
                <div class="row g-2">
                    <div class="col-md-3">
                        <select class="form-select" name="sensor">
                            <option value="">All Sensors</option>
                            <c:forEach var="s" items="${sensors}">
                                <option value="${s.sensorId}" ${sensorFilter==String.valueOf(s.sensorId) ? 'selected'
                                    : '' }>${s.model}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="tech">
                            <option value="">All Technicians</option>
                            <c:forEach var="t" items="${technicians}">
                                <option value="${t.techId}" ${techFilter==String.valueOf(t.techId) ? 'selected' : '' }>
                                    ${t.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="event_type">
                            <option value="">All Types</option>
                            <option value="CALIBRATION" ${eventFilter=='CALIBRATION' ? 'selected' : '' }>Calibration
                            </option>
                            <option value="REPAIR" ${eventFilter=='REPAIR' ? 'selected' : '' }>Repair</option>
                            <option value="REPLACEMENT" ${eventFilter=='REPLACEMENT' ? 'selected' : '' }>Replacement
                            </option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i>
                            Filter</button>
                    </div>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Sensor</th>
                            <th>Technician</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="m" items="${maintenanceEvents}">
                            <tr>
                                <td>${m.maintenanceId}</td>
                                <td>${m.sensorModel}</td>
                                <td>${m.technicianName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${m.eventType == 'CALIBRATION'}"><span
                                                class="badge bg-info">Calibration</span></c:when>
                                        <c:when test="${m.eventType == 'REPAIR'}"><span
                                                class="badge bg-warning text-dark">Repair</span></c:when>
                                        <c:when test="${m.eventType == 'REPLACEMENT'}"><span
                                                class="badge bg-danger">Replacement</span></c:when>
                                    </c:choose>
                                </td>
                                <td>${m.eventDate}</td>
                                <td>${m.notes}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/maintenance/edit?id=${m.maintenanceId}"
                                        class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                                    <form method="post" action="${pageContext.request.contextPath}/maintenance/delete"
                                        class="d-inline" onsubmit="return confirm('Delete this event?')">
                                        <input type="hidden" name="id" value="${m.maintenanceId}">
                                        <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty maintenanceEvents}">
                            <tr>
                                <td colspan="7" class="text-center text-muted">No maintenance events found</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />