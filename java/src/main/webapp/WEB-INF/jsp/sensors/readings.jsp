<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <jsp:include page="../includes/header.jsp">
                <jsp:param name="pageTitle" value="Sensor Readings - ${sensor.model}" />
            </jsp:include>

            <div class="container-fluid mt-4">
                <h2><i class="bi bi-graph-up"></i> Readings for Sensor: ${sensor.model}</h2>
                <p class="text-muted">Type: ${sensor.sensorTypeName} | Location: ${sensor.locationName} | Status:
                    <c:choose>
                        <c:when test="${sensor.status == 'ACTIVE'}"><span class="badge bg-success">Active</span>
                        </c:when>
                        <c:when test="${sensor.status == 'INACTIVE'}"><span class="badge bg-secondary">Inactive</span>
                        </c:when>
                        <c:otherwise><span class="badge bg-warning text-dark">Maintenance</span></c:otherwise>
                    </c:choose>
                </p>
                <hr>

                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Model</th>
                                <th>Type</th>
                                <th>Value</th>
                                <th>Timestamp</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${readings}">
                                <tr>
                                    <td>${r.readingId}</td>
                                    <td>${r.sensorModel}</td>
                                    <td>${r.sensorTypeName}</td>
                                    <td>
                                        <fmt:formatNumber value="${r.readingValue}" maxFractionDigits="4" />
                                    </td>
                                    <td>${r.readingTimestamp}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty readings}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted">No readings found</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <a href="${pageContext.request.contextPath}/sensors" class="btn btn-secondary"><i
                        class="bi bi-arrow-left"></i> Back to Sensors</a>
            </div>

            <jsp:include page="../includes/footer.jsp" />