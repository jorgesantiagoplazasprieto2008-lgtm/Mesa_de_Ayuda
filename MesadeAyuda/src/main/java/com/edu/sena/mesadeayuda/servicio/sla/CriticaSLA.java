/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.sena.mesadeayuda.servicio.sla;

import java.time.LocalDateTime;

/**
 *
 * @author Usuario
 */
public class CriticaSLA implements EstrategiaSLA{
    private static final int HORAS = 2;
    
    @Override
    public LocalDateTime calcularFechaLimite(LocalDateTime fechaCreacion) {
        return fechaCreacion != null ? fechaCreacion.plusHours(HORAS) : LocalDateTime.now().plusHours(HORAS); 
    }
    
    @Override 
    public int getHorasAtencion() {
        return HORAS;
    }
    
}
