package com.edu.sena.mesadeayuda.Modelo.estado;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;

/**
 *
 * @author Sagi
 */
public class EstadoNuevo implements EstadoTicket {
    @Override
    public EstadoTicket asignar() {
        return new EstadoAsignado();
    }

    @Override
    public EstadoTicket iniciar() {
        throw new IllegalStateException("Un ticket NUEVO debe ser asignado a un agente antes de iniciar atención.");
    }

    @Override
    public EstadoTicket resolver() {
        throw new IllegalStateException("Un ticket NUEVO no puede ser resuelto directamente.");
    }

    @Override
    public EstadoTicket cerrar() {
        throw new IllegalStateException("Un ticket NUEVO no puede cerrarse sin ser resuelto.");
    }

    @Override
    public EstadoTicket reabrir() {
        throw new IllegalStateException("Un ticket NUEVO no ha sido cerrado para ser reabierto.");
    }

    @Override
    public EstadoTicket cancelar() {
        return new EstadoCancelado();
    }

    @Override
    public String nombre() {
        return "NUEVO";
    }
}