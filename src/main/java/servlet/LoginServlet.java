package servlet;

import dao.UsuarioDAO;
import model.Usuario;
import util.Roles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) != null && request.getSession().getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        try {
            Usuario u = usuarioDAO.autenticar(usuario != null ? usuario.trim() : "", password != null ? password : "");
            if (u != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", u.getUsuario());
                session.setAttribute("usuarioId", u.getId());
                session.setAttribute("rol", u.getRol());
                session.setAttribute("nombreCompleto", u.getNombreCompleto() != null ? u.getNombreCompleto() : u.getUsuario());
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            // Verificar si existe pero está pendiente
            Usuario pendiente = usuarioDAO.buscarPorUsuario(usuario != null ? usuario.trim() : "");
            if (pendiente != null && pendiente.isPending()) {
                request.setAttribute("error", "Tu cuenta está pendiente de aprobación. Contacta al administrador.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
