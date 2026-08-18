package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Ticket;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sagi
 */
public interface TicketRepository {
     Ticket guardar(Ticket ticket);
    Optional<Ticket> buscarPorId(Long id);
    List<Ticket> buscarPorSolicitante(Long solicitanteId);
    List<Ticket> buscarPorAgente(Long agenteId);
    List<Ticket> obtenerTodos();
    void actualizar(Ticket ticket);
}
