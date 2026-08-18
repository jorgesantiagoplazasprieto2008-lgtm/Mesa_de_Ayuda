package com.edu.sena.mesadeayuda.servicio.sla;

import java.time.LocalDateTime;

/**
 *
 * @author Sagi
 */
public interface EstrategiaSLA {
    LocalDateTime calcularFechaLimite(LocalDateTime fechaCreacion);
    int getHorasAtencion();
}
