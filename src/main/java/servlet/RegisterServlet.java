package servlet;

import dao.UsuarioDAO;
import model.Usuario;
import util.Roles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registro")
public class RegisterServlet extends HttpServlet {
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
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String rol = request.getParameter("rol");

        if (usuario == null || usuario.trim().isEmpty()) {
            request.setAttribute("error", "El usuario es obligatorio");
            forwardConDatos(request, response, usuario, nombreCompleto, rol);
            return;
        }
        if (password == null || password.length() < 4) {
            request.setAttribute("error", "La contraseña debe tener al menos 4 caracteres");
            forwardConDatos(request, response, usuario, nombreCompleto, rol);
            return;
        }
        if (!password.equals(password2)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            forwardConDatos(request, response, usuario, nombreCompleto, rol);
            return;
        }
        if (rol == null || !isRolValido(rol)) {
            rol = Roles.CAJERO;
        }

        try {
            if (usuarioDAO.existeUsuario(usuario.trim(), null)) {
                request.setAttribute("error", "El usuario ya existe. Elige otro.");
                forwardConDatos(request, response, usuario, nombreCompleto, rol);
                return;
            }
            Usuario u = new Usuario(usuario.trim(), password, rol);
            u.setNombreCompleto(nombreCompleto != null ? nombreCompleto.trim() : null);
            usuarioDAO.registrar(u);
            request.setAttribute("mensaje", "Registro exitoso. Tu cuenta está pendiente de aprobación por el administrador. Podrás iniciar sesión cuando sea aprobada.");
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void forwardConDatos(HttpServletRequest req, HttpServletResponse res, String usuario, String nombre, String rol) throws ServletException, IOException {
        req.setAttribute("usuarioVal", usuario);
        req.setAttribute("nombreVal", nombre);
        req.setAttribute("rolVal", rol);
        req.getRequestDispatcher("/registro.jsp").forward(req, res);
    }

    private boolean isRolValido(String r) {
        return Roles.ADMIN.equals(r) || Roles.CAJERO.equals(r) || Roles.SUPERVISOR.equals(r)
                || Roles.INVENTARIO.equals(r) || Roles.CONTADOR.equals(r);
    }
}
