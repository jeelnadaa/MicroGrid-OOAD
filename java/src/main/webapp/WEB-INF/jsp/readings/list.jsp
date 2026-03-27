<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <jsp:include page="../includes/header.jsp">
                <jsp:param name="pageTitle" value="Readings" />
            </jsp:include>

            <div class="container-fluid mt-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h2><i class="bi bi-graph-up"></i> Readings</h2>
                    <div>
                        <a href="${pageContext.request.contextPath}/export/readings/csv"
                            class="btn btn-outline-success btn-sm"><i class="bi bi-download"></i> Export CSV</a>
                        <a href="${pageContext.request.contextPath}/readings/create" class="btn btn-primary"><i
                                class="bi bi-plus-circle"></i> Add Reading</a>
                    </div>
                </div>

                <form method="get" class="mb-3">
                    <div class="row g-2">
                        <div class="col-md-4">
                            <select class="form-select" name="sensor">
                                <option value="">All Sensors</option>
                                <c:forEach var="s" items="${sensors}">
                                    <option value="${s.sensorId}" ${sensorFilter==String.valueOf(s.sensorId)
                                        ? 'selected' : '' }>${s.model}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i>
                                Filter</button>
                        </div>
                    </div>
                </form>

                <div class="table-responsive">
                    <table class="table table-hover table-sm">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Sensor</th>
                                <th>Type</th>
                                <th>Location</th>
                                <th>Value</th>
                                <th>Timestamp</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${readings}">
                                <tr>
                                    <td>${r.readingId}</td>
                                    <td>${r.sensorModel}</td>
                                    <td>${r.sensorTypeName}</td>
                                    <td>${r.locationName}</td>
                                    <td>
                                        <fmt:formatNumber value="${r.readingValue}" maxFractionDigits="4" />
                                    </td>
                                    <td>${r.readingTimestamp}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/readings/edit?id=${r.readingId}"
                                            class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                                        <form method="post" action="${pageContext.request.contextPath}/readings/delete"
                                            class="d-inline" onsubmit="return confirm('Delete this reading?')">
                                            <input type="hidden" name="id" value="${r.readingId}">
                                            <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty readings}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted">No readings found</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <jsp:include page="../includes/footer.jsp" />