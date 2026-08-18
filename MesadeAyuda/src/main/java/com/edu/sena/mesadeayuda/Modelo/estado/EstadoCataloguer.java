package com.edu.sena.mesadeayuda.Modelo.estado;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;



/**
 *
 * @author Sagi
 */
public class EstadoCataloguer {
        
    public static EstadoTicket desdeNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new EstadoNuevo();
        }
        String clave = nombre.trim().toUpperCase().replace(" ", "_");
        switch (clave) {
            case "ASIGNADO":
                return new EstadoAsignado();
            case "EN_PROCESO":
            case "ENPROCESO":
                return new EstadoEnProceso();
            case "RESUELTO":
                return new EstadoResuelto();
            case "CERRADO":
                return new EstadoCerrado();
            case "CANCELADO":
                return new EstadoCancelado();
            case "NUEVO":
            default:
                return new EstadoNuevo();
        }
    }
}
