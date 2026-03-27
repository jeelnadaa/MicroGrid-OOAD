<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="${reading != null ? 'Edit' : 'Create'} Reading" />
        </jsp:include>

        <div class="container mt-4">
            <h2><i class="bi bi-graph-up"></i> ${reading != null ? 'Edit' : 'Record'} Reading</h2>
            <hr>

            <form method="post"
                action="${pageContext.request.contextPath}/readings/${reading != null ? 'edit' : 'create'}">
                <c:if test="${reading != null}">
                    <input type="hidden" name="id" value="${reading.readingId}">
                </c:if>

                <div class="mb-3">
                    <label for="sensor_id" class="form-label">Sensor</label>
                    <select class="form-select" id="sensor_id" name="sensor_id" required>
                        <option value="">Select Sensor...</option>
                        <c:forEach var="s" items="${sensors}">
                            <option value="${s.sensorId}" ${reading !=null && reading.sensorId==s.sensorId ? 'selected'
                                : '' }>${s.model} (${s.sensorTypeName})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="reading_value" class="form-label">Reading Value</label>
                    <input type="number" step="0.0001" class="form-control" id="reading_value" name="reading_value"
                        required value="${reading != null ? reading.readingValue : ''}">
                </div>
                <div class="mb-3">
                    <label for="reading_timestamp" class="form-label">Timestamp</label>
                    <input type="datetime-local" class="form-control" id="reading_timestamp" name="reading_timestamp"
                        required value="${reading != null ? reading.readingTimestamp.toString().substring(0, 16) : ''}">
                </div>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-circle"></i> ${reading != null ?
                    'Update' : 'Record'}</button>
                <a href="${pageContext.request.contextPath}/readings" class="btn btn-secondary">Cancel</a>
            </form>
        </div>

        <jsp:include page="../includes/footer.jsp" />