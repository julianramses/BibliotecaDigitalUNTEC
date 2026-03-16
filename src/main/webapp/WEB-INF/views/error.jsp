<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error – Biblioteca Digital UNTEC</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div style="min-height:100vh;display:flex;align-items:center;justify-content:center;
            background:linear-gradient(135deg,#1a4fa0,#0d3070);padding:20px;">
    <div style="background:#fff;border-radius:16px;padding:48px;text-align:center;max-width:420px;
                box-shadow:0 4px 24px rgba(0,0,0,.15);">
        <div style="font-size:4rem;margin-bottom:16px;">😕</div>
        <h1 style="color:#1a4fa0;font-size:1.4rem;margin-bottom:8px;">Página no encontrada</h1>
        <p style="color:#6c757d;margin-bottom:24px;font-size:0.9rem;">
            La página que intentas acceder no existe o ocurrió un error en el servidor.
        </p>
        <a href="${pageContext.request.contextPath}/dashboard"
           style="display:inline-block;background:#1a4fa0;color:#fff;padding:10px 24px;
                  border-radius:8px;text-decoration:none;font-weight:600;">
            ← Volver al inicio
        </a>
    </div>
</div>
</body>
</html>
