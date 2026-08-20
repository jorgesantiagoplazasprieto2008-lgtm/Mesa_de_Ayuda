package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.MensajeChat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repositorio JDBC para Persistencia de Mensajes de Chat en MySQL.
 */
public class ChatRepositoryJdbc implements ChatRepository {

    private static final Logger LOGGER = Logger.getLogger(ChatRepositoryJdbc.class.getName());

    public ChatRepositoryJdbc() {
        asegurarTablaBD();
    }

    private void asegurarTablaBD() {
        String sql = "CREATE TABLE IF NOT EXISTS chat_mensajes (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ticket_id BIGINT NOT NULL, " +
                "emisor_id BIGINT NOT NULL, " +
                "emisor_nombre VARCHAR(100) NOT NULL, " +
                "emisor_rol VARCHAR(20) NOT NULL, " +
                "receptor_id BIGINT NOT NULL, " +
                "texto TEXT NOT NULL, " +
                "fecha DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "leido TINYINT(1) DEFAULT 0)";
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, "Tabla chat_mensajes ya existente o error menor: " + e.getMessage());
        }
    }

    @Override
    public MensajeChat guardar(MensajeChat mensaje) {
        asegurarTablaBD();
        String sql = "INSERT INTO chat_mensajes (ticket_id, emisor_id, emisor_nombre, emisor_rol, receptor_id, texto, leido) VALUES (?, ?, ?, ?, ?, ?, 0)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, mensaje.getTicketId());
            stmt.setLong(2, mensaje.getEmisorId());
            stmt.setString(3, mensaje.getEmisorNombre());
            stmt.setString(4, mensaje.getEmisorRol());
            stmt.setLong(5, mensaje.getReceptorId());
            stmt.setString(6, mensaje.getTexto());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    mensaje.setId(rs.getLong(1));
                }
            }
            return mensaje;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar mensaje de chat en JDBC", e);
            throw new RuntimeException("Error en base de datos al enviar mensaje de chat", e);
        }
    }

    @Override
    public List<MensajeChat> obtenerMensajesPorTicket(Long ticketId) {
        asegurarTablaBD();
        List<MensajeChat> lista = new ArrayList<>();
        String sql = "SELECT id, ticket_id, emisor_id, emisor_nombre, emisor_rol, receptor_id, texto, fecha, leido FROM chat_mensajes WHERE ticket_id = ? ORDER BY fecha ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMensaje(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener mensajes por ticket", e);
        }
        return lista;
    }

    @Override
    public List<MensajeChat> obtenerMensajesEntreUsuarios(Long usuario1Id, Long usuario2Id) {
        asegurarTablaBD();
        List<MensajeChat> lista = new ArrayList<>();
        String sql = "SELECT id, ticket_id, emisor_id, emisor_nombre, emisor_rol, receptor_id, texto, fecha, leido FROM chat_mensajes " +
                     "WHERE (emisor_id = ? AND receptor_id = ?) OR (emisor_id = ? AND receptor_id = ?) ORDER BY fecha ASC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuario1Id);
            stmt.setLong(2, usuario2Id);
            stmt.setLong(3, usuario2Id);
            stmt.setLong(4, usuario1Id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMensaje(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener mensajes entre usuarios", e);
        }
        return lista;
    }

    @Override
    public void marcarComoLeidos(Long ticketId, Long receptorId) {
        asegurarTablaBD();
        String sql = "UPDATE chat_mensajes SET leido = 1 WHERE ticket_id = ? AND receptor_id = ? AND leido = 0";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ticketId);
            stmt.setLong(2, receptorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al marcar mensajes como leídos", e);
        }
    }

    private MensajeChat mapearMensaje(ResultSet rs) throws SQLException {
        MensajeChat m = new MensajeChat();
        m.setId(rs.getLong("id"));
        m.setTicketId(rs.getLong("ticket_id"));
        m.setEmisorId(rs.getLong("emisor_id"));
        m.setEmisorNombre(rs.getString("emisor_nombre"));
        m.setEmisorRol(rs.getString("emisor_rol"));
        m.setReceptorId(rs.getLong("receptor_id"));
        m.setTexto(rs.getString("texto"));
        m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        m.setLeido(rs.getBoolean("leido"));
        return m;
    }
}
