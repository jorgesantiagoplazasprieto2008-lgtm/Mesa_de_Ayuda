package com.edu.sena.mesadeayuda.repositorio;

import com.edu.sena.mesadeayuda.Modelo.*;
import com.edu.sena.mesadeayuda.Modelo.estado.EstadoCataloguer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repositorio JDBC de Tickets con soporte completo para transacciones y comentarios.
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
            stmt.setLong(3, ticket.getCategoria() != null ? ticket.getCategoria().getId() : 1L);
            stmt.setString(4, ticket.getPrioridad() != null ? ticket.getPrioridad().name() : "MEDIA");
            stmt.setLong(5, ticket.getSolicitante() != null ? ticket.getSolicitante().getId() : 1L);
            if (ticket.getAgente() != null) {
                stmt.setLong(6, ticket.getAgente().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            stmt.setString(7, ticket.getEstado() != null ? ticket.getEstado().nombre() : "NUEVO");
            stmt.setTimestamp(8, Timestamp.valueOf(ticket.getFechaCreacion() != null ? ticket.getFechaCreacion() : LocalDateTime.now()));
            stmt.setTimestamp(9, ticket.getFechaLimiteSLA() != null ? Timestamp.valueOf(ticket.getFechaLimiteSLA()) : null);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt(1));
                }
            }
            guardarComentarios(conn, ticket);
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
                    Ticket t = mapearTicket(rs);
                    cargarComentarios(conn, t);
                    return Optional.of(t);
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
                    Ticket t = mapearTicket(rs);
                    cargarComentarios(conn, t);
                    lista.add(t);
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
                    Ticket t = mapearTicket(rs);
                    cargarComentarios(conn, t);
                    lista.add(t);
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
                Ticket t = mapearTicket(rs);
                cargarComentarios(conn, t);
                lista.add(t);
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
            stmt.setLong(3, ticket.getCategoria() != null ? ticket.getCategoria().getId() : 1L);
            stmt.setString(4, ticket.getPrioridad() != null ? ticket.getPrioridad().name() : "MEDIA");
            stmt.setLong(5, ticket.getSolicitante() != null ? ticket.getSolicitante().getId() : 1L);
            if (ticket.getAgente() != null) {
                stmt.setLong(6, ticket.getAgente().getId());
            } else {
                stmt.setNull(6, Types.BIGINT);
            }
            stmt.setString(7, ticket.getEstado() != null ? ticket.getEstado().nombre() : "NUEVO");
            stmt.setTimestamp(8, ticket.getFechaLimiteSLA() != null ? Timestamp.valueOf(ticket.getFechaLimiteSLA()) : null);
            stmt.setLong(9, ticket.getId());
            stmt.executeUpdate();

            guardarComentarios(conn, ticket);
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

    private void guardarComentarios(Connection conn, Ticket ticket) {
        if (ticket == null || ticket.getComentarios() == null || ticket.getComentarios().isEmpty()) return;

        String sqlTabla = "CREATE TABLE IF NOT EXISTS comentarios (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "ticket_id INT NOT NULL, " +
                "usuario_id BIGINT NOT NULL, " +
                "texto TEXT NOT NULL, " +
                "fecha DATETIME NOT NULL)";

        String sqlInsert = "INSERT INTO comentarios (ticket_id, usuario_id, texto, fecha) VALUES (?, ?, ?, ?)";

        try (Statement st = conn.createStatement()) {
            st.execute(sqlTabla);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "No se pudo asegurar la tabla de comentarios: " + e.getMessage());
        }

        for (Comentario c : ticket.getComentarios()) {
            if (c.getId() == null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, ticket.getId());
                    stmt.setLong(2, c.getAutor() != null ? c.getAutor().getId() : 1L);
                    stmt.setString(3, c.getTexto());
                    stmt.setTimestamp(4, Timestamp.valueOf(c.getFecha() != null ? c.getFecha() : LocalDateTime.now()));
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            c.setId(rs.getLong(1));
                        }
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error al insertar comentario en BD", e);
                }
            }
        }
    }

    private void cargarComentarios(Connection conn, Ticket ticket) {
        if (ticket == null) return;
        String sql = "SELECT id, usuario_id, texto, fecha FROM comentarios WHERE ticket_id = ? ORDER BY fecha ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticket.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long cId = rs.getLong("id");
                    Long uId = rs.getLong("usuario_id");
                    String texto = rs.getString("texto");
                    Timestamp fechaTs = rs.getTimestamp("fecha");
                    LocalDateTime fecha = fechaTs != null ? fechaTs.toLocalDateTime() : LocalDateTime.now();

                    // Verificar si ya existe en la lista para no duplicar
                    boolean existe = ticket.getComentarios().stream().anyMatch(c -> cId.equals(c.getId()));
                    if (!existe) {
                        Usuario autor = usuarioRepository.buscarPorId(uId).orElse(null);
                        Comentario c = new Comentario(cId, autor, texto, fecha);
                        ticket.agregarComentario(c);
                    }
                }
            }
        } catch (SQLException e) {
            // Si la tabla no existe aún, se ignora silenciosamente
            LOGGER.log(Level.FINE, "No se pudieron cargar comentarios del ticket #" + ticket.getId(), e);
        }
    }
}
