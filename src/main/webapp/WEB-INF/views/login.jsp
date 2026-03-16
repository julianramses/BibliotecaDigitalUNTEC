<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-page">
    <div class="login-box">

        <!-- Logo / Encabezado -->
        <div class="login-logo">
            <span class="book-icon">📚</span>
            <h1>Biblioteca Digital</h1>
            <p>Universidad UNTEC</p>
        </div>

        <!-- Alerta de error (JSTL c:if) -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ⚠️ <c:out value="${error}"/>
            </div>
        </c:if>

        <!-- Alerta de cierre de sesión exitoso -->
        <c:if test="${param.logout eq 'true'}">
            <div class="alert alert-success">
                ✅ Sesión cerrada correctamente.
            </div>
        </c:if>

        <!-- Formulario de Login -->
        <form action="${pageContext.request.contextPath}/login" method="post">

            <div class="form-group">
                <label for="email">📧 Correo electrónico</label>
                <input type="email"
                       id="email"
                       name="email"
                       class="form-control"
                       placeholder="usuario@untec.edu"
                       value="<c:out value='${emailIngresado}'/>"
                       required
                       autofocus>
            </div>

            <div class="form-group">
                <label for="password">🔒 Contraseña</label>
                <input type="password"
                       id="password"
                       name="password"
                       class="form-control"
                       placeholder="Ingrese su contraseña"
                       required>
            </div>

            <button type="submit" class="btn btn-primary" style="width:100%; justify-content:center; padding:12px;">
                Ingresar al sistema
            </button>
        </form>

        <!-- Credenciales de demo (uso educativo) -->
        <div class="login-hint">
            <strong>🎓 Credenciales de demostración:</strong>
            Admin: admin@untec.edu / admin123<br>
            Estudiante: juan@untec.edu / juan123
        </div>

    </div>
</div>

</body>
</html>
