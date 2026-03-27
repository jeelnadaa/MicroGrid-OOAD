<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <jsp:include page="includes/header.jsp">
                <jsp:param name="pageTitle" value="Dashboard - Microclimate Sensor Grid" />
            </jsp:include>

            <div class="container-fluid mt-4">
                <h2><i class="bi bi-speedometer2"></i> Dashboard</h2>
                <hr>

                <%-- Statistics Cards --%>
                    <div class="row g-4 mb-4">
                        <div class="col-md-4 col-lg-2">
                            <div class="card text-white bg-primary h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-cpu" style="font-size: 2rem;"></i>
                                    <h3>${totalSensors}</h3>
                                    <p class="mb-0">Total Sensors</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2">
                            <div class="card text-white bg-success h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-check-circle" style="font-size: 2rem;"></i>
                                    <h3>${activeSensors}</h3>
                                    <p class="mb-0">Active Sensors</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2">
                            <div class="card text-white bg-info h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-graph-up" style="font-size: 2rem;"></i>
                                    <h3>${totalReadings}</h3>
                                    <p class="mb-0">Readings</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2">
                            <div class="card text-white bg-warning h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-geo-alt" style="font-size: 2rem;"></i>
                                    <h3>${totalLocations}</h3>
                                    <p class="mb-0">Locations</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2">
                            <div class="card text-white bg-secondary h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-person-badge" style="font-size: 2rem;"></i>
                                    <h3>${totalTechnicians}</h3>
                                    <p class="mb-0">Technicians</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 col-lg-2">
                            <div class="card text-white bg-danger h-100">
                                <div class="card-body text-center">
                                    <i class="bi bi-tools" style="font-size: 2rem;"></i>
                                    <h3>${totalMaintenance}</h3>
                                    <p class="mb-0">Maintenance</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4">
                        <%-- Recent Readings --%>
                            <div class="col-lg-8">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0"><i class="bi bi-clock-history"></i> Recent Readings</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="table-responsive">
                                            <table class="table table-hover table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>Sensor</th>
                                                        <th>Type</th>
                                                        <th>Location</th>
                                                        <th>Value</th>
                                                        <th>Time</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="r" items="${recentReadings}">
                                                        <tr>
                                                            <td>${r.sensorModel}</td>
                                                            <td>${r.sensorTypeName}</td>
                                                            <td>${r.locationName}</td>
                                                            <td>
                                                                <fmt:formatNumber value="${r.readingValue}"
                                                                    maxFractionDigits="4" />
                                                            </td>
                                                            <td>${r.readingTimestamp}</td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty recentReadings}">
                                                        <tr>
                                                            <td colspan="5" class="text-center text-muted">No readings
                                                                yet</td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%-- Maintenance Stats --%>
                                <div class="col-lg-4">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5 class="mb-0"><i class="bi bi-tools"></i> Maintenance by Type</h5>
                                        </div>
                                        <div class="card-body">
                                            <c:forEach var="stat" items="${maintenanceStats}">
                                                <div class="d-flex justify-content-between mb-2">
                                                    <span class="badge bg-secondary">${stat[0]}</span>
                                                    <strong>${stat[1]}</strong>
                                                </div>
                                            </c:forEach>
                                            <c:if test="${empty maintenanceStats}">
                                                <p class="text-muted text-center">No maintenance events</p>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="card mt-4">
                                        <div class="card-header">
                                            <h5 class="mb-0"><i class="bi bi-bar-chart"></i> Avg Readings by Type</h5>
                                        </div>
                                        <div class="card-body">
                                            <c:forEach var="avg" items="${avgReadings}">
                                                <div class="d-flex justify-content-between mb-2">
                                                    <span>${avg[0]}</span>
                                                    <strong>
                                                        <fmt:formatNumber value="${avg[1]}" maxFractionDigits="2" />
                                                    </strong>
                                                </div>
                                            </c:forEach>
                                            <c:if test="${empty avgReadings}">
                                                <p class="text-muted text-center">No readings data</p>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                    </div>
            </div>

            <jsp:include page="includes/footer.jsp" />