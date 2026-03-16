package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.LibroDAO;
import com.untec.biblioteca.dao.PrestamoDAO;
import com.untec.biblioteca.dao.UsuarioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * CONTROLLER: DashboardServlet
 * Muestra el panel principal del sistema con estadísticas generales.
 *
 * GET /dashboard → carga estadísticas y muestra la vista dashboard.jsp.
 *
 * Forma parte de la capa CONTROLLER del patrón MVC.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private LibroDAO    libroDAO;
    private UsuarioDAO  usuarioDAO;
    private PrestamoDAO prestamoDAO;

    @Override
    public void init() {
        this.libroDAO    = new LibroDAO();
        this.usuarioDAO  = new UsuarioDAO();
        this.prestamoDAO = new PrestamoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verificar sesión activa (seguridad básica)
        if (!verificarSesion(req, resp)) return;

        // Pasar estadísticas a la vista
        req.setAttribute("totalLibros",           libroDAO.listarTodos().size());
        req.setAttribute("librosDisponibles",     libroDAO.listarDisponibles().size());
        req.setAttribute("totalUsuarios",         usuarioDAO.listarTodos().size());
        req.setAttribute("prestamosActivos",      prestamoDAO.listarActivos().size());
        req.setAttribute("ultimosPrestamos",      prestamoDAO.listarActivos());

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }

    /**
     * Verifica que exista una sesión válida. Si no, redirige al login.
     */
    private boolean verificarSesion(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
