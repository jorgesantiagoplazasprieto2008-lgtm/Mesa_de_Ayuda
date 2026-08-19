package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Rol;
import com.edu.sena.mesadeayuda.Modelo.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria de usuarios (DIP / OCP / Liskov Substitution Principle).
 */
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final Map<Long, Usuario> usuariosMap = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public UsuarioRepositoryMemoria() {
        // Inicializar usuarios semilla por defecto
        guardar(new Usuario(null, "Juan Perez (Solicitante)", "solicitante@cimm.edu", "12345", Rol.SOLICITANTE));
        guardar(new Usuario(null, "Carlos Rodriguez (Agente)", "agente@cimm.edu", "12345", Rol.AGENTE));
        guardar(new Usuario(null, "Ana Martinez (Agente Red)", "ana@cimm.edu", "12345", Rol.AGENTE));
        guardar(new Usuario(null, "Luis Gomez (Agente Software)", "luis@cimm.edu", "12345", Rol.AGENTE));
        guardar(new Usuario(null, "Administrador SENA", "admin@cimm.edu", "12345", Rol.ADMIN));
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(idSequence.incrementAndGet());
        }
        usuariosMap.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(usuariosMap.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null) return Optional.empty();
        return usuariosMap.values().stream()
                .filter(u -> correo.equalsIgnoreCase(u.getCorreo()))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarPorRol(Rol rol) {
        if (rol == null) return new ArrayList<>();
        return usuariosMap.values().stream()
                .filter(u -> u.getRol() == rol)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuariosMap.values());
    }
}
