<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <jsp:include page="../includes/header.jsp">
            <jsp:param name="pageTitle" value="Technicians" />
        </jsp:include>

        <div class="container-fluid mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2><i class="bi bi-person-badge"></i> Technicians</h2>
                <div>
                    <a href="${pageContext.request.contextPath}/export/technicians/csv"
                        class="btn btn-outline-success btn-sm"><i class="bi bi-download"></i> Export CSV</a>
                    <a href="${pageContext.request.contextPath}/technicians/create" class="btn btn-primary"><i
                            class="bi bi-plus-circle"></i> Add Technician</a>
                </div>
            </div>

            <form method="get" class="mb-3">
                <div class="input-group">
                    <input type="text" class="form-control" name="search" value="${search}"
                        placeholder="Search technicians...">
                    <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i></button>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Contact</th>
                            <th>Specialization</th>
                            <th>Maintenance Count</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ts" items="${techStats}">
                            <tr>
                                <td>${ts[0].techId}</td>
                                <td>${ts[0].name}</td>
                                <td>${ts[0].contactNo}</td>
                                <td>${ts[0].specialization}</td>
                                <td><span class="badge bg-info">${ts[1]}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/technicians/edit?id=${ts[0].techId}"
                                        class="btn btn-sm btn-warning"><i class="bi bi-pencil"></i></a>
                                    <form method="post" action="${pageContext.request.contextPath}/technicians/delete"
                                        class="d-inline" onsubmit="return confirm('Delete this technician?')">
                                        <input type="hidden" name="id" value="${ts[0].techId}">
                                        <button class="btn btn-sm btn-danger"><i class="bi bi-trash"></i></button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty techStats}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">No technicians found</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <jsp:include page="../includes/footer.jsp" />