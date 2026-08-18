/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.repositorio.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */

public class NotificacionEnApp implements Notificador {
    private static final Logger LOGGER = Logger.getLogger(NotificacionEnApp.class.getName());
    @Override
    public void notificar(Usuario destinatario, Ticket ticket, String mensaje) {
        if (destinatario == null || ticket == null) return;
        String sql = "INSERT INTO notificaciones (usuario_id, ticket_id, mensaje, tipo_canal) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, destinatario.getId());
            stmt.setLong(2, ticket.getId());
            stmt.setString(3, mensaje);
            stmt.setString(4, "APP");
            stmt.executeUpdate();
            LOGGER.info(String.format("[NOTIFICACIÓN EN-APP REGISTRADA] Usuario ID: %d | Ticket ID: %d", destinatario.getId(), ticket.getId()));
        } catch (Exception e) {
            LOGGER.log(Level.ALL.WARNING, "No se pudo registrar la notificación en la BD (Modo fallback a consola active): " + e.getMessage());
        }
    }
}