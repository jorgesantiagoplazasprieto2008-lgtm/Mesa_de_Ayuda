package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Comentario;
import com.edu.sena.mesadeayuda.Modelo.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repositorio JDBC para comentarios con autoadaptación completa del esquema de base de datos MySQL.
 */
public class ComentarioRepositoryJdbc implements ComentarioRepository {

    private static final Logger LOGGER = Logger.getLogger(ComentarioRepositoryJdbc.class.getName());
    private final UsuarioRepository usuarioRepository;

    public ComentarioRepositoryJdbc(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        asegurarTabla();
    }

    private void asegurarTabla() {
        String sqlCrear = "CREATE TABLE IF NOT EXISTS comentarios (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ticket_id BIGINT NOT NULL, " +
                "usuario_id BIGINT NOT NULL, " +
                "texto TEXT NOT NULL, " +
                "fecha DATETIME NOT NULL)";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlCrear);

            boolean tieneUsuarioId = false;
            boolean tieneAutorId = false;
            boolean tieneIdUsuario = false;
            boolean tieneSolicitanteId = false;

            boolean tieneTexto = false;
            boolean tieneComentario = false;
            boolean tieneContenido = false;

            boolean tieneFecha = false;
            boolean tieneFechaCreacion = false;
            boolean tieneCreatedAt = false;
            boolean tieneFechaComentario = false;

            boolean tieneTicketId = false;
            boolean tieneIdTicket = false;

            DatabaseMetaData md = conn.getMetaData();
            try (ResultSet rs = md.getColumns(null, null, "comentarios", null)) {
                while (rs.next()) {
                    String col = rs.getString("COLUMN_NAME");
                    if ("usuario_id".equalsIgnoreCase(col)) tieneUsuarioId = true;
                    if ("autor_id".equalsIgnoreCase(col)) tieneAutorId = true;
                    if ("id_usuario".equalsIgnoreCase(col)) tieneIdUsuario = true;
                    if ("solicitante_id".equalsIgnoreCase(col)) tieneSolicitanteId = true;

                    if ("texto".equalsIgnoreCase(col)) tieneTexto = true;
                    if ("comentario".equalsIgnoreCase(col)) tieneComentario = true;
                    if ("contenido".equalsIgnoreCase(col)) tieneContenido = true;

                    if ("fecha".equalsIgnoreCase(col)) tieneFecha = true;
                    if ("fecha_creacion".equalsIgnoreCase(col)) tieneFechaCreacion = true;
                    if ("created_at".equalsIgnoreCase(col)) tieneCreatedAt = true;
                    if ("fecha_comentario".equalsIgnoreCase(col)) tieneFechaComentario = true;

                    if ("ticket_id".equalsIgnoreCase(col)) tieneTicketId = true;
                    if ("id_ticket".equalsIgnoreCase(col)) tieneIdTicket = true;
                }
            }

            // Adaptar ticket_id
            if (!tieneTicketId) {
                if (tieneIdTicket) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN id_ticket ticket_id BIGINT NOT NULL"); } catch (Exception ignored) {}
                } else {
                    try { stmt.execute("ALTER TABLE comentarios ADD COLUMN ticket_id BIGINT NOT NULL"); } catch (Exception ignored) {}
                }
            }

            // Adaptar usuario_id
            if (!tieneUsuarioId) {
                if (tieneAutorId) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN autor_id usuario_id BIGINT NOT NULL"); } catch (Exception ignored) {}
                } else if (tieneIdUsuario) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN id_usuario usuario_id BIGINT NOT NULL"); } catch (Exception ignored) {}
                } else if (tieneSolicitanteId) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN solicitante_id usuario_id BIGINT NOT NULL"); } catch (Exception ignored) {}
                } else {
                    try { stmt.execute("ALTER TABLE comentarios ADD COLUMN usuario_id BIGINT NOT NULL DEFAULT 1"); } catch (Exception ignored) {}
                }
            }

            // Adaptar texto
            if (!tieneTexto) {
                if (tieneComentario) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN comentario texto TEXT NOT NULL"); } catch (Exception ignored) {}
                } else if (tieneContenido) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN contenido texto TEXT NOT NULL"); } catch (Exception ignored) {}
                } else {
                    try { stmt.execute("ALTER TABLE comentarios ADD COLUMN texto TEXT NOT NULL"); } catch (Exception ignored) {}
                }
            }

            // Adaptar fecha
            if (!tieneFecha) {
                if (tieneFechaCreacion) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN fecha_creacion fecha DATETIME NOT NULL"); } catch (Exception ignored) {}
                } else if (tieneCreatedAt) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN created_at fecha DATETIME NOT NULL"); } catch (Exception ignored) {}
                } else if (tieneFechaComentario) {
                    try { stmt.execute("ALTER TABLE comentarios CHANGE COLUMN fecha_comentario fecha DATETIME NOT NULL"); } catch (Exception ignored) {}
                } else {
                    try { stmt.execute("ALTER TABLE comentarios ADD COLUMN fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP"); } catch (Exception ignored) {}
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Aviso durante la verificación/modificación del esquema de comentarios: " + e.getMessage());
        }
    }

    @Override
    public Comentario guardar(Long ticketId, Comentario comentario) {
        if (ticketId == null || comentario == null) return comentario;
        asegurarTabla();
        String sql = "INSERT INTO comentarios (ticket_id, usuario_id, texto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, ticketId);
            stmt.setLong(2, comentario.getAutor() != null ? comentario.getAutor().getId() : 1L);
            stmt.setString(3, comentario.getTexto());
            stmt.setTimestamp(4, Timestamp.valueOf(comentario.getFecha() != null ? comentario.getFecha() : LocalDateTime.now()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    comentario.setId(rs.getLong(1));
                }
            }
            return comentario;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar comentario en BD", e);
            throw new RuntimeException("Error al guardar comentario en BD: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Comentario> buscarPorTicket(Long ticketId) {
        List<Comentario> lista = new ArrayList<>();
        if (ticketId == null) return lista;
        asegurarTabla();
        String sql = "SELECT id, usuario_id, texto, fecha FROM comentarios WHERE ticket_id = ? ORDER BY fecha ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    Long uId = rs.getLong("usuario_id");
                    String texto = rs.getString("texto");
                    Timestamp ts = rs.getTimestamp("fecha");
                    LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

                    Usuario autor = usuarioRepository.buscarPorId(uId).orElse(null);
                    lista.add(new Comentario(id, autor, texto, fecha));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al consultar comentarios de ticket #" + ticketId, e);
        }
        return lista;
    }
}
