package com.edu.sena.mesadeayuda.mapper;

import com.edu.sena.mesadeayuda.Modelo.Comentario;
import com.edu.sena.mesadeayuda.Modelo.Ticket;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.dto.ComentarioDTO;
import com.edu.sena.mesadeayuda.dto.TicketDTO;
import com.edu.sena.mesadeayuda.dto.UsuarioDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {

    public static TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTitulo(ticket.getTitulo());
        dto.setDescripcion(ticket.getDescripcion());
        if (ticket.getCategoria() != null) {
            dto.setCategoriaNombre(ticket.getCategoria().getNombre());
        }
        dto.setPrioridad(ticket.getPrioridad());
        if (ticket.getSolicitante() != null) {
            dto.setSolicitanteNombre(ticket.getSolicitante().getNombre());
        }
        if (ticket.getAgente() != null) {
            dto.setAgenteNombre(ticket.getAgente().getNombre());
        }
        if (ticket.getEstado() != null) {
            dto.setEstadoNombre(ticket.getEstado().nombre());
        }
        dto.setFechaCreacion(ticket.getFechaCreacion());
        dto.setFechaLimiteSLA(ticket.getFechaLimiteSLA());

        if (ticket.getComentarios() != null) {
            dto.setComentarios(ticket.getComentarios().stream()
                    .map(TicketMapper::toComentarioDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static ComentarioDTO toComentarioDTO(Comentario comentario) {
        if (comentario == null) return null;
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(comentario.getId());
        if (comentario.getAutor() != null) {
            dto.setAutorNombre(comentario.getAutor().getNombre());
        }
        dto.setTexto(comentario.getTexto());
        dto.setFecha(comentario.getFecha());
        return dto;
    }

    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol());
    }

    public static List<TicketDTO> toDTOList(List<Ticket> tickets) {
        if (tickets == null) return List.of();
        return tickets.stream().map(TicketMapper::toDTO).collect(Collectors.toList());
    }
}
