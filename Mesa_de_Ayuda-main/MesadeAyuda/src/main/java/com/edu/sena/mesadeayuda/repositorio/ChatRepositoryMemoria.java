package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.MensajeChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en Memoria para Mensajes de Chat (Fallback sin base de datos).
 */
public class ChatRepositoryMemoria implements ChatRepository {

    private final Map<Long, List<MensajeChat>> mensajesPorTicketMap = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public MensajeChat guardar(MensajeChat mensaje) {
        if (mensaje == null || mensaje.getTicketId() == null) return null;

        if (mensaje.getId() == null || mensaje.getId() == 0) {
            mensaje.setId(idSequence.incrementAndGet());
        }

        mensajesPorTicketMap.computeIfAbsent(mensaje.getTicketId(), k -> new ArrayList<>()).add(mensaje);
        return mensaje;
    }

    @Override
    public List<MensajeChat> obtenerMensajesPorTicket(Long ticketId) {
        if (ticketId == null) return new ArrayList<>();
        return new ArrayList<>(mensajesPorTicketMap.getOrDefault(ticketId, new ArrayList<>()));
    }

    @Override
    public List<MensajeChat> obtenerMensajesEntreUsuarios(Long usuario1Id, Long usuario2Id) {
        List<MensajeChat> resultado = new ArrayList<>();
        for (List<MensajeChat> lista : mensajesPorTicketMap.values()) {
            resultado.addAll(lista.stream().filter(m ->
                    (m.getEmisorId().equals(usuario1Id) && m.getReceptorId().equals(usuario2Id)) ||
                    (m.getEmisorId().equals(usuario2Id) && m.getReceptorId().equals(usuario1Id))
            ).collect(Collectors.toList()));
        }
        return resultado;
    }

    @Override
    public void marcarComoLeidos(Long ticketId, Long receptorId) {
        List<MensajeChat> lista = mensajesPorTicketMap.get(ticketId);
        if (lista != null) {
            for (MensajeChat m : lista) {
                if (m.getReceptorId().equals(receptorId)) {
                    m.setLeido(true);
                }
            }
        }
    }
}
