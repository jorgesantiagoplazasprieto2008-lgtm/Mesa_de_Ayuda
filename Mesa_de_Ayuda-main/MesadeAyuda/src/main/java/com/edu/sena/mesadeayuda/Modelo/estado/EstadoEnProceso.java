package com.edu.sena.mesadeayuda.Modelo.estado;
import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;

/**
 *
 * @author Usuario
 */
public class EstadoEnProceso implements EstadoTicket {
    @Override
    public EstadoTicket asignar() {
        return this; // Permite reasignación durante la atención
    }

    @Override
    public EstadoTicket iniciar() {
        return this;
    }

    @Override
    public EstadoTicket resolver() {
        return new EstadoResuelto();
    }

    @Override
    public EstadoTicket cerrar() {
        throw new IllegalStateException("Debe resolver el ticket antes de cerrarlo.");
    }

    @Override
    public EstadoTicket reabrir() {
        throw new IllegalStateException("El ticket ya está en proceso.");
    }

    @Override
    public EstadoTicket cancelar() {
        return new EstadoCancelado();
    }

    @Override
    public String nombre() {
        return "EN_PROCESO";
    }
}
