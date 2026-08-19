package com.edu.sena.mesadeayuda.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComentarioDTO {
    private Long id;
    private String autorNombre;
    private String texto;
    private LocalDateTime fecha;

    public ComentarioDTO() {}

    public ComentarioDTO(Long id, String autorNombre, String texto, LocalDateTime fecha) {
        this.id = id;
        this.autorNombre = autorNombre;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutorNombre() {
        return autorNombre;
    }

    public void setAutorNombre(String autorNombre) {
        this.autorNombre = autorNombre;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getFechaFormateada() {
        if (fecha == null) return "";
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
