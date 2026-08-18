package com.edu.sena.mesadeayuda.servicio.asignacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.repositorio.TicketRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sagi
 */
public class AsignacionMenorCarga implements EstrategiaAsignacion {
    private final TicketRepository ticketRepository;
    public AsignacionMenorCarga(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    @Override
    public Optional<Usuario> seleccionarAgente(Ticket ticket, List<Usuario> agentesDisponibles) {
        if (agentesDisponibles == null || agentesDisponibles.isEmpty()) {
            return Optional.empty();
        }
        if (ticketRepository == null) {
            return Optional.of(agentesDisponibles.get(0));
        }
        return agentesDisponibles.stream()
                .min(Comparator.comparingInt(agente -> {
                    List<Ticket> ticketsAgente = ticketRepository.buscarPorAgente(agente.getId());
                    // Contar sólo tickets activos (no cerrados ni cancelados)
                    return (int) ticketsAgente.stream()
                            .filter(t -> t.getEstado() != null && 
                                   !"CERRADO".equalsIgnoreCase(t.getEstado().nombre()) && 
                                   !"CANCELADO".equalsIgnoreCase(t.getEstado().nombre()))
                            .count();
                }));
    }
}

