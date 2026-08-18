package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.*;

/**
 *
 * @author Usuario
 */
public interface Notificador {
    void notificar(Usuario destinatario, Ticket ticket, String mensaje);
}

