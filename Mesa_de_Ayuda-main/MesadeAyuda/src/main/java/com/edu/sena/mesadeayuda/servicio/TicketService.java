package com.edu.sena.mesadeayuda.servicio;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.Modelo.estado.*;
import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.asignacion.*;
import com.edu.sena.mesadeayuda.servicio.notificacion.*;
import com.edu.sena.mesadeayuda.servicio.prioridad.*;
import com.edu.sena.mesadeayuda.servicio.sla.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Servicio de Negocio centralizado para la gestión de tickets y notificaciones (SOLID: SRP, OCP, LSP, ISP y DIP).
 */
public class TicketService {

    private static final Logger LOGGER = Logger.getLogger(TicketService.class.getName());

    private final TicketRepository ticketRepo;
    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;
    private final ComentarioRepository comentarioRepo;
    private final EstrategiaAsignacion estrategiaAsignacion;
    private final Notificador notificador;
    private final EstrategiaPrioridad estrategiaPrioridad;

    public TicketService(TicketRepository ticketRepo, UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo) {
        this(ticketRepo, usuarioRepo, categoriaRepo, new ComentarioRepositoryMemoria());
    }

    public TicketService(TicketRepository ticketRepo, UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo, ComentarioRepository comentarioRepo) {
        this(ticketRepo, usuarioRepo, categoriaRepo, comentarioRepo,
                new AsignacionPorCategoria(),
                crearNotificadorPorDefecto(),
                new PriorizacionPalabrasClave());
    }

    public TicketService(TicketRepository ticketRepo,
                         UsuarioRepository usuarioRepo,
                         CategoriaRepository categoriaRepo,
                         ComentarioRepository comentarioRepo,
                         EstrategiaAsignacion estrategiaAsignacion,
                         Notificador notificador,
                         EstrategiaPrioridad estrategiaPrioridad) {
        this.ticketRepo = ticketRepo;
        this.usuarioRepo = usuarioRepo;
        this.categoriaRepo = categoriaRepo;
        this.comentarioRepo = comentarioRepo != null ? comentarioRepo : new ComentarioRepositoryMemoria();
        this.estrategiaAsignacion = estrategiaAsignacion != null ? estrategiaAsignacion : new AsignacionPorCategoria();
        this.notificador = notificador != null ? notificador : crearNotificadorPorDefecto();
        this.estrategiaPrioridad = estrategiaPrioridad != null ? estrategiaPrioridad : new PriorizacionPalabrasClave();
    }

    private static Notificador crearNotificadorPorDefecto() {
        NotificadorCompuesto notifier = new NotificadorCompuesto();
        notifier.agregarNotificador(new NotificacionConsola());
        notifier.agregarNotificador(new NotificacionEmailMock());
        notifier.agregarNotificador(new NotificacionEnApp());
        return notifier;
    }

    public Optional<Ticket> buscarTicketPorId(Long id) {
        if (id == null) return Optional.empty();
        Optional<Ticket> opt = ticketRepo.buscarPorId(id);
        opt.ifPresent(t -> {
            List<Comentario> comentariosBD = comentarioRepo.buscarPorTicket(id);
            t.getComentarios().clear();
            t.getComentarios().addAll(comentariosBD);
        });
        return opt;
    }

    public Ticket crearTicket(Ticket ticket, Long categoriaId) {
        if (categoriaId != null) {
            categoriaRepo.buscarPorId(categoriaId).ifPresent(ticket::setCategoria);
        }

        Prioridad prioridad = estrategiaPrioridad.determinarPrioridad(ticket);
        ticket.setPrioridad(prioridad);
        ticket.setEstado(new EstadoNuevo());
        ticket.setFechaCreacion(LocalDateTime.now());

        EstrategiaSLA estrategiaSLA = CataloguerSLA.obtenerEstrategia(ticket.getPrioridad());
        ticket.setFechaLimiteSLA(estrategiaSLA.calcularFechaLimite(ticket.getFechaCreacion()));

        List<Usuario> agentes = usuarioRepo.buscarPorRol(Rol.AGENTE);
        Optional<Usuario> agenteAsignado = estrategiaAsignacion.seleccionarAgente(ticket, agentes);
        if (agenteAsignado.isPresent()) {
            ticket.asignarAgente(agenteAsignado.get());
        }

        Ticket ticketGuardado = ticketRepo.guardar(ticket);

        // Notificación por correo y en-app al solicitante
        notificador.notificar(ticketGuardado.getSolicitante(), ticketGuardado,
                "Tu ticket #" + ticketGuardado.getId() + " ('" + ticketGuardado.getTitulo() + "') ha sido registrado exitosamente.");

        // Notificación emergente e-mail/en-app al agente asignado
        if (ticketGuardado.getAgente() != null) {
            notificador.notificar(ticketGuardado.getAgente(), ticketGuardado,
                    "Se te ha asignado el ticket #" + ticketGuardado.getId() + " ('" + ticketGuardado.getTitulo() + "') para su gestión.");
        }

        return ticketGuardado;
    }

