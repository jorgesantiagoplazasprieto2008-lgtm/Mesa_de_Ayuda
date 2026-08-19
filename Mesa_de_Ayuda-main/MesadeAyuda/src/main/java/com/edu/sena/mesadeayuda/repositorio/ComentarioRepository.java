package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Comentario;
import java.util.List;

/**
 * Interfaz de Repositorio para la gestión centralizada y global de Comentarios (SRP / DIP).
 */
public interface ComentarioRepository {
    Comentario guardar(Long ticketId, Comentario comentario);
    List<Comentario> buscarPorTicket(Long ticketId);
}
