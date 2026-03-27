<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <%-- Header fragment: included by all pages --%>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>${param.pageTitle != null ? param.pageTitle : 'Microclimate Sensor Grid'}</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link rel="stylesheet"
                        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
                </head>

                <body>
                    <%-- Navigation --%>
                        <c:if test="${sessionScope.user != null}">
                            <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                                <div class="container-fluid">
                                    <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                                        <i class="bi bi-cloud-sun"></i> Microclimate Sensor Grid
                                    </a>
                                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                        data-bs-target="#navbarNav">
                                        <span class="navbar-toggler-icon"></span>
                                    </button>
                                    <div class="collapse navbar-collapse" id="navbarNav">
                                        <ul class="navbar-nav me-auto">
                                            <li class="nav-item">
                                                <a class="nav-link" href="${pageContext.request.contextPath}/">
                                                    <i class="bi bi-house"></i> Dashboard
                                                </a>
                                            </li>
                                            <li class="nav-item dropdown">
                                                <a class="nav-link dropdown-toggle" href="#" id="sensorDropdown"
                                                    role="button" data-bs-toggle="dropdown">
                                                    <i class="bi bi-cpu"></i> Sensors
                                                </a>
                                                <ul class="dropdown-menu">
                                                    <li><a class="dropdown-item"
                                                            href="${pageContext.request.contextPath}/sensor-types">Sensor
                                                            Types</a></li>
                                                    <li><a class="dropdown-item"
                                                            href="${pageContext.request.contextPath}/sensors">All
                                                            Sensors</a></li>
                                                    <li><a class="dropdown-item"
                                                            href="${pageContext.request.contextPath}/readings">Readings</a>
                                                    </li>
                                                </ul>
                                            </li>
                                            <li class="nav-item">
                                                <a class="nav-link" href="${pageContext.request.contextPath}/locations">
                                                    <i class="bi bi-geo-alt"></i> Locations
                                                </a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="nav-link"
                                                    href="${pageContext.request.contextPath}/technicians">
                                                    <i class="bi bi-person-badge"></i> Technicians
                                                </a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="nav-link"
                                                    href="${pageContext.request.contextPath}/maintenance">
                                                    <i class="bi bi-tools"></i> Maintenance
                                                </a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="nav-link" href="${pageContext.request.contextPath}/reports">
                                                    <i class="bi bi-bar-chart"></i> Reports
                                                </a>
                                            </li>
                                        </ul>
                                        <ul class="navbar-nav">
                                            <li class="nav-item dropdown">
                                                <a class="nav-link dropdown-toggle" href="#" id="userDropdown"
                                                    role="button" data-bs-toggle="dropdown">
                                                    <i class="bi bi-person-circle"></i> ${sessionScope.username}
                                                </a>
                                                <ul class="dropdown-menu dropdown-menu-end">
                                                    <li><span class="dropdown-item-text"><strong>${sessionScope.user.fullName
                                                                != null ? sessionScope.user.fullName :
                                                                sessionScope.username}</strong></span></li>
                                                    <li>
                                                        <hr class="dropdown-divider">
                                                    </li>
                                                    <li><a class="dropdown-item"
                                                            href="${pageContext.request.contextPath}/logout"><i
                                                                class="bi bi-box-arrow-right"></i> Logout</a></li>
                                                </ul>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </nav>
                        </c:if>

                        <%-- Flash Messages --%>
                            <c:if test="${not empty sessionScope._flash_messages}">
                                <div class="container mt-3">
                                    <c:forEach var="msg" items="${sessionScope._flash_messages}">
                                        <div class="alert alert-${msg.category} alert-dismissible fade show"
                                            role="alert">
                                            ${msg.message}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                        </div>
                                    </c:forEach>
                                </div>
                                <c:remove var="_flash_messages" scope="session" />
                            </c:if>