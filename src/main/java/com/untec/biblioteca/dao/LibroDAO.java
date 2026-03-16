package com.untec.biblioteca.dao;

import com.untec.biblioteca.model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: LibroDAO
 * Implementa las operaciones CRUD para la entidad Libro.
 * Accede a la base de datos mediante JDBC usando ConexionDB (Singleton).
 */
public class LibroDAO {

    private final ConexionDB conexionDB;

    public LibroDAO() {
        this.conexionDB = ConexionDB.getInstancia();
    }

    // ─── READ: Listar todos los libros ────────────────────────────────────────

    /**
     * Retorna la lista completa de libros en la biblioteca.
     */
    public List<Libro> listarTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, isbn, anio, descripcion, disponible FROM libros ORDER BY titulo";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error listando libros: " + e.getMessage());
            throw new RuntimeException("Error al listar libros", e);
        }
        return libros;
    }

    /**
     * Retorna solo los libros que están disponibles para préstamo.
     */
    public List<Libro> listarDisponibles() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, isbn, anio, descripcion, disponible " +
                     "FROM libros WHERE disponible = TRUE ORDER BY titulo";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error listando libros disponibles: " + e.getMessage());
            throw new RuntimeException("Error al listar libros disponibles", e);
        }
        return libros;
    }

    // ─── READ: Buscar por ID ──────────────────────────────────────────────────

    /**
     * Busca un libro por su ID. Retorna null si no existe.
     */
    public Libro buscarPorId(int id) {
        String sql = "SELECT id, titulo, autor, isbn, anio, descripcion, disponible FROM libros WHERE id = ?";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLibro(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error buscando libro por ID: " + e.getMessage());
            throw new RuntimeException("Error al buscar libro por ID", e);
        }
        return null;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Inserta un nuevo libro en la base de datos.
     */
    public void agregar(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, anio, descripcion, disponible) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setInt(4, libro.getAnio());
            ps.setString(5, libro.getDescripcion());
            ps.setBoolean(6, libro.isDisponible());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error agregando libro: " + e.getMessage());
            throw new RuntimeException("Error al agregar libro", e);
        }
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Actualiza los datos de un libro existente.
     */
    public void actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, " +
                     "anio = ?, descripcion = ?, disponible = ? WHERE id = ?";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setInt(4, libro.getAnio());
            ps.setString(5, libro.getDescripcion());
            ps.setBoolean(6, libro.isDisponible());
            ps.setInt(7, libro.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error actualizando libro: " + e.getMessage());
            throw new RuntimeException("Error al actualizar libro", e);
        }
    }

    /**
     * Cambia la disponibilidad de un libro (para préstamos y devoluciones).
     */
    public void actualizarDisponibilidad(int libroId, boolean disponible) {
        String sql = "UPDATE libros SET disponible = ? WHERE id = ?";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, disponible);
            ps.setInt(2, libroId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error actualizando disponibilidad: " + e.getMessage());
            throw new RuntimeException("Error al actualizar disponibilidad", e);
        }
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Elimina un libro por su ID.
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[LibroDAO] Error eliminando libro: " + e.getMessage());
            throw new RuntimeException("Error al eliminar libro", e);
        }
    }

    // ─── Método privado: mapear ResultSet → Libro ─────────────────────────────

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return new Libro(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("isbn"),
            rs.getInt("anio"),
            rs.getString("descripcion"),
            rs.getBoolean("disponible")
        );
    }
}
