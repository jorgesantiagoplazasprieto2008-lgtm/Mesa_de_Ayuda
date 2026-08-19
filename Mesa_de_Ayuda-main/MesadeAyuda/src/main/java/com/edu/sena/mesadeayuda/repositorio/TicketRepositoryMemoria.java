package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Comentario;
import com.edu.sena.mesadeayuda.Modelo.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria de tickets con asignación de IDs de comentarios.
 */
public class TicketRepositoryMemoria implements TicketRepository {

    private final Map<Long, Ticket> ticketMap = new ConcurrentHashMap<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);
    private final AtomicLong commentSequence = new AtomicLong(0);

    @Override
    public Ticket guardar(Ticket ticket) {
        if (ticket.getId() == 0) {
            ticket.setId(idSequence.incrementAndGet());
        }
        asegurarIdsComentarios(ticket);
        ticketMap.put((long) ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(ticketMap.get(id));
    }

    @Override
    public List<Ticket> buscarPorSolicitante(Long solicitanteId) {
        if (solicitanteId == null) return List.of();
        return ticketMap.values().stream()
                .filter(t -> t.getSolicitante() != null && solicitanteId.equals(t.getSolicitante().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> buscarPorAgente(Long agenteId) {
        if (agenteId == null) return List.of();
        return ticketMap.values().stream()
                .filter(t -> t.getAgente() != null && agenteId.equals(t.getAgente().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> obtenerTodos() {
        return new ArrayList<>(ticketMap.values());
    }

    @Override
    public void actualizar(Ticket ticket) {
        if (ticket != null) {
            asegurarIdsComentarios(ticket);
            ticketMap.put((long) ticket.getId(), ticket);
        }
    }

    private void asegurarIdsComentarios(Ticket ticket) {
        if (ticket != null && ticket.getComentarios() != null) {
            for (Comentario c : ticket.getComentarios()) {
                if (c.getId() == null) {
                    c.setId(commentSequence.incrementAndGet());
                }
            }
        }
    }
}
