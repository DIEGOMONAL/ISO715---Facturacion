package servlet;

import dao.UsuarioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");

        String error = null;
        if (usuario == null || usuario.trim().isEmpty()) {
            error = "El usuario es obligatorio.";
        } else if (password == null || password.isEmpty()) {
            error = "La contraseña es obligatoria.";
        } else if (!password.equals(password2)) {
            error = "Las contraseñas no coinciden.";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
            return;
        }

        try {
            usuarioDAO.registrarPendiente(usuario.trim(), password);
            request.setAttribute("mensaje", "Tu usuario ha sido registrado y está pendiente de aprobación por un administrador.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

