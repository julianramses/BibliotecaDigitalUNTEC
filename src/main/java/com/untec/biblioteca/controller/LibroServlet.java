package com.untec.biblioteca.controller;

import com.untec.biblioteca.dao.LibroDAO;
import com.untec.biblioteca.model.Libro;
import com.untec.biblioteca.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * CONTROLLER: LibroServlet
 * Gestiona el CRUD completo de libros.
 *
 * GET  /libros                      → Listar todos los libros.
 * GET  /libros?action=agregar       → Mostrar formulario de nuevo libro.
 * POST /libros?action=agregar       → Guardar nuevo libro.
 * GET  /libros?action=editar&id=X   → Mostrar formulario de edición.
 * POST /libros?action=editar        → Actualizar libro existente.
 * GET  /libros?action=eliminar&id=X → Eliminar libro.
 *
 * Forma parte de la capa CONTROLLER del patrón MVC.
 */
@WebServlet("/libros")
public class LibroServlet extends HttpServlet {

    private LibroDAO libroDAO;

    @Override
    public void init() {
        this.libroDAO = new LibroDAO();
    }

    // ─── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!verificarSesion(req, resp)) return;

        String action = req.getParameter("action");

        if (action == null || action.isEmpty()) {
            // Listar todos los libros
            mostrarListado(req, resp);

        } else {
            switch (action) {
                case "agregar"  -> mostrarFormularioAgregar(req, resp);
                case "editar"   -> mostrarFormularioEditar(req, resp);
                case "eliminar" -> eliminarLibro(req, resp);
                default         -> mostrarListado(req, resp);
            }
        }
    }

    // ─── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!verificarSesion(req, resp)) return;
        verificarRolAdmin(req, resp);

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("agregar".equals(action)) {
            procesarAgregarLibro(req, resp);
        } else if ("editar".equals(action)) {
            procesarEditarLibro(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/libros");
        }
    }

    // ─── Métodos privados: acciones GET ───────────────────────────────────────

    private void mostrarListado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Libro> libros = libroDAO.listarTodos();
        req.setAttribute("libros", libros);
        req.setAttribute("totalLibros", libros.size());
        req.getRequestDispatcher("/WEB-INF/views/libros.jsp").forward(req, resp);
    }

    private void mostrarFormularioAgregar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/agregar-libro.jsp").forward(req, resp);
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Libro libro = libroDAO.buscarPorId(id);
            if (libro != null) {
                req.setAttribute("libro", libro);
                req.getRequestDispatcher("/WEB-INF/views/editar-libro.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Libro no encontrado.");
                mostrarListado(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/libros");
        }
    }

    private void eliminarLibro(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            libroDAO.eliminar(id);
            resp.sendRedirect(req.getContextPath() + "/libros?mensaje=eliminado");
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/libros");
        }
    }

    // ─── Métodos privados: acciones POST ──────────────────────────────────────

    private void procesarAgregarLibro(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Libro libro = construirLibroDesdeFormulario(req, 0);

        // Validación mínima
        if (libro.getTitulo().isEmpty() || libro.getAutor().isEmpty()) {
            req.setAttribute("error", "El título y el autor son obligatorios.");
            req.getRequestDispatcher("/WEB-INF/views/agregar-libro.jsp").forward(req, resp);
            return;
        }

        libroDAO.agregar(libro);
        resp.sendRedirect(req.getContextPath() + "/libros?mensaje=agregado");
    }

    private void procesarEditarLibro(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Libro libro = construirLibroDesdeFormulario(req, id);
            libroDAO.actualizar(libro);
            resp.sendRedirect(req.getContextPath() + "/libros?mensaje=actualizado");
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/libros");
        }
    }

    // ─── Métodos de utilidad ──────────────────────────────────────────────────

    /**
     * Construye un objeto Libro a partir de los parámetros del formulario.
     */
    private Libro construirLibroDesdeFormulario(HttpServletRequest req, int id) {
        Libro libro = new Libro();
        libro.setId(id);
        libro.setTitulo(obtenerParametro(req, "titulo"));
        libro.setAutor(obtenerParametro(req, "autor"));
        libro.setIsbn(obtenerParametro(req, "isbn"));
        libro.setDescripcion(obtenerParametro(req, "descripcion"));
        libro.setDisponible(true);

        try {
            String anioStr = req.getParameter("anio");
            libro.setAnio(anioStr != null && !anioStr.isEmpty() ? Integer.parseInt(anioStr) : 0);
        } catch (NumberFormatException e) {
            libro.setAnio(0);
        }

        return libro;
    }

    private String obtenerParametro(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return valor != null ? valor.trim() : "";
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

    /**
     * Verifica que el usuario sea administrador para operaciones de escritura.
     */
    private void verificarRolAdmin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion != null) {
            Usuario u = (Usuario) sesion.getAttribute("usuario");
            if (u != null && !u.esAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/libros");
            }
        }
    }
}
