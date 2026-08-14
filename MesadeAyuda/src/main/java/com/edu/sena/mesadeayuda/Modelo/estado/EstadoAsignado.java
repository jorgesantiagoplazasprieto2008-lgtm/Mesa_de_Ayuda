/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.sena.mesadeayuda.Modelo.estado;

import com.edu.sena.mesadeayuda.Modelo.EstadoTicket;

/**
 *
 * @author Sagi
 */
public class EstadoAsignado implements EstadoTicket {
    @Override
    public EstadoTicket asignar() {
        return this; // Permite reasignar agente permaneciendo en ASIGNADO
    }

    @Override
    public EstadoTicket iniciar() {
        return new EstadoEnProceso();
    }

    @Override
    public EstadoTicket resolver() {
        throw new IllegalStateException("Un ticket ASIGNADO debe estar EN_PROCESO antes de ser resuelto.");
    }

    @Override
    public EstadoTicket cerrar() {
        throw new IllegalStateException("Un ticket ASIGNADO no se puede cerrar sin ser resuelto.");
    }

    @Override
    public EstadoTicket reabrir() {
        throw new IllegalStateException("Un ticket ASIGNADO no ha sido cerrado.");
    }

    @Override
    public EstadoTicket cancelar() {
        return new EstadoCancelado();
    }

    @Override
    public String nombre() {
        return "ASIGNADO";
    }
}
