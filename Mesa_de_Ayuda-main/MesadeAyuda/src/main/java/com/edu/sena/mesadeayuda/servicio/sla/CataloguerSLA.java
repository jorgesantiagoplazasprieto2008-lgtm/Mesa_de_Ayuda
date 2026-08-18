/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edu.sena.mesadeayuda.servicio.sla;

import com.edu.sena.mesadeayuda.Modelo.Prioridad;

/**
 *
 * @author Usuario
 */
public class CataloguerSLA {
    public static EstrategiaSLA obtenerEstrategia(Prioridad prioridad) {
        if (prioridad == null) return new MediaSLA();
        switch (prioridad) {
            case BAJA: return new BajaSLA();
            case ALTA: return new AltaSLA();
            case CRITICA: return new CriticaSLA();
            case MEDIA:
            default:
                return new MediaSLA();
        }
    }
}