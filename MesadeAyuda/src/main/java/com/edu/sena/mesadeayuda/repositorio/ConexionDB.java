package com.edu.sena.mesadeayuda.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sagi
 */
public class ConexionDB {
    private static final Logger LOGGER = Logger.getLogger(ConexionDB.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/mesa_ayuda_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar el driver de MySQL", e);
            throw new RuntimeException("No se encontró el driver JDBC de MySQL", e);
        }
    }
    
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
