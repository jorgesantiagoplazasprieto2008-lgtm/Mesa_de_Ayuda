package com.edu.sena.mesadeayuda.servicio.asignacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Sagi
 */

public class AsignacionTurnoRotativo implements EstrategiaAsignacion {
    private final AtomicInteger contador = new AtomicInteger(0);
    @Override
    public Optional<Usuario> seleccionarAgente(Ticket ticket, List<Usuario> agentesDisponibles) {
        if (agentesDisponibles == null || agentesDisponibles.isEmpty()) {
            return Optional.empty();
        }
        int index = Math.abs(contador.getAndIncrement() % agentesDisponibles.size());
        return Optional.of(agentesDisponibles.get(index));
    }
}
