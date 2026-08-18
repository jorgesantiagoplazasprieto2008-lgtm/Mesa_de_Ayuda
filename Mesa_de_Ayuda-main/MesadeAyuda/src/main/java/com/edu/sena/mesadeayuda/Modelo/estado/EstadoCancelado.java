package com.edu.sena.mesadeayuda.Modelo.estado;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;

/**
 *
 * @author Sagi
 */
public class EstadoCancelado implements EstadoTicket {
     @Override
    public EstadoTicket asignar() {
        throw new IllegalStateException("Un ticket CANCELADO no se puede asignar.");
    }
    @Override
    public EstadoTicket iniciar() {
        throw new IllegalStateException("Un ticket CANCELADO no se puede iniciar.");
    }
    @Override
    public EstadoTicket resolver() {
        throw new IllegalStateException("Un ticket CANCELADO no se puede resolver.");
    }
    @Override
    public EstadoTicket cerrar() {
        throw new IllegalStateException("Un ticket CANCELADO no se puede cerrar.");
    }
    @Override
    public EstadoTicket reabrir() {
        throw new IllegalStateException("Un ticket CANCELADO no se puede reabrir.");
    }
    @Override
    public EstadoTicket cancelar() {
        return this;
    }
    @Override
    public String nombre() {
        return "CANCELADO";
    }
}
