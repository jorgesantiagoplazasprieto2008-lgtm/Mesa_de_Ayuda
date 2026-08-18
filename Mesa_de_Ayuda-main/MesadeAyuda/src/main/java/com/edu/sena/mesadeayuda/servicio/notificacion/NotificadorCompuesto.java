/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.sena.mesadeayuda.servicio.notificacion;

import com.edu.sena.mesadeayuda.Modelo.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Sagi
 */

public class NotificadorCompuesto implements Notificador {
    private final List<Notificador> notificadores = new ArrayList<>();
    public NotificadorCompuesto(Notificador... notificadores) {
        if (notificadores != null) {
            this.notificadores.addAll(Arrays.asList(notificadores));
        }
    }
    public void agregarNotificador(Notificador notificador) {
        if (notificador != null) {
            this.notificadores.add(notificador);
        }
    }
    @Override
    public void notificar(Usuario destinatario, Ticket ticket, String mensaje) {
        for (Notificador notificador : notificadores) {
            try {
                notificador.notificar(destinatario, ticket, mensaje);
            } catch (Exception e) {
                // Log error without breaking execution of other channels
            }
        }
    }
}

