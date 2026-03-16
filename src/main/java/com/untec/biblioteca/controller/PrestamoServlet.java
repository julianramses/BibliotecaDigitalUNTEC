package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.LibroDAO;
import com.untec.biblioteca.dao.PrestamoDAO;
import com.untec.biblioteca.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * CONTROLLER: PrestamoServlet
 * Gestiona los préstamos y devoluciones de libros.
 *
 * GET  /prestamos                          → Lista todos los préstamos.
 * POST /prestamos?action=prestar&libroId=X → Realiza un préstamo al usuario en sesión.
 * POST /prestamos?action=devolver&id=X     → Registra la devolución de un préstamo.
 *
 * Forma parte de la capa CONTROLLER del patrón MVC.
 */
@WebServlet("/prestamos")
public class PrestamoServlet extends HttpServlet {

    private PrestamoDAO prestamoDAO;
    private LibroDAO    libroDAO;

    @Override
    public void init() {
        this.prestamoDAO = new PrestamoDAO();
        this.libroDAO    = new LibroDAO();
    }

    // ─── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!verificarSesion(req, resp)) return;

        // Pasar a la vista: lista completa + libros disponibles para préstamo
        req.setAttribute("prestamos",         prestamoDAO.listarTodos());
        req.setAttribute("librosDisponibles", libroDAO.listarDisponibles());

        req.getRequestDispatcher("/WEB-INF/views/prestamos.jsp").forward(req, resp);
    }

    // ─── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!verificarSesion(req, resp)) return;

        String action = req.getParameter("action");

        if ("prestar".equals(action)) {
            procesarPrestamo(req, resp);
        } else if ("devolver".equals(action)) {
            procesarDevolucion(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/prestamos");
        }
    }

    // ─── Métodos privados ─────────────────────────────────────────────────────

    private void procesarPrestamo(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int libroId = Integer.parseInt(req.getParameter("libroId"));

            // Obtener el usuario de la sesión actual
            HttpSession sesion = req.getSession(false);
            Usuario usuario = (Usuario) sesion.getAttribute("usuario");

            prestamoDAO.realizarPrestamo(libroId, usuario.getId());
            resp.sendRedirect(req.getContextPath() + "/prestamos?mensaje=prestamo-ok");

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/prestamos?error=libro-invalido");
        } catch (RuntimeException e) {
            System.err.println("[PrestamoServlet] Error en préstamo: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/prestamos?error=prestamo-fallido");
        }
    }

    private void procesarDevolucion(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int prestamoId = Integer.parseInt(req.getParameter("id"));
            prestamoDAO.registrarDevolucion(prestamoId);
            resp.sendRedirect(req.getContextPath() + "/prestamos?mensaje=devolucion-ok");

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/prestamos?error=prestamo-invalido");
        } catch (RuntimeException e) {
            System.err.println("[PrestamoServlet] Error en devolución: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/prestamos?error=devolucion-fallida");
        }
    }

    /**
     * Verifica sesión activa, redirige a login si no existe.
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
