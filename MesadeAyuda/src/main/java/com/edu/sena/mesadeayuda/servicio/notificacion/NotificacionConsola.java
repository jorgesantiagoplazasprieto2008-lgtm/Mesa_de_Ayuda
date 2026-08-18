package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class NotificacionConsola implements Notificador {
    private static final Logger LOGGER = Logger.getLogger(NotificacionConsola.class.getName());
    @Override
    public void notificar(Usuario destinatario, Ticket ticket, String mensaje) {
        if (destinatario == null || ticket == null) return;
        LOGGER.info(String.format("[NOTIFICACIÓN CONSOLA] Para: %s (%s) | Ticket #%d: %s | Mensaje: %s",
                destinatario.getNombre(), destinatario.getCorreo(), ticket.getId(), ticket.getTitulo(), mensaje));
    }
}
