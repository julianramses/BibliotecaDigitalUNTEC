<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Libro – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="layout">

    <%@ include file="nav-include.jsp" %>

    <div class="main-content">
        <div class="topbar">
            <span class="topbar-title">✏️ Editar Libro</span>
            <div class="topbar-user">
                <span class="user-badge"><c:out value="${sessionScope.rolUsuario}"/></span>
                <span>👤 <c:out value="${sessionScope.nombreUsuario}"/></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">⬅ Salir</a>
            </div>
        </div>

        <div class="page-body">

            <div class="page-header">
                <h1>✏️ Editar Libro</h1>
                <a href="${pageContext.request.contextPath}/libros" class="btn btn-secondary">
                    ← Volver al catálogo
                </a>
            </div>

            <div class="card" style="max-width:640px;">
                <div class="card-header">
                    <h2>📝 Modificar datos de: <em><c:out value="${libro.titulo}"/></em></h2>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/libros?action=editar" method="post">

                        <%-- Campo oculto con el ID del libro --%>
                        <input type="hidden" name="id" value="${libro.id}">

                        <div class="form-group">
                            <label for="titulo">Título *</label>
                            <input type="text" id="titulo" name="titulo" class="form-control"
                                   value="<c:out value='${libro.titulo}'/>" required maxlength="200">
                        </div>

                        <div class="form-group">
                            <label for="autor">Autor *</label>
                            <input type="text" id="autor" name="autor" class="form-control"
                                   value="<c:out value='${libro.autor}'/>" required maxlength="150">
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="isbn">ISBN</label>
                                <input type="text" id="isbn" name="isbn" class="form-control"
                                       value="<c:out value='${libro.isbn}'/>" maxlength="20">
                            </div>
                            <div class="form-group">
                                <label for="anio">Año de Publicación</label>
                                <input type="number" id="anio" name="anio" class="form-control"
                                       value="<c:out value='${libro.anio}'/>" min="1800" max="2030">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="descripcion">Descripción</label>
                            <textarea id="descripcion" name="descripcion" class="form-control"
                                      rows="3" maxlength="500"><c:out value="${libro.descripcion}"/></textarea>
                        </div>

                        <div style="display:flex;gap:10px;margin-top:8px;">
                            <button type="submit" class="btn btn-primary">
                                💾 Guardar Cambios
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
