package com.edu.sena.mesadeayuda.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

/**
 * Composition Root de la aplicación Mesa de Ayuda.
 * Inicializa repositorios, servicios y estrategias, inyectando las dependencias (DIP)
 * y poniéndolas a disposición en el ServletContext.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info(">>> Inicializando Contexto de la Aplicación Mesa de Ayuda SENA CIMM <<<");
        // Aquí se instanciarán los repositorios y servicios inyectados por constructor (DIP)
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info(">>> Destruyendo Contexto de la Aplicación Mesa de Ayuda SENA CIMM <<<");
    }
}