    public void atenderTicket(Long ticketId, Usuario usuarioAccion) {
        buscarTicketPorId(ticketId).ifPresent(t -> {
            if (esAgenteAsignadoOAdmin(t, usuarioAccion)) {
                t.setEstado(t.getEstado().iniciar());
                ticketRepo.actualizar(t);

                String agenteNombre = usuarioAccion.getNombre();
                notificador.notificar(t.getSolicitante(), t,
                        "¡Tu ticket #" + t.getId() + " ('" + t.getTitulo() + "') está ahora EN PROCESO! El agente " + agenteNombre + " comenzó a atenderlo.");
                
                if (t.getAgente() != null) {
                    notificador.notificar(t.getAgente(), t,
                            "Has iniciado la atención del ticket #" + t.getId() + " ('" + t.getTitulo() + "').");
                }
            } else {
                throw new SecurityException("No tiene permisos para iniciar la atención de este ticket.");
            }
        });
    }

    public void resolverTicket(Long ticketId, Usuario usuarioAccion) {
        buscarTicketPorId(ticketId).ifPresent(t -> {
            if (esAgenteAsignadoOAdmin(t, usuarioAccion)) {
                t.setEstado(t.getEstado().resolver());
                ticketRepo.actualizar(t);

                notificador.notificar(t.getSolicitante(), t,
                        "¡Tu ticket #" + t.getId() + " ('" + t.getTitulo() + "') ha sido marcado como RESUELTO! Por favor confirma la solución o reábrelo.");
                
                if (t.getAgente() != null) {
                    notificador.notificar(t.getAgente(), t,
                            "Has marcado como RESUELTO el ticket #" + t.getId() + " ('" + t.getTitulo() + "').");
                }
            } else {
                throw new SecurityException("No tiene permisos para resolver este ticket.");
            }
        });
    }

    public void cerrarTicket(Long ticketId, Usuario solicitante) {
        buscarTicketPorId(ticketId).ifPresent(t -> {
            if (t.getSolicitante() != null && t.getSolicitante().getId().equals(solicitante.getId()) || solicitante.getRol() == Rol.ADMIN) {
                t.setEstado(t.getEstado().cerrar());
                ticketRepo.actualizar(t);

                notificador.notificar(t.getSolicitante(), t,
                        "Tu ticket #" + t.getId() + " ('" + t.getTitulo() + "') ha sido CERRADO exitosamente. Gracias por tu retroalimentación.");

                if (t.getAgente() != null) {
                    notificador.notificar(t.getAgente(), t,
                            "El solicitante ha verificado y CERRADO el ticket #" + t.getId() + " ('" + t.getTitulo() + "').");
                }
            } else {
                throw new SecurityException("Solo el solicitante original o un Administrador pueden cerrar el ticket.");
            }
        });
    }

    public void reabrirTicket(Long ticketId, Usuario usuarioAccion) {
        buscarTicketPorId(ticketId).ifPresent(t -> {
            boolean esAdmin = usuarioAccion != null && usuarioAccion.getRol() == Rol.ADMIN;
            boolean esSolicitante = t.getSolicitante() != null && usuarioAccion != null && t.getSolicitante().getId().equals(usuarioAccion.getId());
            boolean estaCerrado = t.getEstado() != null && "CERRADO".equalsIgnoreCase(t.getEstado().nombre());

            if (estaCerrado && !esAdmin) {
                throw new SecurityException("Solo un Administrador puede reabrir un ticket que ya ha sido CERRADO.");
            }

            if (esSolicitante || esAdmin) {
                t.setEstado(t.getEstado().reabrir());
                ticketRepo.actualizar(t);

                notificador.notificar(t.getSolicitante(), t,
                        "El ticket #" + t.getId() + " ('" + t.getTitulo() + "') fue REABIERTO. Volverá a estar EN PROCESO para su atención.");

                if (t.getAgente() != null) {
                    notificador.notificar(t.getAgente(), t,
                            "¡Atención! El ticket #" + t.getId() + " ('" + t.getTitulo() + "') fue REABIERTO.");
                }
            } else {
                throw new SecurityException("No tiene permisos para reabrir este ticket.");
            }
        });
    }

