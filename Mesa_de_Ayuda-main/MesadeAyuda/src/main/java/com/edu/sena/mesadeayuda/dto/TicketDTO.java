package com.edu.sena.mesadeayuda.dto;

import com.edu.sena.mesadeayuda.Modelo.Prioridad;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDTO {
    private int id;
    private String titulo;
    private String descripcion;
    private String categoriaNombre;
    private Prioridad prioridad;
    private String solicitanteNombre;
    private String agenteNombre;
    private String estadoNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLimiteSLA;
    private List<ComentarioDTO> comentarios = new ArrayList<>();

    public TicketDTO() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getSolicitanteNombre() {
        return solicitanteNombre;
    }

    public void setSolicitanteNombre(String solicitanteNombre) {
        this.solicitanteNombre = solicitanteNombre;
    }

    public String getAgenteNombre() {
        return agenteNombre;
    }

    public void setAgenteNombre(String agenteNombre) {
        this.agenteNombre = agenteNombre;
    }

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaLimiteSLA() {
        return fechaLimiteSLA;
    }

    public void setFechaLimiteSLA(LocalDateTime fechaLimiteSLA) {
        this.fechaLimiteSLA = fechaLimiteSLA;
    }

    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }
}
