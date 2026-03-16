<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 
    index.jsp - Página de entrada
    Si hay sesión activa → va al dashboard
    Si no → va al login
--%>
<%
    if (session.getAttribute("usuario") != null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
