package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.Modelo.Rol;
import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.dto.NotificacionDTO;
import com.edu.sena.mesadeayuda.dto.TicketDTO;
import com.edu.sena.mesadeayuda.mapper.TicketMapper;
import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.TicketService;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificacionEnApp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TicketServlet.class.getName());

    private TicketRepository ticketRepo;
    private UsuarioRepository usuarioRepo;
    private CategoriaRepository categoriaRepo;
    private ComentarioRepository comentarioRepo;
    private TicketService ticketService;

    @Override
    public void init() throws ServletException {
        this.ticketRepo = (TicketRepository) getServletContext().getAttribute("ticketRepository");
        this.usuarioRepo = (UsuarioRepository) getServletContext().getAttribute("usuarioRepository");
        this.categoriaRepo = (CategoriaRepository) getServletContext().getAttribute("categoriaRepository");
        this.comentarioRepo = (ComentarioRepository) getServletContext().getAttribute("comentarioRepository");
        this.ticketService = (TicketService) getServletContext().getAttribute("ticketService");

        if (this.usuarioRepo == null) this.usuarioRepo = new UsuarioRepositoryMemoria();
        if (this.categoriaRepo == null) this.categoriaRepo = new CategoriaRepositoryMemoria();
        if (this.ticketRepo == null) this.ticketRepo = new TicketRepositoryMemoria();
        if (this.comentarioRepo == null) this.comentarioRepo = new ComentarioRepositoryMemoria();
        if (this.ticketService == null) {
            this.ticketService = new TicketService(ticketRepo, usuarioRepo, categoriaRepo, comentarioRepo);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<NotificacionDTO> notificaciones = NotificacionEnApp.obtenerYMarcarLeidas(usuario.getId());
        request.setAttribute("notificacionesAlert", notificaciones);

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "create":
                    if (usuario.getRol() != Rol.SOLICITANTE) {
                        response.sendRedirect("TicketServlet?action=list");
                        return;
                    }
                    request.setAttribute("categorias", categoriaRepo.obtenerTodas());
                    request.getRequestDispatcher("/WEB-INF/jsp/Ticket/create.jsp").forward(request, response);
                    break;

                case "detail":
                    String idStr = request.getParameter("id");
                    if (idStr != null) {
                        Long idDetalle = Long.parseLong(idStr);
                        Optional<Ticket> optTicket = ticketService.buscarTicketPorId(idDetalle);
                        if (optTicket.isPresent()) {
                            Ticket ticketObj = optTicket.get();
                            TicketDTO ticketDTO = TicketMapper.toDTO(ticketObj);
                            request.setAttribute("ticket", ticketDTO);
                            request.setAttribute("ticketDominio", ticketObj);
                            if (usuario.getRol() == Rol.ADMIN) {
                                request.setAttribute("agentes", usuarioRepo.buscarPorRol(Rol.AGENTE));
                            }
                        }
                    }
                    request.getRequestDispatcher("/WEB-INF/jsp/Ticket/detail.jsp").forward(request, response);
                    break;

                case "list":
                default:
                    String estadoFilter = request.getParameter("estado");
                    String prioridadFilter = request.getParameter("prioridad");
                    String catIdStr = request.getParameter("categoriaId");
                    Long catId = (catIdStr != null && !catIdStr.isEmpty()) ? Long.parseLong(catIdStr) : null;

                    List<Ticket> listaEntidades = ticketService.obtenerTicketsFiltrados(usuario, estadoFilter, prioridadFilter, catId);
                    List<TicketDTO> dtoList = TicketMapper.toDTOList(listaEntidades);

                    request.setAttribute("tickets", dtoList);
                    request.setAttribute("categorias", categoriaRepo.obtenerTodas());
                    request.setAttribute("estadoFiltro", estadoFilter);
                    request.setAttribute("prioridadFiltro", prioridadFilter);
                    request.setAttribute("categoriaFiltro", catId);

                    request.getRequestDispatcher("/WEB-INF/jsp/Ticket/list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en doGet TicketServlet", e);
            request.setAttribute("errorMsg", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/Ticket/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "insert":
                    if (usuario.getRol() == Rol.SOLICITANTE) {
                        String titulo = request.getParameter("titulo");
                        String descripcion = request.getParameter("descripcion");
                        Long categoriaId = Long.parseLong(request.getParameter("categoriaId"));

                        Ticket t = new Ticket();
                        t.setTitulo(titulo);
                        t.setDescripcion(descripcion);
                        t.setSolicitante(usuario);

                        ticketService.crearTicket(t, categoriaId);
                    }
                    response.sendRedirect("TicketServlet?action=list");
                    break;

                case "atender":
                    Long idAtender = Long.parseLong(request.getParameter("id"));
                    ticketService.atenderTicket(idAtender, usuario);
                    response.sendRedirect("TicketServlet?action=detail&id=" + idAtender);
                    break;

                case "resolver":
                    Long idResolver = Long.parseLong(request.getParameter("id"));
                    ticketService.resolverTicket(idResolver, usuario);
                    response.sendRedirect("TicketServlet?action=detail&id=" + idResolver);
                    break;

                case "cerrar":
                    Long idCerrar = Long.parseLong(request.getParameter("id"));
                    ticketService.cerrarTicket(idCerrar, usuario);
                    response.sendRedirect("TicketServlet?action=detail&id=" + idCerrar);
                    break;

                case "reabrir":
                    Long idReabrir = Long.parseLong(request.getParameter("id"));
                    ticketService.reabrirTicket(idReabrir, usuario);
                    response.sendRedirect("TicketServlet?action=detail&id=" + idReabrir);
                    break;

                case "cancelar":
                    Long idCancelar = Long.parseLong(request.getParameter("id"));
                    ticketService.cancelarTicket(idCancelar, usuario);
                    response.sendRedirect("TicketServlet?action=detail&id=" + idCancelar);
                    break;

                case "reasignar":
                    if (usuario.getRol() == Rol.ADMIN) {
                        Long idReasignar = Long.parseLong(request.getParameter("id"));
                        Long nuevoAgenteId = Long.parseLong(request.getParameter("agenteId"));
                        ticketService.reasignarTicket(idReasignar, nuevoAgenteId, usuario);
                        response.sendRedirect("TicketServlet?action=detail&id=" + idReasignar);
                    } else {
                        response.sendRedirect("TicketServlet?action=list");
                    }
                    break;

                case "comentar":
                    Long idComentar = Long.parseLong(request.getParameter("id"));
                    String textoComentario = request.getParameter("textoComentario");
                    ticketService.agregarComentario(idComentar, textoComentario, usuario);
                    response.sendRedirect("TicketServlet?action=detail&id=" + idComentar);
                    break;

                default:
                    response.sendRedirect("TicketServlet?action=list");
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al procesar acción " + action, e);
            String idRedirect = request.getParameter("id");
            if (idRedirect != null) {
                response.sendRedirect("TicketServlet?action=detail&id=" + idRedirect + "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
            } else {
                response.sendRedirect("TicketServlet?action=list&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
        }
    }
}
