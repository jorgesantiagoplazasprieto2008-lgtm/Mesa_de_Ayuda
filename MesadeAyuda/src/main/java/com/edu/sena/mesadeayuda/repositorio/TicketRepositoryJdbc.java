package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.Modelo.estado.EstadoCataloguer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Usuario
 */
public class TicketRepositoryJdbc implements TicketRepository {
    private static final Logger LOGGER = Logger.getLogger(TicketRepositoryJdbc.class.getName());
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    public TicketRepositoryJdbc(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }
    
    @Override
    public Ticket guardar(Ticket ticket) {
        String sql = "INSERT INTO tickets (titulo, descripcion, categoria_id, prioridad, solicitante_id, agente_id, estado, fecha_creacion, fecha_limite_sla) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ticket.getTitulo());
            stmt.setString(2, ticket.getDescripcion());
            stmt.setLong(3, ticket.getCategoria().getId());
            stmt.setString(4, ticket.getPrioridad().name());
            stmt.setLong(5, ticket.getSolicitante().getId());
            if (ticket.getAgente() != null) {
                stmt.setLong(6, ticket.getAgente().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            stmt.setString(7, ticket.getEstado() != null ? ticket.getEstado().nombre() : "NUEVO");
            stmt.setTimestamp(8, Timestamp.valueOf(ticket.getFechaCreacion()));
            stmt.setTimestamp(9, ticket.getFechaLimiteSLA() != null ? Timestamp.valueOf(ticket.getFechaLimiteSLA()) : null);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt(1));
                }
            }
            return ticket;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar ticket en JDBC", e);
            throw new RuntimeException("Error en base de datos al guardar ticket", e);
        }
    }
    
    @Override
    public Optional<Ticket> buscarPorId(Long id) {
        String sql = "SELECT id, titulo, descripcion, categoria_id, prioridad, solicitante_id, agente_id, estado, fecha_creacion, fecha_limite_sla " +
                     "FROM tickets WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearTicket(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar ticket por ID", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Ticket> buscarPorSolicitante(Long solicitanteId) {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, categoria_id, prioridad, solicitante_id, agente_id, estado, fecha_creacion, fecha_limite_sla " +
                     "FROM tickets WHERE solicitante_id = ? ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, solicitanteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTicket(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar tickets por solicitante", e);
        }
        return lista;
    }
    
    @Override
    public List<Ticket> buscarPorAgente(Long agenteId) {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, categoria_id, prioridad, solicitante_id, agente_id, estado, fecha_creacion, fecha_limite_sla " +
                     "FROM tickets WHERE agente_id = ? ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, agenteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTicket(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar tickets por agente", e);
        }
        return lista;
    }
    
     @Override
    public List<Ticket> obtenerTodos() {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, categoria_id, prioridad, solicitante_id, agente_id, estado, fecha_creacion, fecha_limite_sla " +
                     "FROM tickets ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearTicket(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los tickets", e);
        }
        return lista;
    }
    
        @Override
    public void actualizar(Ticket ticket) {
        String sql = "UPDATE tickets SET titulo = ?, descripcion = ?, categoria_id = ?, prioridad = ?, solicitante_id = ?, " +
                     "agente_id = ?, estado = ?, fecha_limite_sla = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticket.getTitulo());
            stmt.setString(2, ticket.getDescripcion());
            stmt.setLong(3, ticket.getCategoria().getId());
            stmt.setString(4, ticket.getPrioridad().name());
            stmt.setLong(5, ticket.getSolicitante().getId());
            if (ticket.getAgente() != null) {
                stmt.setLong(6, ticket.getAgente().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            stmt.setString(7, ticket.getEstado() != null ? ticket.getEstado().nombre() : "NUEVO");
            stmt.setTimestamp(8, ticket.getFechaLimiteSLA() != null ? Timestamp.valueOf(ticket.getFechaLimiteSLA()) : null);
            stmt.setLong(9, ticket.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar ticket en JDBC", e);
            throw new RuntimeException("Error en BD al actualizar ticket", e);
        }
    }
    
        private Ticket mapearTicket(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setId(rs.getInt("id"));
        t.setTitulo(rs.getString("titulo"));
        t.setDescripcion(rs.getString("descripcion"));
        Long catId = rs.getLong("categoria_id");
        categoriaRepository.buscarPorId(catId).ifPresent(t::setCategoria);
        t.setPrioridad(Prioridad.valueOf(rs.getString("prioridad")));
        Long solId = rs.getLong("solicitante_id");
        usuarioRepository.buscarPorId(solId).ifPresent(t::setSolicitante);
        Long agId = rs.getLong("agente_id");
        if (!rs.wasNull() && agId != null) { usuarioRepository.buscarPorId(agId).ifPresent(t::setAgente);}
        t.setEstado(EstadoCataloguer.desdeNombre(rs.getString("estado")));
        Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
        if (tsCreacion != null) {
            t.setFechaCreacion(tsCreacion.toLocalDateTime());
        }
        Timestamp tsSla = rs.getTimestamp("fecha_limite_sla");
        if (tsSla != null) {
            t.setFechaLimiteSLA(tsSla.toLocalDateTime());
        }
        return t;
    }
}
