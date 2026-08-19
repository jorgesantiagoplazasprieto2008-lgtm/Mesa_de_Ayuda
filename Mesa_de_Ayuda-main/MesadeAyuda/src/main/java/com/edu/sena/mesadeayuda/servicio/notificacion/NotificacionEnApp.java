package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.dto.NotificacionDTO;
import com.edu.sena.mesadeayuda.repositorio.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Canal de Notificaciones En-App con sanitización estricta para SweetAlert2.
 */
public class NotificacionEnApp implements Notificador {

    private static final Logger LOGGER = Logger.getLogger(NotificacionEnApp.class.getName());
    private static final Map<Long, List<NotificacionDTO>> MEMORIA_NOTIFICACIONES = new ConcurrentHashMap<>();
    private static final AtomicLong SEQ = new AtomicLong(0);

    public NotificacionEnApp() {
        asegurarTablaBD();
    }

    private static void asegurarTablaBD() {
        String sql = "CREATE TABLE IF NOT EXISTS notificaciones (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "usuario_id BIGINT NOT NULL, " +
                "ticket_id BIGINT NOT NULL, " +
                "mensaje TEXT NOT NULL, " +
                "tipo_canal VARCHAR(20) DEFAULT 'APP', " +
                "leido TINYINT(1) DEFAULT 0, " +
                "fecha DATETIME DEFAULT CURRENT_TIMESTAMP)";
        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception ignored) {}
    }

    @Override
    public void notificar(Usuario destinatario, Ticket ticket, String mensaje) {
        if (destinatario == null || ticket == null || mensaje == null) return;

        String icono = determinarIcono(mensaje);
        String titulo = determinarTitulo(mensaje);
        String msgSanitizado = sanitizarParaJs(mensaje);
        String tituloSanitizado = sanitizarParaJs(titulo);

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO notificaciones (usuario_id, ticket_id, mensaje, tipo_canal, leido) VALUES (?, ?, ?, ?, 0)")) {
            stmt.setLong(1, destinatario.getId());
            stmt.setLong(2, ticket.getId());
            stmt.setString(3, mensaje);
            stmt.setString(4, "APP");
            stmt.executeUpdate();
            LOGGER.info(String.format("[NOTIFICACIÓN EN-APP BD] Usuario ID: %d | Ticket ID: %d", destinatario.getId(), ticket.getId()));
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Notificación guardada en cola en memoria fallback: " + e.getMessage());
        }

        NotificacionDTO dto = new NotificacionDTO(SEQ.incrementAndGet(), (long) ticket.getId(), tituloSanitizado, msgSanitizado, icono);
        MEMORIA_NOTIFICACIONES.computeIfAbsent(destinatario.getId(), k -> new ArrayList<>()).add(dto);
    }

    public static List<NotificacionDTO> obtenerYMarcarLeidas(Long usuarioId) {
        List<NotificacionDTO> resultado = new ArrayList<>();
        if (usuarioId == null) return resultado;

        // 1. Memoria
        List<NotificacionDTO> enMemoria = MEMORIA_NOTIFICACIONES.remove(usuarioId);
        if (enMemoria != null) {
            resultado.addAll(enMemoria);
        }

        // 2. Base de datos
        asegurarTablaBD();
        String sqlSelect = "SELECT id, ticket_id, mensaje FROM notificaciones WHERE usuario_id = ? AND leido = 0 ORDER BY fecha ASC";
        String sqlUpdate = "UPDATE notificaciones SET leido = 1 WHERE usuario_id = ? AND leido = 0";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmtSel = conn.prepareStatement(sqlSelect)) {
            stmtSel.setLong(1, usuarioId);
            try (ResultSet rs = stmtSel.executeQuery()) {
                while (rs.next()) {
                    Long nId = rs.getLong("id");
                    Long tId = rs.getLong("ticket_id");
                    String msgRaw = rs.getString("mensaje");
                    String msgSanitizado = sanitizarParaJs(msgRaw);
                    String icono = determinarIcono(msgRaw);
                    String tituloSanitizado = sanitizarParaJs(determinarTitulo(msgRaw));

                    boolean existe = resultado.stream().anyMatch(n -> msgSanitizado.equalsIgnoreCase(n.getMensaje()));
                    if (!existe) {
                        resultado.add(new NotificacionDTO(nId, tId, tituloSanitizado, msgSanitizado, icono));
                    }
                }
            }

            try (PreparedStatement stmtUp = conn.prepareStatement(sqlUpdate)) {
                stmtUp.setLong(1, usuarioId);
                stmtUp.executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Sin notificaciones pendientes en BD: " + e.getMessage());
        }

        return resultado;
    }

    private static String sanitizarParaJs(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("'", "’")
                    .replace("\r", "")
                    .replace("\n", " ");
    }

    private static String determinarIcono(String mensaje) {
        if (mensaje == null) return "info";
        String msgUpper = mensaje.toUpperCase();
        if (msgUpper.contains("CERRADO") || msgUpper.contains("RESUELTO") || msgUpper.contains("EXITOSAMENTE")) {
            return "success";
        }
        if (msgUpper.contains("REABIERTO") || msgUpper.contains("ASIGNADO") || msgUpper.contains("REASIGNADO")) {
            return "warning";
        }
        if (msgUpper.contains("CANCELADO") || msgUpper.contains("ERROR")) {
            return "error";
        }
        return "info";
    }

    private static String determinarTitulo(String mensaje) {
        if (mensaje == null) return "Notificación de Ticket";
        String u = mensaje.toUpperCase();
        if (u.contains("ASIGNADO") || u.contains("REASIGNADO")) return "¡Asignación de Ticket!";
        if (u.contains("EN PROCESO")) return "Ticket en Atención";
        if (u.contains("RESUELTO")) return "¡Ticket Resuelto!";
        if (u.contains("CERRADO")) return "Ticket Cerrado";
        if (u.contains("REABIERTO")) return "¡Ticket Reabierto!";
        if (u.contains("CANCELADO")) return "Ticket Cancelado";
        return "Notificación de Ticket";
    }
}