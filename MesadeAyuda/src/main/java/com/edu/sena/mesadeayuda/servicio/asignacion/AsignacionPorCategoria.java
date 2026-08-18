package com.edu.sena.mesadeayuda.servicio.asignacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sagi
 */

public class AsignacionPorCategoria implements EstrategiaAsignacion {
    @Override
    public Optional<Usuario> seleccionarAgente(Ticket ticket, List<Usuario> agentesDisponibles) {
        if (agentesDisponibles == null || agentesDisponibles.isEmpty()) {
            return Optional.empty();
        }
        if (ticket == null || ticket.getCategoria() == null || ticket.getCategoria().getNombre() == null) {
            return Optional.of(agentesDisponibles.get(0));
        }
        String nombreCategoria = ticket.getCategoria().getNombre().toLowerCase();
        // Buscar primero un agente cuya coincidencia de nombre coincida con la categoría
        Optional<Usuario> agenteEspecializado = agentesDisponibles.stream()
                .filter(a -> a.getNombre() != null && coincidenPalabrasClave(a.getNombre().toLowerCase(), nombreCategoria))
                .findFirst();
        // Si no hay especialista, retornar el primer agente disponible (fallback transparente)
        return agenteEspecializado.or(() -> Optional.of(agentesDisponibles.get(0)));
    }
    private boolean coincidenPalabrasClave(String nombreAgente, String nombreCategoria) {
        if (nombreCategoria.contains("red") && nombreAgente.contains("red")) return true;
        if (nombreCategoria.contains("software") && nombreAgente.contains("software")) return true;
        if (nombreCategoria.contains("hardware") && nombreAgente.contains("hardware")) return true;
        return nombreAgente.contains(nombreCategoria);
    }
}
