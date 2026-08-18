package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Categoria;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sagi
 */

public interface CategoriaRepository {
    Categoria guardar(Categoria categoria);
    Optional<Categoria> buscarPorId(Long id);
    List<Categoria> obtenerTodas();
}
