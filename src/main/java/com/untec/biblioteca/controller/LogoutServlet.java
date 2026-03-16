package com.untec.biblioteca.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * CONTROLLER: LogoutServlet
 * Invalida la sesión del usuario y redirige al login.
 *
 * GET /logout → cierra la sesión y redirige a /login.
 *
 * Forma parte de la capa CONTROLLER del patrón MVC.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Obtener la sesión actual (sin crear una nueva)
        HttpSession sesion = req.getSession(false);

        if (sesion != null) {
            String email = "";
            Object u = sesion.getAttribute("usuario");
            if (u instanceof com.untec.biblioteca.model.Usuario) {
                email = ((com.untec.biblioteca.model.Usuario) u).getEmail();
            }
            sesion.invalidate(); // Destruir la sesión
            System.out.println("[LogoutServlet] Sesión cerrada para: " + email);
        }

        // Redirigir al login con mensaje de cierre exitoso
        resp.sendRedirect(req.getContextPath() + "/login?logout=true");
    }
}
