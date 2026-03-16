package com.untec.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO: ConexionDB
 * Implementa el patrón Singleton para gestionar la conexión a la base de datos.
 * Utiliza H2 como base de datos embebida (uso educativo, sin instalación extra).
 *
 * Al inicializarse por primera vez, crea las tablas y carga datos de ejemplo.
 */
public class ConexionDB {

    // URL de conexión H2 en modo archivo (los datos persisten entre reinicios).
    // AUTO_SERVER se elimina: causaba conflictos de puerto en algunos sistemas.
    private static final String URL      = "jdbc:h2:file:./biblioteca_untec_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USUARIO  = "sa";
    private static final String PASSWORD = "";

    // Instancia única del Singleton
    private static ConexionDB instancia;

    /**
     * Constructor privado - ejecuta la inicialización de la base de datos.
     */
    private ConexionDB() {
        try {
            Class.forName("org.h2.Driver");
            inicializarBaseDeDatos();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver H2: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna la instancia única de ConexionDB (Singleton).
     * Thread-safe gracias a synchronized.
     */
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     * El llamador es responsable de cerrarla (try-with-resources).
     */
    public Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /**
     * Crea el schema y datos iniciales si no existen.
     * Usa IF NOT EXISTS para ser idempotente.
     */
    private void inicializarBaseDeDatos() {
        String sqlCrearTablaUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id       INTEGER     NOT NULL AUTO_INCREMENT PRIMARY KEY,
                nombre   VARCHAR(100) NOT NULL,
                email    VARCHAR(150) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                rol      VARCHAR(20)  NOT NULL DEFAULT 'ESTUDIANTE'
            )
            """;

        String sqlCrearTablaLibros = """
            CREATE TABLE IF NOT EXISTS libros (
                id          INTEGER      NOT NULL AUTO_INCREMENT PRIMARY KEY,
                titulo      VARCHAR(200) NOT NULL,
                autor       VARCHAR(150) NOT NULL,
                isbn        VARCHAR(20),
                anio        INTEGER,
                descripcion VARCHAR(500),
                disponible  BOOLEAN      NOT NULL DEFAULT TRUE
            )
            """;

        String sqlCrearTablaPrestamos = """
            CREATE TABLE IF NOT EXISTS prestamos (
                id               INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
                libro_id         INTEGER NOT NULL,
                usuario_id       INTEGER NOT NULL,
                fecha_prestamo   DATE    NOT NULL,
                fecha_devolucion DATE,
                activo           BOOLEAN NOT NULL DEFAULT TRUE,
                FOREIGN KEY (libro_id)   REFERENCES libros(id),
                FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
            )
            """;

        // Datos de ejemplo (solo si las tablas están vacías)
        String sqlInsertarUsuarios = """
            MERGE INTO usuarios (id, nombre, email, password, rol) KEY(email)
            VALUES
                (1, 'Administrador UNTEC', 'admin@untec.edu',    'admin123',    'ADMIN'),
                (2, 'Juan Estudiante',     'juan@untec.edu',     'juan123',     'ESTUDIANTE'),
                (3, 'María García',        'maria@untec.edu',    'maria123',    'ESTUDIANTE')
            """;

        String sqlInsertarLibros = """
            MERGE INTO libros (id, titulo, autor, isbn, anio, descripcion, disponible) KEY(isbn)
            VALUES
                (1, 'Clean Code',                        'Robert C. Martin', '9780132350884', 2008,
                 'Guía sobre cómo escribir código limpio y mantenible.',        TRUE),
                (2, 'El Programador Pragmático',         'David Thomas',     '9780135957059', 2019,
                 'Consejos prácticos para el desarrollo de software moderno.',  TRUE),
                (3, 'Patrones de Diseño',                'Gang of Four',     '9780201633610', 1994,
                 'Catálogo de patrones de diseño reutilizables.',               TRUE),
                (4, 'Algoritmos y Estructuras de Datos', 'Thomas Cormen',    '9780262033848', 2009,
                 'Referencia clásica sobre algoritmos y análisis de complejidad.', TRUE),
                (5, 'Java EE 8 Application Development','David Heffelfinger','9781788293679', 2017,
                 'Desarrollo de aplicaciones empresariales con Java EE 8.',     TRUE),
                (6, 'Spring in Action',                  'Craig Walls',      '9781617294945', 2018,
                 'Desarrollo de aplicaciones con el framework Spring.',         TRUE)
            """;

        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlCrearTablaUsuarios);
            stmt.execute(sqlCrearTablaLibros);
            stmt.execute(sqlCrearTablaPrestamos);
            stmt.execute(sqlInsertarUsuarios);
            stmt.execute(sqlInsertarLibros);

            System.out.println("[BibliotecaUNTEC] Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("[BibliotecaUNTEC] Error inicializando BD: " + e.getMessage());
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }
}
