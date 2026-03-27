<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Sensor Types" />
        </jsp:include>

        <div class="container-fluid mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2><i class="bi bi-tags"></i> Sensor Types</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/export/sensor-types/csv"
                        class="btn btn-outline-success btn-sm"><i class="bi bi-download"></i> Export CSV</a>
                    <a href="${pageContext.request.contextPath}/sensor-types/create" class="btn btn-primary"><i
                            class="bi bi-plus-circle"></i> Add Sensor Type</a>
                </div>
            </div>

            <form method="get" class="mb-3">
                <div class="input-group">
                    <input type="text" class="form-control" name="search" value="${search}"
                        placeholder="Search sensor types...">
                    <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i></button>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Created</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="st" items="${sensorTypes}">
                            <tr>
                                <td>${st.typeId}</td>
                                <td>${st.name}</td>
                                <td>${st.description}</td>
                                <td>${st.createdAt}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/sensor-types/edit?id=${st.typeId}"
                                        class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                                    <form method="post" action="${pageContext.request.contextPath}/sensor-types/delete"
                                        class="d-inline" onsubmit="return confirm('Delete this sensor type?')">
                                        <input type="hidden" name="id" value="${st.typeId}">
                                        <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sensorTypes}">
                            <tr>
                                <td colspan="5" class="text-center text-muted">No sensor types found</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />