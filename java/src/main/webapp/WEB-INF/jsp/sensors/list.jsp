<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Sensors" />
        </jsp:include>

        <div class="container-fluid mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2><i class="bi bi-cpu"></i> Sensors</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/export/sensors/csv"
                        class="btn btn-outline-success btn-sm"><i class="bi bi-download"></i> Export CSV</a>
                    <a href="${pageContext.request.contextPath}/sensors/create" class="btn btn-primary"><i
                            class="bi bi-plus-circle"></i> Add Sensor</a>
                </div>
            </div>

            <%-- Filters --%>
                <form method="get" class="mb-3">
                    <div class="row g-2">
                        <div class="col-md-3">
                            <input type="text" class="form-control" name="search" value="${search}"
                                placeholder="Search by model...">
                        </div>
                        <div class="col-md-2">
                            <select class="form-select" name="status">
                                <option value="">All Status</option>
                                <option value="ACTIVE" ${statusFilter=='ACTIVE' ? 'selected' : '' }>Active</option>
                                <option value="INACTIVE" ${statusFilter=='INACTIVE' ? 'selected' : '' }>Inactive
                                </option>
                                <option value="MAINTENANCE" ${statusFilter=='MAINTENANCE' ? 'selected' : '' }>
                                    Maintenance</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <select class="form-select" name="type">
                                <option value="">All Types</option>
                                <c:forEach var="st" items="${sensorTypes}">
                                    <option value="${st.typeId}" ${typeFilter==String.valueOf(st.typeId) ? 'selected'
                                        : '' }>${st.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <select class="form-select" name="location">
                                <option value="">All Locations</option>
                                <c:forEach var="loc" items="${locations}">
                                    <option value="${loc.locationId}" ${locationFilter==String.valueOf(loc.locationId)
                                        ? 'selected' : '' }>${loc.areaName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-outline-primary w-100" type="submit"><i class="bi bi-search"></i>
                                Filter</button>
                        </div>
                    </div>
                </form>

                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Model</th>
                                <th>Type</th>
                                <th>Location</th>
                                <th>Install Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${sensors}">
                                <tr>
                                    <td>${s.sensorId}</td>
                                    <td>${s.model}</td>
                                    <td>${s.sensorTypeName}</td>
                                    <td>${s.locationName}</td>
                                    <td>${s.installDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${s.status == 'ACTIVE'}"><span
                                                    class="badge bg-success">Active</span></c:when>
                                            <c:when test="${s.status == 'INACTIVE'}"><span
                                                    class="badge bg-secondary">Inactive</span></c:when>
                                            <c:when test="${s.status == 'MAINTENANCE'}"><span
                                                    class="badge bg-warning text-dark">Maintenance</span></c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/sensors/readings?id=${s.sensorId}"
                                            class="btn btn-sm btn-info" title="View Readings"><i
                                                class="bi bi-graph-up"></i></a>
                                        <a href="${pageContext.request.contextPath}/sensors/edit?id=${s.sensorId}"
                                            class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                                        <form method="post" action="${pageContext.request.contextPath}/sensors/delete"
                                            class="d-inline" onsubmit="return confirm('Delete this sensor?')">
                                            <input type="hidden" name="id" value="${s.sensorId}">
                                            <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty sensors}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted">No sensors found</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />