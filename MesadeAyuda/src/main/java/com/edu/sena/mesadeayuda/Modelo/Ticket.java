
package com.edu.sena.mesadeayuda.Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sagi
 */
public class Ticket {
    private int Id;
    private String titulo;
    private String descripcion;
    private Categoria Categoria;
    private Prioridad prioridad;
    private Usuario Solicitante;
    private Usuario Agente;
    private EstadoTicket estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLimiteSLA;
    private final List<Comentario> comentarios = new ArrayList<>();

    public Ticket(int Id, String titulo, String descripcion, Categoria Categoria, Prioridad prioridad, Usuario Solicitante, LocalDateTime fechaCreacion) {
        this.Id = Id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.Categoria = Categoria;
        this.prioridad = prioridad;
        this.Solicitante = Solicitante;
        this.fechaCreacion = fechaCreacion;
    } 

    public Ticket() {
    }            

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
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

    public Categoria getCategoria() {
        return Categoria;
    }

    public void setCategoria(Categoria Categoria) {
        this.Categoria = Categoria;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Usuario getSolicitante() {
        return Solicitante;
    }

    public void setSolicitante(Usuario Solicitante) {
        this.Solicitante = Solicitante;
    }

    public Usuario getAgente() {
        return Agente;
    }

    public void setAgente(Usuario Agente) {
        this.Agente = Agente;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
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
    
    public void agregarComentario(Comentario comentario) {
        if (comentario != null) this.comentarios.add(comentario);
    }
    
    public void asignarAgente(Usuario agente) {
        this.estado = this.estado.asignar();
        this.Agente = agente;
    }
    

    
    
    
    

    
}
