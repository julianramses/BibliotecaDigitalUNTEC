<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Préstamos – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">

    <%@ include file="nav-include.jsp" %>

    <div class="main-content">
        <div class="topbar">
            <span class="topbar-title">🔄 Gestión de Préstamos</span>
            <div class="topbar-user">
                <span class="user-badge"><c:out value="${sessionScope.rolUsuario}"/></span>
                <span>👤 <c:out value="${sessionScope.nombreUsuario}"/></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">⬅ Salir</a>
            </div>
        </div>

        <div class="page-body">

            <%-- Alertas de operaciones (JSTL c:choose / c:when) --%>
            <c:choose>
                <c:when test="${param.mensaje eq 'prestamo-ok'}">
                    <div class="alert alert-success">✅ Préstamo registrado correctamente.</div>
                </c:when>
                <c:when test="${param.mensaje eq 'devolucion-ok'}">
                    <div class="alert alert-success">✅ Devolución registrada. El libro ya está disponible.</div>
                </c:when>
                <c:when test="${param.error eq 'libro-invalido'}">
                    <div class="alert alert-danger">⚠️ Seleccione un libro válido para el préstamo.</div>
                </c:when>
                <c:when test="${param.error eq 'prestamo-fallido'}">
                    <div class="alert alert-danger">⚠️ No se pudo registrar el préstamo. Intente nuevamente.</div>
                </c:when>
            </c:choose>

            <!-- Formulario de nuevo préstamo -->
            <div class="card" style="margin-bottom:24px;">
                <div class="card-header">
                    <h2>📤 Nuevo Préstamo</h2>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${not empty librosDisponibles}">
                            <form action="${pageContext.request.contextPath}/prestamos?action=prestar"
                                  method="post"
                                  style="display:flex;gap:12px;align-items:flex-end;flex-wrap:wrap;">

                                <div class="form-group" style="flex:1;min-width:220px;margin-bottom:0;">
                                    <label for="libroId">Seleccionar libro disponible</label>
                                    <select id="libroId" name="libroId" class="form-control" required>
                                        <option value="">— Elegir libro —</option>
                                        <%-- c:forEach para listar libros disponibles --%>
                                        <c:forEach var="libro" items="${librosDisponibles}">
                                            <option value="${libro.id}">
                                                <c:out value="${libro.titulo}"/> — <c:out value="${libro.autor}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div style="padding-bottom:0;">
                                    <button type="submit" class="btn btn-success">
                                        📤 Solicitar Préstamo
                                    </button>
                                </div>

                            </form>
                            <p style="font-size:0.78rem;color:#6c757d;margin-top:10px;">
                                El préstamo se registrará a tu cuenta: <strong><c:out value="${sessionScope.nombreUsuario}"/></strong>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state" style="padding:20px;">
                                <div class="empty-icon">📭</div>
                                <p>No hay libros disponibles para préstamo en este momento.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Tabla de todos los préstamos -->
            <div class="card">
                <div class="card-header">
                    <h2>📋 Historial de Préstamos</h2>
                </div>
                <div class="table-wrapper">
                    <c:choose>
                        <c:when test="${not empty prestamos}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Libro</th>
                                        <th>Usuario</th>
                                        <th>Fecha Préstamo</th>
                                        <th>Fecha Devolución</th>
                                        <th>Estado</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="prestamo" items="${prestamos}">
                                        <tr>
                                            <td><c:out value="${prestamo.id}"/></td>
                                            <td>📖 <c:out value="${prestamo.tituloLibro}"/></td>
                                            <td>👤 <c:out value="${prestamo.nombreUsuario}"/></td>
                                            <td><c:out value="${prestamo.fechaPrestamoFormateada}"/></td>
                                            <td><c:out value="${prestamo.fechaDevolucionFormateada}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${prestamo.activo}">
                                                        <span class="badge badge-warning">🔄 En préstamo</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-success">✅ Devuelto</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <%-- Botón devolver solo si el préstamo está activo --%>
                                                <c:if test="${prestamo.activo}">
                                                    <form action="${pageContext.request.contextPath}/prestamos?action=devolver"
                                                          method="post" style="display:inline;">
                                                        <input type="hidden" name="id" value="${prestamo.id}">
                                                        <button type="submit" class="btn btn-primary btn-sm"
                                                                onclick="return confirm('¿Confirmar devolución de «${prestamo.tituloLibro}»?')">
                                                            📥 Devolver
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${not prestamo.activo}">
                                                    <span style="color:#aaa;font-size:0.8rem;">—</span>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <div class="empty-icon">📭</div>
                                <p>No hay préstamos registrados aún.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div><!-- /page-body -->
        <footer>Sistema de Biblioteca Digital UNTEC &mdash; Módulo 5 Java EE &mdash; Alkemy</footer>
    </div>

</div>
</body>
</html>
