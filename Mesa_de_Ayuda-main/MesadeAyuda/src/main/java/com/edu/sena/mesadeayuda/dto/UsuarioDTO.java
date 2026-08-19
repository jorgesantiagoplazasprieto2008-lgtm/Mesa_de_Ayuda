package com.edu.sena.mesadeayuda.dto;

import com.edu.sena.mesadeayuda.Modelo.Rol;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correo;
    private Rol rol;

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nombre, String correo, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
