package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.*;
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
public class UsuarioRepositoryJdbc implements UsuarioRepository {
    private static final Logger LOGGER = Logger.getLogger(UsuarioRepositoryJdbc.class.getName());
    
    @Override
     public Usuario guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, password, rol) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getRol().name());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }
            return usuario;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar usuario en JDBC", e);
            throw new RuntimeException("Error en base de datos al guardar usuario", e);
        }
    }
     
     @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT id, nombre, correo, password, rol FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuario por ID", e);
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<Usuario> buscarPorCorreo(String correo){
        String sql = "SELECT id, nombre, correo, password, rol FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            } 
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepositoryJdbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Usuario> buscarPorRol(Rol rol) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, password, rol FROM usuarios WHERE rol = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar usuarios por rol", e);
        }
        return lista;
    }
    
    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, password, rol FROM usuarios";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los usuarios", e);
        }
        return lista;
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("password"),
                Rol.valueOf(rs.getString("rol"))
        );
    }
}
