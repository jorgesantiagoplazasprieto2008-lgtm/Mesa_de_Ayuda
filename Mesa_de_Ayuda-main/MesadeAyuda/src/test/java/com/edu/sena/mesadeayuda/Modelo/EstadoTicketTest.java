package com.edu.sena.mesadeayuda.Modelo;

import com.edu.sena.mesadeayuda.Modelo.estado.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstadoTicketTest {

    @Test
    @DisplayName("NUEVO a ASIGNADO es una transición válida")
    public void testTransicionNuevoAAsignado() {
        EstadoTicket estado = new EstadoNuevo();
        EstadoTicket siguiente = estado.asignar();
        assertEquals("ASIGNADO", siguiente.nombre());
    }

    @Test
    @DisplayName("NUEVO no puede ser resuelto directamente (lanza IllegalStateException)")
    public void testNuevoNoPuedeResolver() {
        EstadoTicket estado = new EstadoNuevo();
        assertThrows(IllegalStateException.class, estado::resolver);
    }

    @Test
    @DisplayName("ASIGNADO a EN_PROCESO es una transición válida")
    public void testTransicionAsignadoAEnProceso() {
        EstadoTicket estado = new EstadoAsignado();
        EstadoTicket siguiente = estado.iniciar();
        assertEquals("EN_PROCESO", siguiente.nombre());
    }

    @Test
    @DisplayName("EN_PROCESO a RESUELTO es una transición válida")
    public void testTransicionEnProcesoAResuelto() {
        EstadoTicket estado = new EstadoEnProceso();
        EstadoTicket siguiente = estado.resolver();
        assertEquals("RESUELTO", siguiente.nombre());
    }

    @Test
    @DisplayName("RESUELTO a CERRADO es una transición válida")
    public void testTransicionResueltoACerrado() {
        EstadoTicket estado = new EstadoResuelto();
        EstadoTicket siguiente = estado.cerrar();
        assertEquals("CERRADO", siguiente.nombre());
    }

    @Test
    @DisplayName("RESUELTO a EN_PROCESO (reabrir) es una transición válida")
    public void testTransicionResueltoAReabrir() {
        EstadoTicket estado = new EstadoResuelto();
        EstadoTicket siguiente = estado.reabrir();
        assertEquals("EN_PROCESO", siguiente.nombre());
    }

    @Test
    @DisplayName("CERRADO a EN_PROCESO (reabrir por Admin) es una transición válida")
    public void testCerradoAdmiteReabrirPorAdmin() {
        EstadoTicket estado = new EstadoCerrado();
        EstadoTicket siguiente = estado.reabrir();
        assertEquals("EN_PROCESO", siguiente.nombre());
    }
}
