package com.untec.biblioteca.dao;

import com.untec.biblioteca.model.Prestamo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: PrestamoDAO
 * Gestiona las operaciones CRUD para la entidad Prestamo.
 * Incluye lógica de préstamo y devolución de libros.
 */
public class PrestamoDAO {

    private final ConexionDB conexionDB;
    private final LibroDAO   libroDAO;

    public PrestamoDAO() {
        this.conexionDB = ConexionDB.getInstancia();
        this.libroDAO   = new LibroDAO();
    }

    // ─── READ: Listar todos los préstamos ─────────────────────────────────────

    /**
     * Retorna todos los préstamos con información del libro y usuario (JOIN).
     */
    public List<Prestamo> listarTodos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
            SELECT p.id, p.libro_id, p.usuario_id,
                   p.fecha_prestamo, p.fecha_devolucion, p.activo,
                   l.titulo AS titulo_libro,
                   u.nombre AS nombre_usuario
            FROM prestamos p
            JOIN libros   l ON p.libro_id   = l.id
            JOIN usuarios u ON p.usuario_id = u.id
            ORDER BY p.fecha_prestamo DESC
            """;

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                prestamos.add(mapearPrestamoConJoin(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error listando préstamos: " + e.getMessage());
            throw new RuntimeException("Error al listar préstamos", e);
        }
        return prestamos;
    }

    /**
     * Retorna solo los préstamos activos (no devueltos aún).
     */
    public List<Prestamo> listarActivos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = """
            SELECT p.id, p.libro_id, p.usuario_id,
                   p.fecha_prestamo, p.fecha_devolucion, p.activo,
                   l.titulo AS titulo_libro,
                   u.nombre AS nombre_usuario
            FROM prestamos p
            JOIN libros   l ON p.libro_id   = l.id
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE p.activo = TRUE
            ORDER BY p.fecha_prestamo DESC
            """;

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                prestamos.add(mapearPrestamoConJoin(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error listando préstamos activos: " + e.getMessage());
            throw new RuntimeException("Error al listar préstamos activos", e);
        }
        return prestamos;
    }

    // ─── CREATE: Realizar préstamo ────────────────────────────────────────────

    /**
     * Registra un nuevo préstamo y marca el libro como no disponible.
     * Usa transacción para garantizar consistencia.
     */
    public void realizarPrestamo(int libroId, int usuarioId) {
        String sqlPrestamo = "INSERT INTO prestamos (libro_id, usuario_id, fecha_prestamo, activo) " +
                             "VALUES (?, ?, ?, TRUE)";

        try (Connection conn = conexionDB.getConexion()) {
            conn.setAutoCommit(false); // Inicio de transacción

            try (PreparedStatement ps = conn.prepareStatement(sqlPrestamo)) {
                ps.setInt(1, libroId);
                ps.setInt(2, usuarioId);
                ps.setDate(3, Date.valueOf(LocalDate.now()));
                ps.executeUpdate();
            }

            // Marcar libro como no disponible
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE libros SET disponible = FALSE WHERE id = ?")) {
                ps.setInt(1, libroId);
                ps.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
            System.out.println("[PrestamoDAO] Préstamo registrado: libro=" + libroId + ", usuario=" + usuarioId);

        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error realizando préstamo: " + e.getMessage());
            throw new RuntimeException("Error al realizar el préstamo", e);
        }
    }

    // ─── UPDATE: Registrar devolución ─────────────────────────────────────────

    /**
     * Registra la devolución de un libro y lo marca nuevamente como disponible.
     * Usa transacción para garantizar consistencia.
     */
    public void registrarDevolucion(int prestamoId) {
        String sqlPrestamo = "UPDATE prestamos SET fecha_devolucion = ?, activo = FALSE WHERE id = ?";

        try (Connection conn = conexionDB.getConexion()) {
            conn.setAutoCommit(false); // Inicio de transacción

            // Obtener el libroId del préstamo
            int libroId = -1;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT libro_id FROM prestamos WHERE id = ?")) {
                ps.setInt(1, prestamoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        libroId = rs.getInt("libro_id");
                    }
                }
            }

            if (libroId == -1) {
                throw new RuntimeException("Préstamo no encontrado: " + prestamoId);
            }

            // Actualizar préstamo
            try (PreparedStatement ps = conn.prepareStatement(sqlPrestamo)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt(2, prestamoId);
                ps.executeUpdate();
            }

            // Marcar libro como disponible nuevamente
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE libros SET disponible = TRUE WHERE id = ?")) {
                ps.setInt(1, libroId);
                ps.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
            System.out.println("[PrestamoDAO] Devolución registrada: préstamo=" + prestamoId);

        } catch (SQLException e) {
            System.err.println("[PrestamoDAO] Error registrando devolución: " + e.getMessage());
            throw new RuntimeException("Error al registrar la devolución", e);
        }
    }

    // ─── Método privado: mapear ResultSet → Prestamo ──────────────────────────

    private Prestamo mapearPrestamoConJoin(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setId(rs.getInt("id"));
        p.setLibroId(rs.getInt("libro_id"));
        p.setUsuarioId(rs.getInt("usuario_id"));
        p.setActivo(rs.getBoolean("activo"));
        p.setTituloLibro(rs.getString("titulo_libro"));
        p.setNombreUsuario(rs.getString("nombre_usuario"));

        // Convertir java.sql.Date → LocalDate
        Date fechaPrestamo = rs.getDate("fecha_prestamo");
        if (fechaPrestamo != null) {
            p.setFechaPrestamo(fechaPrestamo.toLocalDate());
        }

        Date fechaDevolucion = rs.getDate("fecha_devolucion");
        if (fechaDevolucion != null) {
            p.setFechaDevolucion(fechaDevolucion.toLocalDate());
        }

        return p;
    }
}
