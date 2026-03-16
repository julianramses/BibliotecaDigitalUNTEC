package com.untec.biblioteca.dao;

import com.untec.biblioteca.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: UsuarioDAO
 * Maneja las operaciones de base de datos para la entidad Usuario.
 * Incluye autenticación, búsqueda y listado de usuarios.
 */
public class UsuarioDAO {

    private final ConexionDB conexionDB;

    public UsuarioDAO() {
        this.conexionDB = ConexionDB.getInstancia();
    }

    // ─── READ: Listar todos ───────────────────────────────────────────────────

    /**
     * Retorna la lista completa de usuarios registrados.
     */
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, email, password, rol FROM usuarios ORDER BY nombre";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error listando usuarios: " + e.getMessage());
            throw new RuntimeException("Error al listar usuarios", e);
        }
        return usuarios;
    }

    // ─── READ: Buscar por ID ──────────────────────────────────────────────────

    /**
     * Busca un usuario por su ID. Retorna null si no existe.
     */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, nombre, email, password, rol FROM usuarios WHERE id = ?";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error buscando usuario por ID: " + e.getMessage());
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
        return null;
    }

    // ─── AUTENTICACIÓN ────────────────────────────────────────────────────────

    /**
     * Autentica a un usuario por email y contraseña.
     * Retorna el Usuario si las credenciales son válidas, null en caso contrario.
     *
     * NOTA: En producción se debe hashear la contraseña (BCrypt, etc.).
     * Para uso educativo se compara en texto plano.
     */
    public Usuario autenticar(String email, String password) {
        String sql = "SELECT id, nombre, email, password, rol FROM usuarios " +
                     "WHERE email = ? AND password = ?";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error autenticando usuario: " + e.getMessage());
            throw new RuntimeException("Error al autenticar usuario", e);
        }
        return null;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public void agregar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail().trim().toLowerCase());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error agregando usuario: " + e.getMessage());
            throw new RuntimeException("Error al agregar usuario", e);
        }
    }

    // ─── Método privado: mapear ResultSet → Usuario ───────────────────────────

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("rol")
        );
    }
}
