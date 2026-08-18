package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */

public class NotificacionEmailMock implements Notificador {
    private static final Logger LOGGER = Logger.getLogger(NotificacionEmailMock.java.getName());
    @Override
    public void notificar(Usuario destinatario, Ticket ticket, String mensaje) {
        if (destinatario == null || ticket == null) return;
        LOGGER.info(String.format("[EMAIL ENVIADO] TO: %s | ASUNTO: Actualización Ticket #%d | CUERPO: Estimado(a) %s, %s",
                destinatario.getCorreo(), ticket.getId(), destinatario.getNombre(), mensaje));
    }
}
