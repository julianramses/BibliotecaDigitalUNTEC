<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    nav-include.jsp
    Barra de navegación lateral compartida por todas las vistas del sistema.
    Se incluye con <%@ include file="nav-include.jsp" %> en cada JSP.
--%>
<div class="sidebar">
    <div class="sidebar-logo">
        <h2>📚 Biblioteca<br>Digital</h2>
        <span>UNTEC</span>
    </div>

    <nav>
        <a href="${pageContext.request.contextPath}/dashboard"
           class="nav-link ${pageContext.request.servletPath.contains('dashboard') ? 'active' : ''}">
            <span class="icon">🏠</span> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/libros"
           class="nav-link ${pageContext.request.servletPath.contains('libro') ? 'active' : ''}">
            <span class="icon">📖</span> Libros
        </a>
        <a href="${pageContext.request.contextPath}/prestamos"
           class="nav-link ${pageContext.request.servletPath.contains('prestamo') ? 'active' : ''}">
            <span class="icon">🔄</span> Préstamos
        </a>
    </nav>

    <div class="sidebar-footer">
        Módulo 5 – Java EE<br>Alkemy © 2024
    </div>
</div>
