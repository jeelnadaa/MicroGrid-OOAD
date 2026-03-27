<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <jsp:include page="../includes/header.jsp">
                <jsp:param name="pageTitle" value="Reports & Analytics" />
            </jsp:include>

            <div class="container-fluid mt-4">
                <h2><i class="bi bi-bar-chart"></i> Reports & Analytics</h2>
                <hr>

                <div class="row g-4">
                    <%-- Sensor Status Distribution --%>
                        <div class="col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">Sensor Status Distribution</h5>
                                </div>
                                <div class="card-body">
                                    <c:forEach var="sd" items="${statusDist}">
                                        <div class="d-flex justify-content-between mb-2">
                                            <span
                                                class="badge bg-${sd[0] == 'ACTIVE' ? 'success' : sd[0] == 'INACTIVE' ? 'secondary' : 'warning'}">${sd[0]}</span>
                                            <strong>${sd[1]}</strong>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <%-- Maintenance Summary --%>
                            <div class="col-md-4">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0">Maintenance Summary</h5>
                                    </div>
                                    <div class="card-body">
                                        <table class="table table-sm">
                                            <thead>
                                                <tr>
                                                    <th>Type</th>
                                                    <th>Count</th>
                                                    <th>Sensors</th>
                                                    <th>Technicians</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="ms" items="${maintenanceSummary}">
                                                    <tr>
                                                        <td>${ms[0]}</td>
                                                        <td>${ms[1]}</td>
                                                        <td>${ms[2]}</td>
                                                        <td>${ms[3]}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <%-- Top Technicians --%>
                                <div class="col-md-4">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5 class="mb-0">Top Technicians</h5>
                                        </div>
                                        <div class="card-body">
                                            <table class="table table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>Name</th>
                                                        <th>Specialization</th>
                                                        <th>Count</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="tt" items="${topTechnicians}">
                                                        <tr>
                                                            <td>${tt[1]}</td>
                                                            <td>${tt[2]}</td>
                                                            <td><span class="badge bg-primary">${tt[3]}</span></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                </div>

                <%-- Average Readings --%>
                    <div class="row g-4 mt-2">
                        <div class="col-md-6">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">Average Readings by Sensor Type</h5>
                                </div>
                                <div class="card-body">
                                    <table class="table table-sm">
                                        <thead>
                                            <tr>
                                                <th>Sensor Type</th>
                                                <th>Avg Value</th>
                                                <th>Readings</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="ar" items="${avgReadings}">
                                                <tr>
                                                    <td>${ar[0]}</td>
                                                    <td>
                                                        <fmt:formatNumber value="${ar[1]}" maxFractionDigits="2" />
                                                    </td>
                                                    <td>${ar[2]}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <%-- Recent Status Changes --%>
                            <div class="col-md-6">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0">Recent Status Changes</h5>
                                    </div>
                                    <div class="card-body">
                                        <table class="table table-sm">
                                            <thead>
                                                <tr>
                                                    <th>Sensor</th>
                                                    <th>Old Status</th>
                                                    <th>New Status</th>
                                                    <th>Changed At</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="sl" items="${statusLogs}">
                                                    <tr>
                                                        <td>${sl.sensorModel}</td>
                                                        <td><span class="badge bg-secondary">${sl.oldStatus}</span></td>
                                                        <td><span class="badge bg-primary">${sl.newStatus}</span></td>
                                                        <td>${sl.changeTimestamp}</td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty statusLogs}">
                                                    <tr>
                                                        <td colspan="4" class="text-muted text-center">No status changes
                                                            recorded</td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                    </div>
            </div>

            <jsp:include page="../includes/footer.jsp" />