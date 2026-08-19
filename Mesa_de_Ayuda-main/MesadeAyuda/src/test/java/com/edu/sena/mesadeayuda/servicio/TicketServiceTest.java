package com.edu.sena.mesadeayuda.servicio;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceTest {

    private TicketService ticketService;
    private UsuarioRepository usuarioRepo;
    private CategoriaRepository categoriaRepo;
    private TicketRepository ticketRepo;

    @BeforeEach
    public void setUp() {
        usuarioRepo = new UsuarioRepositoryMemoria();
        categoriaRepo = new CategoriaRepositoryMemoria();
        ticketRepo = new TicketRepositoryMemoria();
        ticketService = new TicketService(ticketRepo, usuarioRepo, categoriaRepo);
    }

    @Test
    @DisplayName("Crear ticket asigna automáticamente prioridad y agente responsable")
    public void testCrearTicketAsignacionAutomatica() {
        Usuario solicitante = usuarioRepo.buscarPorCorreo("solicitante@cimm.edu").orElseThrow();

        Ticket t = new Ticket();
        t.setTitulo("Problema crítico de servidor caído");
        t.setDescripcion("El servidor general está fuera de línea de manera urgente");
        t.setSolicitante(solicitante);

        Ticket creado = ticketService.crearTicket(t, 1L); // Categoría Red

        assertNotNull(creado);
        assertEquals(Prioridad.CRITICA, creado.getPrioridad());
        assertNotNull(creado.getAgente());
        assertEquals("ASIGNADO", creado.getEstado().nombre());
        assertNotNull(creado.getFechaLimiteSLA());
    }

    @Test
    @DisplayName("Flujo completo de atención: Atender -> Resolver -> Cerrar")
    public void testFlujoCompletoAtencion() {
        Usuario solicitante = usuarioRepo.buscarPorCorreo("solicitante@cimm.edu").orElseThrow();

        Ticket t = new Ticket();
        t.setTitulo("Teclado dañado");
        t.setDescripcion("Varias teclas no funcionan");
        t.setSolicitante(solicitante);

        Ticket creado = ticketService.crearTicket(t, 2L);
        Long id = (long) creado.getId();
        Usuario agente = creado.getAgente();

        // 1. Agente atiende el ticket -> EN_PROCESO
        ticketService.atenderTicket(id, agente);
        Ticket tEnProceso = ticketRepo.buscarPorId(id).orElseThrow();
        assertEquals("EN_PROCESO", tEnProceso.getEstado().nombre());

        // 2. Agente resuelve el ticket -> RESUELTO
        ticketService.resolverTicket(id, agente);
        Ticket tResuelto = ticketRepo.buscarPorId(id).orElseThrow();
        assertEquals("RESUELTO", tResuelto.getEstado().nombre());

        // 3. Solicitante confirma y cierra -> CERRADO
        ticketService.cerrarTicket(id, solicitante);
        Ticket tCerrado = ticketRepo.buscarPorId(id).orElseThrow();
        assertEquals("CERRADO", tCerrado.getEstado().nombre());
    }

    @Test
    @DisplayName("Agregar comentario incrementa la lista de comentarios del ticket")
    public void testAgregarComentario() {
        Usuario solicitante = usuarioRepo.buscarPorCorreo("solicitante@cimm.edu").orElseThrow();

        Ticket t = new Ticket();
        t.setTitulo("Prueba comentario");
        t.setDescripcion("Detalle...");
        t.setSolicitante(solicitante);

        Ticket creado = ticketService.crearTicket(t, 3L);
        Long id = (long) creado.getId();

        ticketService.agregarComentario(id, "Primera observación sobre el ticket", solicitante);

        Ticket tActualizado = ticketRepo.buscarPorId(id).orElseThrow();
        assertEquals(1, tActualizado.getComentarios().size());
        assertEquals("Primera observación sobre el ticket", tActualizado.getComentarios().get(0).getTexto());
    }
}
