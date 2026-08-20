package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.MensajeChat;
import java.util.List;

/**
 * Interfaz de repositorio para la gestión de mensajes de chat (Patrón DIP & SRP).
 */
public interface ChatRepository {
    MensajeChat guardar(MensajeChat mensaje);
    List<MensajeChat> obtenerMensajesPorTicket(Long ticketId);
    List<MensajeChat> obtenerMensajesEntreUsuarios(Long usuario1Id, Long usuario2Id);
    void marcarComoLeidos(Long ticketId, Long receptorId);
}
