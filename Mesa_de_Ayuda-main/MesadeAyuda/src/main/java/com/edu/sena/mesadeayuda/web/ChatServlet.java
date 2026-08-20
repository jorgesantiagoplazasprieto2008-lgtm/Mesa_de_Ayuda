package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.Modelo.MensajeChat;
import com.edu.sena.mesadeayuda.Modelo.Rol;
import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.dto.NotificacionDTO;
import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.TicketService;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificacionEnApp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controlador de Chat en Tiempo Real entre Solicitantes y Agentes Asignados.
 */
public class ChatServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChatServlet.class.getName());

    private ChatRepository chatRepo;
    private TicketService ticketService;
    private UsuarioRepository usuarioRepo;

    @Override
    public void init() throws ServletException {
        this.chatRepo = (ChatRepository) getServletContext().getAttribute("chatRepository");
        this.ticketService = (TicketService) getServletContext().getAttribute("ticketService");
        this.usuarioRepo = (UsuarioRepository) getServletContext().getAttribute("usuarioRepository");

        if (this.chatRepo == null) this.chatRepo = new ChatRepositoryMemoria();
        if (this.usuarioRepo == null) this.usuarioRepo = new UsuarioRepositoryMemoria();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // Cargar notificaciones emergentes
        List<NotificacionDTO> notificaciones = NotificacionEnApp.obtenerYMarcarLeidas(usuarioLogueado.getId());
        request.setAttribute("notificacionesAlert", notificaciones);

        String ajax = request.getParameter("ajax");
        String ticketIdStr = request.getParameter("ticketId");

        // 1. Petición AJAX para refresco automático en tiempo real
        if ("1".equals(ajax) && ticketIdStr != null && !ticketIdStr.isEmpty()) {
            Long ticketId = Long.parseLong(ticketIdStr);
            List<MensajeChat> mensajes = chatRepo.obtenerMensajesPorTicket(ticketId);
            chatRepo.marcarComoLeidos(ticketId, usuarioLogueado.getId());

            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                for (MensajeChat m : mensajes) {
                    boolean esMio = m.getEmisorId().equals(usuarioLogueado.getId());
                    out.println("<div class='d-flex " + (esMio ? "justify-content-end" : "justify-content-start") + " mb-3'>");
                    out.println("<div class='card shadow-sm " + (esMio ? "bg-primary text-white" : "bg-body-tertiary") + "' style='max-width: 75%; border-radius: 1rem;'>");
                    out.println("<div class='card-body py-2 px-3'>");
                    out.println("<div class='d-flex justify-content-between align-items-center mb-1 gap-2'>");
                    out.println("<strong class='small " + (esMio ? "text-white" : "text-primary") + "'><i class='fas fa-user-circle me-1'></i>" + escapeHtml(m.getEmisorNombre()) + " (" + m.getEmisorRol() + ")</strong>");
                    out.println("<small style='font-size: 0.7rem;' class='" + (esMio ? "text-white-50" : "text-muted") + "'>" + m.getFechaFormateada() + "</small>");
                    out.println("</div>");
                    out.println("<p class='mb-0' style='white-space: pre-wrap; font-size: 0.95rem;'>" + escapeHtml(m.getTexto()) + "</p>");
                    out.println("</div></div></div>");
                }
            }
            return;
        }

        // 2. Cargar vista completa de Chat
        List<Ticket> misTickets;
        if (usuarioLogueado.getRol() == Rol.SOLICITANTE) {
            misTickets = ticketService.obtenerTicketsFiltrados(usuarioLogueado, null, null, null).stream()
                    .filter(t -> t.getAgente() != null)
                    .collect(Collectors.toList());
        } else if (usuarioLogueado.getRol() == Rol.AGENTE) {
            misTickets = ticketService.obtenerTicketsFiltrados(usuarioLogueado, null, null, null);
        } else { // ADMIN
            misTickets = ticketService.obtenerTicketsFiltrados(usuarioLogueado, null, null, null).stream()
                    .filter(t -> t.getAgente() != null)
                    .collect(Collectors.toList());
        }

        request.setAttribute("ticketsConAgente", misTickets);

        Ticket ticketSeleccionado = null;
        if (ticketIdStr != null && !ticketIdStr.isEmpty()) {
            Long ticketId = Long.parseLong(ticketIdStr);
            Optional<Ticket> opt = ticketService.buscarTicketPorId(ticketId);
            if (opt.isPresent()) {
                ticketSeleccionado = opt.get();
            }
        } else if (!misTickets.isEmpty()) {
            ticketSeleccionado = misTickets.get(0);
        }

        if (ticketSeleccionado != null) {
            request.setAttribute("ticketSeleccionado", ticketSeleccionado);
            List<MensajeChat> historial = chatRepo.obtenerMensajesPorTicket((long) ticketSeleccionado.getId());
            chatRepo.marcarComoLeidos((long) ticketSeleccionado.getId(), usuarioLogueado.getId());
            request.setAttribute("historialMensajes", historial);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/Chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            Long ticketId = Long.parseLong(request.getParameter("ticketId"));
            String texto = request.getParameter("texto");

            if (texto != null && !texto.trim().isEmpty()) {
                Optional<Ticket> optT = ticketService.buscarTicketPorId(ticketId);
                if (optT.isPresent()) {
                    Ticket t = optT.get();
                    Long receptorId;
                    if (usuarioLogueado.getRol() == Rol.SOLICITANTE) {
                        receptorId = t.getAgente() != null ? t.getAgente().getId() : 0L;
                    } else {
                        receptorId = t.getSolicitante() != null ? t.getSolicitante().getId() : 0L;
                    }

                    MensajeChat msg = new MensajeChat(ticketId, usuarioLogueado.getId(), usuarioLogueado.getNombre(), usuarioLogueado.getRol().name(), receptorId, texto.trim());
                    chatRepo.guardar(msg);

                    // Notificación en-app si el destinatario no está en línea
                    if (receptorId > 0) {
                        usuarioRepo.buscarPorId(receptorId).ifPresent(dest -> {
                            NotificacionEnApp notifApp = new NotificacionEnApp();
                            notifApp.notificar(dest, t, "Nuevo mensaje de chat de " + usuarioLogueado.getNombre() + ": \"" + texto.trim() + "\"");
                        });
                    }
                }
            }

            response.sendRedirect("ChatServlet?ticketId=" + ticketId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al enviar mensaje de chat", e);
            response.sendRedirect("ChatServlet");
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
}
