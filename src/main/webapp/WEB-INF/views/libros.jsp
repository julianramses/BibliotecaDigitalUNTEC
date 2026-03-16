<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Libros – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">

    <%@ include file="nav-include.jsp" %>

    <div class="main-content">
        <!-- Top Bar -->
        <div class="topbar">
            <span class="topbar-title">📖 Gestión de Libros</span>
            <div class="topbar-user">
                <span class="user-badge"><c:out value="${sessionScope.rolUsuario}"/></span>
                <span>👤 <c:out value="${sessionScope.nombreUsuario}"/></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">⬅ Salir</a>
            </div>
        </div>

        <div class="page-body">

            <!-- Mensajes de operación (JSTL c:if + c:choose) -->
            <c:if test="${param.mensaje eq 'agregado'}">
                <div class="alert alert-success">✅ Libro agregado correctamente al catálogo.</div>
            </c:if>
            <c:if test="${param.mensaje eq 'actualizado'}">
                <div class="alert alert-success">✅ Libro actualizado correctamente.</div>
            </c:if>
            <c:if test="${param.mensaje eq 'eliminado'}">
                <div class="alert alert-info">🗑️ Libro eliminado del catálogo.</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">⚠️ <c:out value="${error}"/></div>
            </c:if>

            <!-- Encabezado de página -->
            <div class="page-header">
                <h1>📚 Catálogo de Libros
                    <small style="font-size:0.75rem;color:#6c757d;font-weight:400;">
                        — <c:out value="${totalLibros}"/> libro(s) registrados
                    </small>
                </h1>
                <%-- Botón "Agregar" solo visible para administradores (JSTL c:if) --%>
                <c:if test="${sessionScope.rolUsuario eq 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/libros?action=agregar"
                       class="btn btn-primary">
                        ➕ Agregar Libro
                    </a>
                </c:if>
            </div>

            <!-- Tabla de libros -->
            <div class="card">
                <div class="table-wrapper">
                    <c:choose>
                        <c:when test="${not empty libros}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Título</th>
                                        <th>Autor</th>
                                        <th>ISBN</th>
                                        <th>Año</th>
                                        <th>Disponibilidad</th>
                                        <%-- Columna acciones solo para admins --%>
                                        <c:if test="${sessionScope.rolUsuario eq 'ADMIN'}">
                                            <th>Acciones</th>
                                        </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%-- c:forEach para iterar la lista de libros --%>
                                    <c:forEach var="libro" items="${libros}">
                                        <tr>
                                            <td><c:out value="${libro.id}"/></td>
                                            <td>
                                                <strong><c:out value="${libro.titulo}"/></strong>
                                                <c:if test="${not empty libro.descripcion}">
                                                    <br><small style="color:#6c757d;font-size:0.78rem;">
                                                        <c:out value="${libro.descripcion}"/>
                                                    </small>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${libro.autor}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty libro.isbn}">
                                                        <code style="font-size:0.78rem;"><c:out value="${libro.isbn}"/></code>
                                                    </c:when>
                                                    <c:otherwise><span style="color:#aaa">—</span></c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><c:out value="${libro.anio > 0 ? libro.anio : '—'}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${libro.disponible}">
                                                        <span class="badge badge-success">✅ Disponible</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-danger">🔒 Prestado</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <c:if test="${sessionScope.rolUsuario eq 'ADMIN'}">
                                                <td style="white-space:nowrap;">
                                                    <a href="${pageContext.request.contextPath}/libros?action=editar&id=${libro.id}"
                                                       class="btn btn-secondary btn-sm">✏️ Editar</a>
                                                    <a href="${pageContext.request.contextPath}/libros?action=eliminar&id=${libro.id}"
                                                       class="btn btn-danger btn-sm"
                                                       onclick="return confirm('¿Eliminar el libro «${libro.titulo}»? Esta acción no se puede deshacer.')">
                                                        🗑️ Eliminar
                                                    </a>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <div class="empty-icon">📭</div>
                                <p>No hay libros registrados en el catálogo.</p>
                                <c:if test="${sessionScope.rolUsuario eq 'ADMIN'}">
                                    <a href="${pageContext.request.contextPath}/libros?action=agregar"
                                       class="btn btn-primary" style="margin-top:12px;">
                                        ➕ Agregar el primer libro
                                    </a>
                                </c:if>
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
