<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="${maintenance != null ? 'Edit' : 'Create'} Maintenance Event" />
        </jsp:include>

        <div class="container mt-4">
            <h2><i class="bi bi-tools"></i> ${maintenance != null ? 'Edit' : 'Create'} Maintenance Event</h2>
            <hr>

            <form method="post"
                action="${pageContext.request.contextPath}/maintenance/${maintenance != null ? 'edit' : 'create'}">
                <c:if test="${maintenance != null}">
                    <input type="hidden" name="id" value="${maintenance.maintenanceId}">
                </c:if>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="sensor_id" class="form-label">Sensor</label>
                        <select class="form-select" id="sensor_id" name="sensor_id" required>
                            <option value="">Select Sensor...</option>
                            <c:forEach var="s" items="${sensors}">
                                <option value="${s.sensorId}" ${maintenance !=null && maintenance.sensorId==s.sensorId
                                    ? 'selected' : '' }>${s.model}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="tech_id" class="form-label">Technician</label>
                        <select class="form-select" id="tech_id" name="tech_id" required>
                            <option value="">Select Technician...</option>
                            <c:forEach var="t" items="${technicians}">
                                <option value="${t.techId}" ${maintenance !=null && maintenance.techId==t.techId
                                    ? 'selected' : '' }>${t.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="event_type" class="form-label">Event Type</label>
                        <select class="form-select" id="event_type" name="event_type" required>
                            <option value="CALIBRATION" ${maintenance !=null && maintenance.eventType=='CALIBRATION'
                                ? 'selected' : '' }>Calibration</option>
                            <option value="REPAIR" ${maintenance !=null && maintenance.eventType=='REPAIR' ? 'selected'
                                : '' }>Repair</option>
                            <option value="REPLACEMENT" ${maintenance !=null && maintenance.eventType=='REPLACEMENT'
                                ? 'selected' : '' }>Replacement</option>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="event_date" class="form-label">Event Date</label>
                        <input type="datetime-local" class="form-control" id="event_date" name="event_date" required
                            value="${maintenance != null ? maintenance.eventDate.toString().substring(0, 16) : ''}">
                    </div>
                </div>
                <div class="mb-3">
                    <label for="notes" class="form-label">Notes</label>
                    <textarea class="form-control" id="notes" name="notes"
                        rows="3">${maintenance != null ? maintenance.notes : ''}</textarea>
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-circle"></i> ${maintenance != null ?
                    'Update' : 'Create'}</button>
                <a href="${pageContext.request.contextPath}/maintenance" class="btn btn-secondary">Cancel</a>
            </form>
        </div>

        <jsp:include page="../includes/footer.jsp" />