    public void cancelarTicket(Long ticketId, Usuario admin) {
        if (admin.getRol() != Rol.ADMIN) {
            throw new SecurityException("Solo un Administrador puede cancelar tickets.");
        }
        buscarTicketPorId(ticketId).ifPresent(t -> {
            t.setEstado(t.getEstado().cancelar());
            ticketRepo.actualizar(t);

            notificador.notificar(t.getSolicitante(), t,
                    "El ticket #" + t.getId() + " ('" + t.getTitulo() + "') fue CANCELADO por el Administrador.");

            if (t.getAgente() != null) {
                notificador.notificar(t.getAgente(), t,
                        "El ticket #" + t.getId() + " ('" + t.getTitulo() + "') fue CANCELADO por el Administrador.");
            }
        });
    }

    public void reasignarTicket(Long ticketId, Long nuevoAgenteId, Usuario admin) {
        if (admin.getRol() != Rol.ADMIN) {
            throw new SecurityException("Solo un Administrador puede reasignar tickets.");
        }

        buscarTicketPorId(ticketId).ifPresent(t -> {
            usuarioRepo.buscarPorId(nuevoAgenteId).ifPresent(nuevoAgente -> {
                t.asignarAgente(nuevoAgente);
                ticketRepo.actualizar(t);

                notificador.notificar(nuevoAgente, t,
                        "¡Alerta! Se te ha ASIGNADO el ticket #" + t.getId() + " ('" + t.getTitulo() + "') por el Administrador.");

                notificador.notificar(t.getSolicitante(), t,
                        "Tu ticket #" + t.getId() + " ('" + t.getTitulo() + "') fue reasignado al agente " + nuevoAgente.getNombre() + ".");
            });
        });
    }

    public void agregarComentario(Long ticketId, String texto, Usuario autor) {
        if (ticketId == null || texto == null || texto.trim().isEmpty() || autor == null) return;
        buscarTicketPorId(ticketId).ifPresent(t -> {
            Comentario c = new Comentario(autor, texto.trim(), LocalDateTime.now());
            comentarioRepo.guardar(ticketId, c);
            t.agregarComentario(c);
            ticketRepo.actualizar(t);
            LOGGER.info("Comentario agregado al ticket #" + ticketId + " por " + autor.getNombre());
        });
    }

    public List<Ticket> obtenerTicketsFiltrados(Usuario usuario, String estado, String prioridad, Long categoriaId) {
        List<Ticket> base;
        switch (usuario.getRol()) {
            case SOLICITANTE:
                base = ticketRepo.buscarPorSolicitante(usuario.getId());
                break;
            case AGENTE:
                base = ticketRepo.buscarPorAgente(usuario.getId());
                break;
            case ADMIN:
            default:
                base = ticketRepo.obtenerTodos();
                break;
        }

        return base.stream()
                .filter(t -> (estado == null || estado.isEmpty() || (t.getEstado() != null && t.getEstado().nombre().equalsIgnoreCase(estado))))
                .filter(t -> (prioridad == null || prioridad.isEmpty() || (t.getPrioridad() != null && t.getPrioridad().name().equalsIgnoreCase(prioridad))))
                .filter(t -> (categoriaId == null || (t.getCategoria() != null && categoriaId.equals(t.getCategoria().getId()))))
                .collect(Collectors.toList());
    }

    private boolean esAgenteAsignadoOAdmin(Ticket ticket, Usuario usuario) {
        if (usuario == null) return false;
        if (usuario.getRol() == Rol.ADMIN) return true;
        return ticket.getAgente() != null && ticket.getAgente().getId().equals(usuario.getId());
    }
}
