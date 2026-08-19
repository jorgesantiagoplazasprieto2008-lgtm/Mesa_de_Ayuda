package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import java.util.logging.Logger;

/**
 * Notificador por Correo Electrónico con formato detallado por estado de ticket.
 */
public class NotificacionEmailMock implements Notificador {

    private static final Logger LOGGER = Logger.getLogger(NotificacionEmailMock.class.getName());

    @Override
    public void notificar(Usuario destinatario, Ticket ticket, String mensaje) {
        if (destinatario == null || ticket == null || destinatario.getCorreo() == null) return;

        String estadoStr = ticket.getEstado() != null ? ticket.getEstado().nombre() : "ACTUALIZACIÓN";
        String asunto = String.format("[Mesa de Ayuda SENA] Estado de Ticket #%d (%s): %s", ticket.getId(), ticket.getTitulo(), estadoStr);

        String cuerpo = String.format(
            "Estimado(a) %s,\n\n" +
            "Se ha registrado la siguiente novedad en el Ticket #%d:\n" +
            "--------------------------------------------------\n" +
            "Título del Ticket: %s\n" +
            "Estado Actual: %s\n" +
            "Detalle de la Notificación: %s\n" +
            "--------------------------------------------------\n\n" +
            "Puedes ingresar a la plataforma Mesa de Ayuda CIMM para revisar los detalles o responder en el panel de soporte.\n\n" +
            "Atentamente,\n" +
            "Mesa de Ayuda CIMM - SENA",
            destinatario.getNombre(),
            ticket.getId(),
            ticket.getTitulo(),
            estadoStr,
            mensaje
        );

        LOGGER.info(String.format("\n================ [ENVÍO DE NOTIFICACIÓN POR EMAIL] ================\nPARA: %s (%s)\nASUNTO: %s\nCUERPO:\n%s\n==================================================================",
                destinatario.getNombre(), destinatario.getCorreo(), asunto, cuerpo));
    }
}
