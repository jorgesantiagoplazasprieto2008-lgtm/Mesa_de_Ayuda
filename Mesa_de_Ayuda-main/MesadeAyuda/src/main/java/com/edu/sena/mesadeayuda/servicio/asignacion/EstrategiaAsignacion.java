package com.edu.sena.mesadeayuda.servicio.asignacion;

import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sagi
 */

public interface EstrategiaAsignacion {
    Optional<Usuario> seleccionarAgente(Ticket ticket, List<Usuario> agentesDisponibles);
}
