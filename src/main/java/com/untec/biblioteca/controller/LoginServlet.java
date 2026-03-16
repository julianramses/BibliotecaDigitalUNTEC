package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.UsuarioDAO;
import com.untec.biblioteca.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * CONTROLLER: LoginServlet
 * Gestiona el flujo de autenticación del sistema.
 *
 * GET  /login  → Muestra el formulario de inicio de sesión.
 * POST /login  → Procesa las credenciales y crea la sesión HTTP.
 *
 * Forma parte de la capa CONTROLLER del patrón MVC.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        // Inicializar el DAO al arrancar el servlet
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * GET /login → muestra la vista de login.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Si ya hay sesión activa, redirigir al dashboard
        HttpSession sesion = req.getSession(false);
        if (sesion != null && sesion.getAttribute("usuario") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        // Mostrar la vista de login
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    /**
     * POST /login → procesa el formulario de autenticación.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Obtener datos del formulario
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        // Validación básica de campos vacíos
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Por favor ingrese email y contraseña.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // Intentar autenticar con la base de datos
        Usuario usuario = usuarioDAO.autenticar(email.trim().toLowerCase(), password);

        if (usuario != null) {
            // Credenciales válidas → crear sesión HTTP
            HttpSession sesion = req.getSession(true);
            sesion.setAttribute("usuario", usuario);
            sesion.setAttribute("nombreUsuario", usuario.getNombre());
            sesion.setAttribute("rolUsuario", usuario.getRol());
            sesion.setMaxInactiveInterval(30 * 60); // 30 minutos

            System.out.println("[LoginServlet] Usuario autenticado: " + usuario.getEmail());
            resp.sendRedirect(req.getContextPath() + "/dashboard");

        } else {
            // Credenciales inválidas → volver al login con mensaje de error
            req.setAttribute("error", "Email o contraseña incorrectos. Intente nuevamente.");
            req.setAttribute("emailIngresado", email); // Pre-llenar el campo email
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
