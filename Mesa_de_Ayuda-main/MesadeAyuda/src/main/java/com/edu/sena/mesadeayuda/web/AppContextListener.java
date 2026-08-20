package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.repositorio.*;
import com.edu.sena.mesadeayuda.servicio.TicketService;
import com.edu.sena.mesadeayuda.servicio.asignacion.AsignacionPorCategoria;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificacionConsola;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificacionEmailMock;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificacionEnApp;
import com.edu.sena.mesadeayuda.servicio.notificacion.NotificadorCompuesto;
import com.edu.sena.mesadeayuda.servicio.prioridad.PriorizacionPalabrasClave;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Composition Root de la aplicación Mesa de Ayuda SENA CIMM.
 */
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info(">>> Inicializando Composition Root de Mesa de Ayuda SENA CIMM (Jakarta EE) <<<");

        ServletContext sc = sce.getServletContext();

        UsuarioRepository usuarioRepo;
        CategoriaRepository categoriaRepo;
        TicketRepository ticketRepo;
        ComentarioRepository comentarioRepo;
        ChatRepository chatRepo;

        try {
            if (probarConexionDB()) {
                LOGGER.info("[PERSISTENCIA] Usando repositorios JDBC conectados a MySQL");
                usuarioRepo = new UsuarioRepositoryJdbc();
                categoriaRepo = new CategoriaRepositoryJdbc();
                ticketRepo = new TicketRepositoryJdbc(usuarioRepo, categoriaRepo);
                comentarioRepo = new ComentarioRepositoryJdbc(usuarioRepo);
                chatRepo = new ChatRepositoryJdbc();
            } else {
                LOGGER.info("[PERSISTENCIA FALLBACK] Activando repositorios en Memoria pre-poblados.");
                usuarioRepo = new UsuarioRepositoryMemoria();
                categoriaRepo = new CategoriaRepositoryMemoria();
                ticketRepo = new TicketRepositoryMemoria();
                comentarioRepo = new ComentarioRepositoryMemoria();
                chatRepo = new ChatRepositoryMemoria();
            }

            NotificadorCompuesto notificador = new NotificadorCompuesto();
            notificador.agregarNotificador(new NotificacionConsola());
            notificador.agregarNotificador(new NotificacionEmailMock());
            notificador.agregarNotificador(new NotificacionEnApp());

            TicketService ticketService = new TicketService(
                    ticketRepo,
                    usuarioRepo,
                    categoriaRepo,
                    comentarioRepo,
                    new AsignacionPorCategoria(),
                    notificador,
                    new PriorizacionPalabrasClave()
            );

            sc.setAttribute("usuarioRepository", usuarioRepo);
            sc.setAttribute("categoriaRepository", categoriaRepo);
            sc.setAttribute("ticketRepository", ticketRepo);
            sc.setAttribute("comentarioRepository", comentarioRepo);
            sc.setAttribute("chatRepository", chatRepo);
            sc.setAttribute("ticketService", ticketService);

            LOGGER.info(">>> Contexto de Aplicación configurado e inyectado con éxito <<<");
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Excepción no capturada en contextInitialized; aplicando repositorios defensivos en memoria.", t);
            try {
                usuarioRepo = new UsuarioRepositoryMemoria();
                categoriaRepo = new CategoriaRepositoryMemoria();
                ticketRepo = new TicketRepositoryMemoria();
                comentarioRepo = new ComentarioRepositoryMemoria();
                chatRepo = new ChatRepositoryMemoria();
                TicketService ticketService = new TicketService(ticketRepo, usuarioRepo, categoriaRepo, comentarioRepo);

                sc.setAttribute("usuarioRepository", usuarioRepo);
                sc.setAttribute("categoriaRepository", categoriaRepo);
                sc.setAttribute("ticketRepository", ticketRepo);
                sc.setAttribute("comentarioRepository", comentarioRepo);
                sc.setAttribute("chatRepository", chatRepo);
                sc.setAttribute("ticketService", ticketService);
            } catch (Throwable ignored) {
                LOGGER.log(Level.SEVERE, "Fallo crítico en repositorios defensivos", ignored);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info(">>> Destruyendo Contexto de la Aplicación Mesa de Ayuda SENA CIMM <<<");
    }

    private boolean probarConexionDB() {
        try (Connection conn = ConexionDB.getConexion()) {
            return conn != null && !conn.isClosed();
        } catch (Throwable e) {
            LOGGER.info("Prueba de conexión JDBC a MySQL no disponible (se utilizará persistencia en memoria): " + e.getMessage());
            return false;
        }
    }
}
