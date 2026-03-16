package com.untec.biblioteca.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Modelo: Prestamo
 * Representa el préstamo de un libro a un usuario.
 * Forma parte de la capa MODEL del patrón MVC.
 */
public class Prestamo {

    private int id;
    private int libroId;
    private int usuarioId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion; // null si aún no fue devuelto
    private boolean activo;

    // Campos de joins para mostrar en vistas (no son columnas de la tabla)
    private String tituloLibro;
    private String nombreUsuario;

    // Constructor vacío
    public Prestamo() {}

    // Constructor para crear un nuevo préstamo
    public Prestamo(int libroId, int usuarioId) {
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = LocalDate.now();
        this.activo = true;
    }

    // Constructor completo
    public Prestamo(int id, int libroId, int usuarioId,
                    LocalDate fechaPrestamo, LocalDate fechaDevolucion, boolean activo) {
        this.id = id;
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.activo = activo;
    }

    // ─── Getters y Setters ───────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLibroId() { return libroId; }
    public void setLibroId(int libroId) { this.libroId = libroId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getTituloLibro() { return tituloLibro; }
    public void setTituloLibro(String tituloLibro) { this.tituloLibro = tituloLibro; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    /** Retorna la fecha de préstamo formateada como dd/MM/yyyy */
    public String getFechaPrestamoFormateada() {
        if (fechaPrestamo == null) return "-";
        return fechaPrestamo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Retorna la fecha de devolución formateada o "Pendiente" si no hay */
    public String getFechaDevolucionFormateada() {
        if (fechaDevolucion == null) return "Pendiente";
        return fechaDevolucion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return "Prestamo{id=" + id + ", libroId=" + libroId + ", usuarioId=" + usuarioId + "}";
    }
}
