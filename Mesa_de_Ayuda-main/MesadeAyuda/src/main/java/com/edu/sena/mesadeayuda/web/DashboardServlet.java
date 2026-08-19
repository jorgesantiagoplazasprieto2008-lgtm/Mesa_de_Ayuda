package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.Modelo.Rol;
import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.dto.NotificacionDTO;
import com.edu.sena.mesadeayuda.dto.TicketDTO;
import com.edu.sena.mesadeayuda.mapper.TicketMapper;
import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificacionEnApp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardServlet extends HttpServlet {

    private TicketRepository ticketRepo;
    private UsuarioRepository usuarioRepo;

    @Override
    public void init() throws ServletException {
        this.ticketRepo = (TicketRepository) getServletContext().getAttribute("ticketRepository");
        this.usuarioRepo = (UsuarioRepository) getServletContext().getAttribute("usuarioRepository");
        if (this.ticketRepo == null) this.ticketRepo = new TicketRepositoryMemoria();
        if (this.usuarioRepo == null) this.usuarioRepo = new UsuarioRepositoryMemoria();
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

        List<Ticket> todos = ticketRepo.obtenerTodos();

        if (usuario.getRol() == Rol.SOLICITANTE) {
            List<Ticket> misTickets = ticketRepo.buscarPorSolicitante(usuario.getId());
            long total = misTickets.size();
            long pendientes = misTickets.stream().filter(t -> t.getEstado() != null && ("ASIGNADO".equalsIgnoreCase(t.getEstado().nombre()) || "NUEVO".equalsIgnoreCase(t.getEstado().nombre()))).count();
            long enProceso = misTickets.stream().filter(t -> t.getEstado() != null && "EN_PROCESO".equalsIgnoreCase(t.getEstado().nombre())).count();
            long resueltosOCerrados = misTickets.stream().filter(t -> t.getEstado() != null && ("RESUELTO".equalsIgnoreCase(t.getEstado().nombre()) || "CERRADO".equalsIgnoreCase(t.getEstado().nombre()))).count();

            request.setAttribute("stat1_label", "Total Solicitudes");
            request.setAttribute("stat1_val", total);
            request.setAttribute("stat2_label", "Pendientes (ASIGNADO)");
            request.setAttribute("stat2_val", pendientes);
            request.setAttribute("stat3_label", "En Atención (EN PROCESO)");
            request.setAttribute("stat3_val", enProceso);
            request.setAttribute("stat4_label", "Atendidos / Resueltos");
            request.setAttribute("stat4_val", resueltosOCerrados);

            List<TicketDTO> recientesDTO = TicketMapper.toDTOList(misTickets.stream().limit(5).collect(Collectors.toList()));
            request.setAttribute("ticketsRecientes", recientesDTO);

        } else if (usuario.getRol() == Rol.AGENTE) {
            List<Ticket> asignados = ticketRepo.buscarPorAgente(usuario.getId());
            long total = asignados.size();
            long pendientesAtencion = asignados.stream().filter(t -> t.getEstado() != null && "ASIGNADO".equalsIgnoreCase(t.getEstado().nombre())).count();
            long enProceso = asignados.stream().filter(t -> t.getEstado() != null && "EN_PROCESO".equalsIgnoreCase(t.getEstado().nombre())).count();
            long atendidosResueltosOCerrados = asignados.stream().filter(t -> t.getEstado() != null && ("RESUELTO".equalsIgnoreCase(t.getEstado().nombre()) || "CERRADO".equalsIgnoreCase(t.getEstado().nombre()))).count();

            request.setAttribute("stat1_label", "Total Tickets Asignados");
            request.setAttribute("stat1_val", total);
            request.setAttribute("stat2_label", "Pendientes (ASIGNADO)");
            request.setAttribute("stat2_val", pendientesAtencion);
            request.setAttribute("stat3_label", "En Atención (EN PROCESO)");
            request.setAttribute("stat3_val", enProceso);
            request.setAttribute("stat4_label", "Atendidos / Resueltos");
            request.setAttribute("stat4_val", atendidosResueltosOCerrados);

            List<TicketDTO> recientesDTO = TicketMapper.toDTOList(asignados.stream().limit(5).collect(Collectors.toList()));
            request.setAttribute("ticketsRecientes", recientesDTO);

        } else { // ADMIN
            long total = todos.size();
            long pendientes = todos.stream().filter(t -> t.getEstado() != null && ("NUEVO".equalsIgnoreCase(t.getEstado().nombre()) || "ASIGNADO".equalsIgnoreCase(t.getEstado().nombre()))).count();
            long enProceso = todos.stream().filter(t -> t.getEstado() != null && "EN_PROCESO".equalsIgnoreCase(t.getEstado().nombre())).count();
            long resueltosOCerrados = todos.stream().filter(t -> t.getEstado() != null && ("RESUELTO".equalsIgnoreCase(t.getEstado().nombre()) || "CERRADO".equalsIgnoreCase(t.getEstado().nombre()))).count();

            request.setAttribute("stat1_label", "Total Solicitudes");
            request.setAttribute("stat1_val", total);
            request.setAttribute("stat2_label", "Pendientes (ASIGNADO)");
            request.setAttribute("stat2_val", pendientes);
            request.setAttribute("stat3_label", "En Atención (EN PROCESO)");
            request.setAttribute("stat3_val", enProceso);
            request.setAttribute("stat4_label", "Atendidos / Resueltos");
            request.setAttribute("stat4_val", resueltosOCerrados);

            List<Usuario> agentesList = usuarioRepo.buscarPorRol(Rol.AGENTE);
            Map<String, Long> cargaAgentes = new HashMap<>();
            for (Usuario ag : agentesList) {
                long carga = todos.stream().filter(t -> t.getAgente() != null && ag.getId().equals(t.getAgente().getId())).count();
                cargaAgentes.put(ag.getNombre(), carga);
            }
            request.setAttribute("cargaAgentes", cargaAgentes);

            List<TicketDTO> recientesDTO = TicketMapper.toDTOList(todos.stream().limit(7).collect(Collectors.toList()));
            request.setAttribute("ticketsRecientes", recientesDTO);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/Dashboard.jsp").forward(request, response);
    }
}
