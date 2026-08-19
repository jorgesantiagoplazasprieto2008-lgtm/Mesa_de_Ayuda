package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.repositorio.UsuarioRepository;
import com.edu.sena.mesadeayuda.repositorio.UsuarioRepositoryMemoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    private UsuarioRepository usuarioRepo;

    @Override
    public void init() throws ServletException {
        this.usuarioRepo = (UsuarioRepository) getServletContext().getAttribute("usuarioRepository");
        if (this.usuarioRepo == null) {
            this.usuarioRepo = new UsuarioRepositoryMemoria();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email != null) email = email.trim();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorCorreo(email);

            if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", usuarioOpt.get());
                LOGGER.info("Inicio de sesión exitoso para: " + email);
                response.sendRedirect("DashboardServlet");
            } else {
                LOGGER.warning("Intento de inicio de sesión fallido para: " + email);
                response.sendRedirect("index.jsp?error=invalid");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en LoginServlet", e);
            response.sendRedirect("index.jsp?error=server");
        }
    }
}
