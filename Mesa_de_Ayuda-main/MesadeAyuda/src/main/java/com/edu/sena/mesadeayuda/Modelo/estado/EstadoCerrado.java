package com.edu.sena.mesadeayuda.Modelo.estado;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;

/**
 * Estado CERRADO del ticket. Permite reapertura exclusivamente por el Administrador.
 */
public class EstadoCerrado implements EstadoTicket {
    
    @Override
    public EstadoTicket asignar() {
        throw new IllegalStateException("Un ticket CERRADO no admite cambios de agente.");
    }

    @Override
    public EstadoTicket iniciar() {
        throw new IllegalStateException("Un ticket CERRADO no puede iniciarse.");
    }

    @Override
    public EstadoTicket resolver() {
        throw new IllegalStateException("Un ticket CERRADO ya finalizó su ciclo.");
    }

    @Override
    public EstadoTicket cerrar() {
        return this;
    }

    @Override
    public EstadoTicket reabrir() {
        return new EstadoEnProceso();
    }

    @Override
    public EstadoTicket cancelar() {
        throw new IllegalStateException("Un ticket CERRADO no se puede cancelar.");
    }

    @Override
    public String nombre() {
        return "CERRADO";
    }
}
