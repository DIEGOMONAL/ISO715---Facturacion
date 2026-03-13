package servlet;

import dao.UsuarioDAO;
import util.ConexionBD;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/usuarios")
public class UsuariosServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection con = ConexionBD.getConnection()) {
            ResultSet rs = usuarioDAO.listarTodos(con);
            request.setAttribute("usuariosRS", rs);
            request.getRequestDispatcher("/usuarios/listaUsuarios.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        int id = idStr != null ? Integer.parseInt(idStr) : 0;
        try {
            if ("aprobar".equals(action)) {
                usuarioDAO.cambiarEstado(id, "ACTIVO");
            } else if ("bloquear".equals(action)) {
                usuarioDAO.cambiarEstado(id, "INACTIVO");
            } else if ("rol".equals(action)) {
                String rol = request.getParameter("rol");
                usuarioDAO.cambiarRol(id, rol);
            }
            response.sendRedirect(request.getContextPath() + "/usuarios");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

