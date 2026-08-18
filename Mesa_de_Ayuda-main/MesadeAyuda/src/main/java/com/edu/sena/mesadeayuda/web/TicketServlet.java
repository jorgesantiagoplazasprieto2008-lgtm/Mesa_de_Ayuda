package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Rol;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.TicketService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TicketServlet", urlPatterns = {"/TicketServlet"})
public class TicketServlet extends HttpServlet {

    private TicketRepository ticketRepo;
    private UsuarioRepository usuarioRepo;
    private TicketService ticketService;

    @Override
    public void init() throws ServletException {
        this.usuarioRepo = new UsuarioRepositoryJdbc();
        CategoriaRepository cRepo = new CategoriaRepositoryJdbc();
        this.ticketRepo = new TicketRepositoryJdbc(usuarioRepo, cRepo);
        this.ticketService = new TicketService(ticketRepo, usuarioRepo, cRepo);
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

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "create":
                if (usuario.getRol() != Rol.SOLICITANTE) {
                    response.sendRedirect("TicketServlet?action=list"); // Solo el solicitante crea
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/jsp/Ticket/create.jsp").forward(request, response);
                break;
            case "detail":
                Long idDetalle = Long.parseLong(request.getParameter("id"));
                ticketRepo.buscarPorId(idDetalle).ifPresent(t -> request.setAttribute("ticket", t));
                if (usuario.getRol() == Rol.ADMIN) {
                    request.setAttribute("agentes", usuarioRepo.buscarPorRol(Rol.AGENTE));
                }
                request.getRequestDispatcher("/WEB-INF/jsp/Ticket/detail.jsp").forward(request, response);
                break;
            default:
                List<Ticket> tickets = new ArrayList<>();
                switch (usuario.getRol()) {
                    case SOLICITANTE:
                        tickets = ticketRepo.buscarPorSolicitante(usuario.getId());
                        break;
                    case AGENTE:
                        tickets = ticketRepo.buscarPorAgente(usuario.getId());
                        break;
                    case ADMIN:
                        tickets = ticketRepo.obtenerTodos();
                        break;
                }
                request.setAttribute("tickets", tickets);
                request.getRequestDispatcher("/WEB-INF/jsp/Ticket/list.jsp").forward(request, response);
                break;
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
        
        if ("insert".equals(action) && usuario.getRol() == Rol.SOLICITANTE) {
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            Long categoriaId = Long.parseLong(request.getParameter("categoriaId"));
            
            Ticket t = new Ticket();
            t.setTitulo(titulo);
            t.setDescripcion(descripcion);
            t.setSolicitante(usuario);
            
            ticketService.crearTicket(t, categoriaId);
            response.sendRedirect("TicketServlet?action=list");
            
        } else if ("atender".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            ticketService.atenderTicket(id, usuario);
            response.sendRedirect("TicketServlet?action=detail&id=" + id);
            
        } else if ("resolver".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            ticketService.resolverTicket(id, usuario);
            response.sendRedirect("TicketServlet?action=detail&id=" + id);
            
        } else if ("reasignar".equals(action) && usuario.getRol() == Rol.ADMIN) {
            Long id = Long.parseLong(request.getParameter("id"));
            Long nuevoAgenteId = Long.parseLong(request.getParameter("agenteId"));
            ticketService.reasignarTicket(id, nuevoAgenteId, usuario);
            response.sendRedirect("TicketServlet?action=detail&id=" + id);
            
        } else {
            response.sendRedirect("TicketServlet?action=list");
        }
    }
}
