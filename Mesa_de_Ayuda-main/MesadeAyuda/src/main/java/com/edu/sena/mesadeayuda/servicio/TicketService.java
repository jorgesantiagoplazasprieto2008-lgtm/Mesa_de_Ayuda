package com.edu.sena.mesadeayuda.servicio;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.Modelo.estado.*;
import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.asignacion.*;
import com.edu.sena.mesadeayuda.servicio.notificacion.*;
import com.edu.sena.mesadeayuda.servicio.sla.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TicketService {
    
    private static final Logger LOGGER = Logger.getLogger(TicketService.class.getName());
    
    private final TicketRepository ticketRepo;
    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;
    private final EstrategiaAsignacion estrategiaAsignacion;
    private final Notificador notificador;

    public TicketService(TicketRepository ticketRepo, UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo) {
        this.ticketRepo = ticketRepo;
        this.usuarioRepo = usuarioRepo;
        this.categoriaRepo = categoriaRepo;
        // Estrategias inyectadas/seleccionadas (DIP y OCP)
        this.estrategiaAsignacion = new AsignacionPorCategoria();
        NotificadorCompuesto notifier = new NotificadorCompuesto();
        notifier.agregarNotificador(new NotificacionConsola());
        this.notificador = notifier;
    }

    public void crearTicket(Ticket ticket, Long categoriaId) {
        // 1. Asignar Categoría
        categoriaRepo.buscarPorId(categoriaId).ifPresent(ticket::setCategoria);

        // 2. Establecer Prioridad Automática (Basado en la categoría para simular reglas de negocio)
        if (ticket.getCategoria() != null) {
            String catName = ticket.getCategoria().getNombre().toLowerCase();
            if (catName.contains("red")) ticket.setPrioridad(Prioridad.MEDIA);
            else if (catName.contains("hardware")) ticket.setPrioridad(Prioridad.ALTA);
            else if (catName.contains("software")) ticket.setPrioridad(Prioridad.BAJA);
            else ticket.setPrioridad(Prioridad.MEDIA);
        } else {
            ticket.setPrioridad(Prioridad.MEDIA);
        }

        // 3. Patrón State: Estado inicial
        ticket.setEstado(new EstadoNuevo());
        ticket.setFechaCreacion(LocalDateTime.now());

        // 4. Patrón Strategy (SLA): Calcular tiempo límite
        EstrategiaSLA estrategiaSLA = CataloguerSLA.obtenerEstrategia(ticket.getPrioridad());
        ticket.setFechaLimiteSLA(estrategiaSLA.calcularFechaLimite(ticket.getFechaCreacion()));

        // 5. Patrón Strategy (Asignación Automática)
        List<Usuario> agentes = usuarioRepo.buscarPorRol(Rol.AGENTE);
        Optional<Usuario> agenteAsignado = estrategiaAsignacion.seleccionarAgente(ticket, agentes);
        
        if (agenteAsignado.isPresent()) {
            ticket.asignarAgente(agenteAsignado.get()); // Cambia a EstadoAsignado internamente usando el State pattern
        }

        // Guardar
        ticketRepo.guardar(ticket);

        // 6. Patrón OCP / Strategy (Notificación)
        notificador.notificar(ticket.getSolicitante(), ticket, "Tu ticket ha sido creado y está siendo procesado.");
        if (ticket.getAgente() != null) {
            notificador.notificar(ticket.getAgente(), ticket, "Se te ha asignado un nuevo ticket.");
        }
    }

    public void atenderTicket(Long ticketId, Usuario agenteLogueado) {
        ticketRepo.buscarPorId(ticketId).ifPresent(t -> {
            // Verificar permiso (solo el agente asignado o admin)
            if (t.getAgente() != null && t.getAgente().getId().equals(agenteLogueado.getId()) || agenteLogueado.getRol() == Rol.ADMIN) {
                t.setEstado(t.getEstado().iniciar());
                ticketRepo.actualizar(t);
                notificador.notificar(t.getSolicitante(), t, "Tu ticket está ahora EN PROCESO.");
            }
        });
    }

    public void resolverTicket(Long ticketId, Usuario agenteLogueado) {
        ticketRepo.buscarPorId(ticketId).ifPresent(t -> {
            if (t.getAgente() != null && t.getAgente().getId().equals(agenteLogueado.getId()) || agenteLogueado.getRol() == Rol.ADMIN) {
                t.setEstado(t.getEstado().resolver());
                ticketRepo.actualizar(t);
                notificador.notificar(t.getSolicitante(), t, "Tu ticket ha sido RESUELTO.");
            }
        });
    }

    public void reasignarTicket(Long ticketId, Long nuevoAgenteId, Usuario admin) {
        if (admin.getRol() != Rol.ADMIN) return;

        ticketRepo.buscarPorId(ticketId).ifPresent(t -> {
            usuarioRepo.buscarPorId(nuevoAgenteId).ifPresent(nuevoAgente -> {
                t.asignarAgente(nuevoAgente); // Pasa a Asignado
                ticketRepo.actualizar(t);
                notificador.notificar(nuevoAgente, t, "Se te ha reasignado este ticket por el Administrador.");
            });
        });
    }
}
