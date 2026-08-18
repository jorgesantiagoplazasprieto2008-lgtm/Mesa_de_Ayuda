package com.edu.sena.mesadeayuda.web;

import com.edu.sena.mesadeayuda.Modelo.Usuario;
import com.edu.sena.mesadeayuda.repositorio.UsuarioRepository;
import com.edu.sena.mesadeayuda.repositorio.UsuarioRepositoryJdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private UsuarioRepository usuarioRepo = new UsuarioRepositoryJdbc();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String correo = request.getParameter("email");
        String password = request.getParameter("password");

        Optional<Usuario> usuarioOpt = usuarioRepo.buscarPorCorreo(correo);

        // Si el usuario existe y la contraseña coincide
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuarioOpt.get());
            response.sendRedirect("DashboardServlet");
        } else {
            // Credenciales incorrectas
            response.sendRedirect("index.jsp?error=credenciales_invalidas");
        }
    }
}
