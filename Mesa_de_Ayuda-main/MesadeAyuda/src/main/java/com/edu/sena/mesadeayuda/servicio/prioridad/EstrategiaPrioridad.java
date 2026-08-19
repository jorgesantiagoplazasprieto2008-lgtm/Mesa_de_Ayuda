package com.edu.sena.mesadeayuda.servicio.prioridad;

import com.edu.sena.mesadeayuda.Modelo.Prioridad;
import com.edu.sena.mesadeayuda.Modelo.Ticket;

/**
 * Interfaz para definir la estrategia de cálculo de prioridad de un ticket (Patrón Strategy / OCP).
 */
public interface EstrategiaPrioridad {
    Prioridad determinarPrioridad(Ticket ticket);
}
