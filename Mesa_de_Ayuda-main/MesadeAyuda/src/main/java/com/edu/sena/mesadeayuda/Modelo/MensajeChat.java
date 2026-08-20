package com.edu.sena.mesadeayuda.Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad del Modelo para el Chat de Soporte en Tiempo Real.
 */
public class MensajeChat {
    private Long id;
    private Long ticketId;
    private Long emisorId;
    private String emisorNombre;
    private String emisorRol;
    private Long receptorId;
    private String texto;
    private LocalDateTime fecha;
    private boolean leido;

    public MensajeChat() {
        this.fecha = LocalDateTime.now();
    }

    public MensajeChat(Long ticketId, Long emisorId, String emisorNombre, String emisorRol, Long receptorId, String texto) {
        this.ticketId = ticketId;
        this.emisorId = emisorId;
        this.emisorNombre = emisorNombre;
        this.emisorRol = emisorRol;
        this.receptorId = receptorId;
        this.texto = texto;
        this.fecha = LocalDateTime.now();
        this.leido = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long emisorId) {
        this.emisorId = emisorId;
    }

    public String getEmisorNombre() {
        return emisorNombre;
    }

    public void setEmisorNombre(String emisorNombre) {
        this.emisorNombre = emisorNombre;
    }

    public String getEmisorRol() {
        return emisorRol;
    }

    public void setEmisorRol(String emisorRol) {
        this.emisorRol = emisorRol;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
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

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getFechaFormateada() {
        if (fecha == null) return "";
        return fecha.format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM"));
    }
}
