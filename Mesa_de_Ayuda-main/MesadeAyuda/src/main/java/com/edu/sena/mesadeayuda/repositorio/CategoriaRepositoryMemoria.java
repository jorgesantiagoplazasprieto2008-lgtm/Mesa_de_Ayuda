package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositorio en memoria de categorías.
 */
public class CategoriaRepositoryMemoria implements CategoriaRepository {

    private final Map<Long, Categoria> categoriaMap = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    public CategoriaRepositoryMemoria() {
        guardar(new Categoria(null, "Red", "Problemas de conectividad, WiFi y cableado de red"));
        guardar(new Categoria(null, "Hardware", "Daños de equipos, impresoras, mouses y pantallas"));
        guardar(new Categoria(null, "Software", "Instalación y fallas de sistemas operativos y programas"));
        guardar(new Categoria(null, "Mantenimiento", "Mantenimiento preventivo y correctivo de maquinaria"));
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        if (categoria.getId() == null) {
            categoria.setId(idSequence.incrementAndGet());
        }
        categoriaMap.put(categoria.getId(), categoria);
        return categoria;
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(categoriaMap.get(id));
    }

    @Override
    public List<Categoria> obtenerTodas() {
        return new ArrayList<>(categoriaMap.values());
    }
}
