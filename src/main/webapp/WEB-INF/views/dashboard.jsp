<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">

    <%@ include file="nav-include.jsp" %>

    <div class="main-content">
        <!-- Top Bar -->
        <div class="topbar">
            <span class="topbar-title">🏠 Panel Principal</span>
            <div class="topbar-user">
                <span class="user-badge">
                    <c:out value="${sessionScope.rolUsuario}"/>
                </span>
                <span>👤 <c:out value="${sessionScope.nombreUsuario}"/></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">⬅ Salir</a>
            </div>
        </div>

        <div class="page-body">

            <!-- Tarjetas de estadísticas -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon">📚</div>
                    <div class="stat-info">
                        <h3><c:out value="${totalLibros}"/></h3>
                        <p>Total de Libros</p>
                    </div>
                </div>
                <div class="stat-card success">
                    <div class="stat-icon">✅</div>
                    <div class="stat-info">
                        <h3><c:out value="${librosDisponibles}"/></h3>
                        <p>Libros Disponibles</p>
                    </div>
                </div>
                <div class="stat-card accent">
                    <div class="stat-icon">🔄</div>
                    <div class="stat-info">
                        <h3><c:out value="${prestamosActivos}"/></h3>
                        <p>Préstamos Activos</p>
                    </div>
                </div>
                <div class="stat-card warning">
                    <div class="stat-icon">👥</div>
                    <div class="stat-info">
                        <h3><c:out value="${totalUsuarios}"/></h3>
                        <p>Usuarios Registrados</p>
                    </div>
                </div>
            </div>

            <!-- Tabla de préstamos activos -->
            <div class="card">
                <div class="card-header">
                    <h2>🔄 Préstamos Activos</h2>
                    <a href="${pageContext.request.contextPath}/prestamos" class="btn btn-primary btn-sm">
                        Ver todos
                    </a>
                </div>
                <div class="table-wrapper">
                    <c:choose>
                        <c:when test="${not empty ultimosPrestamos}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Libro</th>
                                        <th>Usuario</th>
                                        <th>Fecha Préstamo</th>
                                        <th>Estado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="p" items="${ultimosPrestamos}">
                                        <tr>
                                            <td><c:out value="${p.id}"/></td>
                                            <td>📖 <c:out value="${p.tituloLibro}"/></td>
                                            <td>👤 <c:out value="${p.nombreUsuario}"/></td>
                                            <td><c:out value="${p.fechaPrestamoFormateada}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${p.activo}">
                                                        <span class="badge badge-warning">En préstamo</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-success">Devuelto</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <div class="empty-icon">📭</div>
                                <p>No hay préstamos activos en este momento.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div><!-- /page-body -->

        <footer>Sistema de Biblioteca Digital UNTEC &mdash; Módulo 5 Java EE &mdash; Alkemy</footer>
    </div><!-- /main-content -->

</div><!-- /layout -->
</body>
</html>
