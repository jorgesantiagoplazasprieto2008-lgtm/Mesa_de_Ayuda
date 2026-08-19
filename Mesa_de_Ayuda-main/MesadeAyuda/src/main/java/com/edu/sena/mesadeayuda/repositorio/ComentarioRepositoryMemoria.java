package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Comentario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositorio en Memoria para gestión global de Comentarios.
 */
public class ComentarioRepositoryMemoria implements ComentarioRepository {

    private final Map<Long, List<Comentario>> comentariosPorTicket = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Comentario guardar(Long ticketId, Comentario comentario) {
        if (ticketId == null || comentario == null) return comentario;
        if (comentario.getId() == null) {
            comentario.setId(sequence.incrementAndGet());
        }
        comentariosPorTicket.computeIfAbsent(ticketId, k -> new ArrayList<>()).add(comentario);
        return comentario;
    }

    @Override
    public List<Comentario> buscarPorTicket(Long ticketId) {
        if (ticketId == null) return new ArrayList<>();
        return new ArrayList<>(comentariosPorTicket.getOrDefault(ticketId, new ArrayList<>()));
    }
}
