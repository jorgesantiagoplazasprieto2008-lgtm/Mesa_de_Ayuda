package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sagi
 */
public class CategoriaRepositoryJdbc implements CategoriaRepository {
     private static final Logger LOGGER = Logger.getLogger(CategoriaRepositoryJdbc.class.getName());
     
    @Override
    public Categoria guardar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getLong(1));
                }
            }
            return categoria;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar categoría en JDBC", e);
            throw new RuntimeException("Error en BD al guardar categoría", e);
        }
    }
    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Categoria(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar categoría por ID", e);
        }
        return Optional.empty();
    }
    
     @Override
    public List<Categoria> obtenerTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todas las categorías", e);
        }
        return lista;
    }
}

