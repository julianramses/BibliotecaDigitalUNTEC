<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agregar Libro – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">

    <%@ include file="nav-include.jsp" %>

    <div class="main-content">
        <div class="topbar">
            <span class="topbar-title">➕ Agregar Nuevo Libro</span>
            <div class="topbar-user">
                <span class="user-badge"><c:out value="${sessionScope.rolUsuario}"/></span>
                <span>👤 <c:out value="${sessionScope.nombreUsuario}"/></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">⬅ Salir</a>
            </div>
        </div>

        <div class="page-body">

            <c:if test="${not empty error}">
                <div class="alert alert-danger">⚠️ <c:out value="${error}"/></div>
            </c:if>

            <div class="page-header">
                <h1>➕ Nuevo Libro</h1>
                <a href="${pageContext.request.contextPath}/libros" class="btn btn-secondary">
                    ← Volver al catálogo
                </a>
            </div>

            <div class="card" style="max-width:640px;">
                <div class="card-header">
                    <h2>📝 Datos del Libro</h2>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/libros?action=agregar" method="post">

                        <div class="form-group">
                            <label for="titulo">Título *</label>
                            <input type="text" id="titulo" name="titulo" class="form-control"
                                   placeholder="Ej: Clean Code" required maxlength="200">
                        </div>

                        <div class="form-group">
                            <label for="autor">Autor *</label>
                            <input type="text" id="autor" name="autor" class="form-control"
                                   placeholder="Ej: Robert C. Martin" required maxlength="150">
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="isbn">ISBN</label>
                                <input type="text" id="isbn" name="isbn" class="form-control"
                                       placeholder="Ej: 9780132350884" maxlength="20">
                            </div>
                            <div class="form-group">
                                <label for="anio">Año de Publicación</label>
                                <input type="number" id="anio" name="anio" class="form-control"
                                       placeholder="Ej: 2008" min="1800" max="2030">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="descripcion">Descripción</label>
                            <textarea id="descripcion" name="descripcion" class="form-control"
                                      rows="3" placeholder="Breve descripción del contenido..."
                                      maxlength="500"></textarea>
                        </div>

                        <div style="display:flex;gap:10px;margin-top:8px;">
                            <button type="submit" class="btn btn-primary">
                                💾 Guardar Libro
                            </button>
                            <a href="${pageContext.request.contextPath}/libros" class="btn btn-secondary">
                                Cancelar
                            </a>
                        </div>

                    </form>
                </div>
            </div>

        </div>
        <footer>Sistema de Biblioteca Digital UNTEC &mdash; Módulo 5 Java EE &mdash; Alkemy</footer>
    </div>

</div>
</body>
</html>
