package com.edu.sena.mesadeayuda.servicio.prioridad;

import com.edu.sena.mesadeayuda.Modelo.Prioridad;
import com.edu.sena.mesadeayuda.Modelo.Ticket;

/**
 * Estrategia de priorización automática basada en categoría y palabras clave (RF-03).
 */
public class PriorizacionPalabrasClave implements EstrategiaPrioridad {

    @Override
    public Prioridad determinarPrioridad(Ticket ticket) {
        if (ticket == null) return Prioridad.MEDIA;

        String contenido = ((ticket.getTitulo() != null ? ticket.getTitulo() : "") + " " +
                (ticket.getDescripcion() != null ? ticket.getDescripcion() : "")).toLowerCase();

        // 1. Verificación por palabras clave críticas / urgentes
        if (contenido.contains("caido") || contenido.contains("caído") ||
            contenido.contains("servidor") || contenido.contains("bloqueo general") ||
            contenido.contains("urgente") || contenido.contains("critico") || contenido.contains("crítico")) {
            return Prioridad.CRITICA;
        }

        if (contenido.contains("falla") || contenido.contains("dañado") ||
            contenido.contains("daño") || contenido.contains("sin internet") ||
            contenido.contains("impresora")) {
            return Prioridad.ALTA;
        }

        // 2. Verificación por categoría
        if (ticket.getCategoria() != null && ticket.getCategoria().getNombre() != null) {
            String catName = ticket.getCategoria().getNombre().toLowerCase();
            if (catName.contains("red")) return Prioridad.MEDIA;
            if (catName.contains("hardware")) return Prioridad.ALTA;
            if (catName.contains("software")) return Prioridad.BAJA;
            if (catName.contains("mantenimiento")) return Prioridad.MEDIA;
        }

        return Prioridad.MEDIA;
    }
}
