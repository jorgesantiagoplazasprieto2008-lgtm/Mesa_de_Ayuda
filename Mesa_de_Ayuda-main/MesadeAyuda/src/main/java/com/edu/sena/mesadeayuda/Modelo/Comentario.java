package com.edu.sena.mesadeayuda.Modelo;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un comentario realizado sobre un ticket.
 */
public class Comentario {
    private Long id;
    private Usuario autor;
    private String texto;
    private LocalDateTime fecha;

    public Comentario() {}

    public Comentario(Long id, Usuario autor, String texto, LocalDateTime fecha) {
        this.id = id;
        this.autor = autor;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Comentario(Usuario autor, String texto, LocalDateTime fecha) {
        this.autor = autor;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
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
}
