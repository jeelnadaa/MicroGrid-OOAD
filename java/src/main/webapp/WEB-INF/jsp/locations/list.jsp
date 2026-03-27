<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Locations" />
        </jsp:include>

        <div class="container-fluid mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2><i class="bi bi-geo-alt"></i> Locations</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/export/locations/csv"
                        class="btn btn-outline-success btn-sm"><i class="bi bi-download"></i> Export CSV</a>
                    <a href="${pageContext.request.contextPath}/locations/create" class="btn btn-primary"><i
                            class="bi bi-plus-circle"></i> Add Location</a>
                </div>
            </div>

            <form method="get" class="mb-3">
                <div class="input-group">
                    <input type="text" class="form-control" name="search" value="${search}"
                        placeholder="Search locations...">
                    <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i></button>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Area Name</th>
                            <th>Latitude</th>
                            <th>Longitude</th>
                            <th>Elevation</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="loc" items="${locations}">
                            <tr>
                                <td>${loc.locationId}</td>
                                <td>${loc.areaName}</td>
                                <td>${loc.latitude}</td>
                                <td>${loc.longitude}</td>
                                <td>${loc.elevation}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/locations/edit?id=${loc.locationId}"
                                        class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                                    <form method="post" action="${pageContext.request.contextPath}/locations/delete"
                                        class="d-inline" onsubmit="return confirm('Delete this location?')">
                                        <input type="hidden" name="id" value="${loc.locationId}">
                                        <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty locations}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">No locations found</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />