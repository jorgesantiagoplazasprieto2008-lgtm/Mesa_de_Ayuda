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

public class ConfiguracionServlet extends HttpServlet {

    private UsuarioRepository usuarioRepo;

    @Override
    public void init() throws ServletException {
        this.usuarioRepo = (UsuarioRepository) getServletContext().getAttribute("usuarioRepository");
        if (this.usuarioRepo == null) this.usuarioRepo = new UsuarioRepositoryMemoria();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/Configuracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("actualizarPerfil".equals(action)) {
            String nuevoNombre = request.getParameter("nombre");
            String nuevaPassword = request.getParameter("password");
            String tema = request.getParameter("tema");

            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                usuario.setNombre(nuevoNombre.trim());
            }
            if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
                usuario.setPassword(nuevaPassword.trim());
            }
            if (tema != null && !tema.trim().isEmpty()) {
                session.setAttribute("temaModo", tema.trim());
            }

            usuarioRepo.guardar(usuario);
            session.setAttribute("usuarioLogueado", usuario);
            request.setAttribute("mensajeExito", "Configuración, contraseña y preferencias de tema actualizadas correctamente.");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/Configuracion.jsp").forward(request, response);
    }
}
