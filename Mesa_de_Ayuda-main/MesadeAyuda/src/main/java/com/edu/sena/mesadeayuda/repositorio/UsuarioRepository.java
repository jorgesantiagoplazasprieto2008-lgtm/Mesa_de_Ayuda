package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Rol;
import com.edu.sena.mesadeayuda.Modelo.Usuario;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Sagi
 */
public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorCorreo(String correo);
    List<Usuario> buscarPorRol(Rol rol);
    List<Usuario> obtenerTodos();
}
