package com.edu.sena.mesadeayuda.Modelo.estado;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;

/**
 *
 * @author Usuario
 */
public class EstadoResuelto implements EstadoTicket {
    @Override
    public EstadoTicket asignar() {
        throw new IllegalStateException("No se puede reasignar un ticket ya resuelto.");
    }

    @Override
    public EstadoTicket iniciar() {
        throw new IllegalStateException("El ticket ya fue resuelto.");
    }

    @Override
    public EstadoTicket resolver() {
        return this;
    }

    @Override
    public EstadoTicket cerrar() {
        return new EstadoCerrado();
    }

    @Override
    public EstadoTicket reabrir() {
        return new EstadoEnProceso();
    }

    @Override
    public EstadoTicket cancelar() {
        return new EstadoCancelado();
    }

    @Override
    public String nombre() {
        return "RESUELTO";
    }
}
