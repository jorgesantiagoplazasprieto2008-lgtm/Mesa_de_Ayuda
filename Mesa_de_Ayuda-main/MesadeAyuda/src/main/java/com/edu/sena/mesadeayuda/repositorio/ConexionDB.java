package com.edu.sena.mesadeayuda.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestor de conexión JDBC para MySQL.
 */
public class ConexionDB {
    private static final Logger LOGGER = Logger.getLogger(ConexionDB.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/mesa_ayuda_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";
    private static boolean driverCargado = false;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverCargado = true;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.FINE, "Driver JDBC de MySQL no encontrado en runtime");
        }
    }

    public static Connection getConexion() throws SQLException {
        if (!driverCargado) {
            throw new SQLException("El driver JDBC com.mysql.cj.jdbc.Driver no está disponible en el classpath.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
