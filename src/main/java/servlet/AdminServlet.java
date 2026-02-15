package servlet;

import dao.AuditoriaDAO;
import dao.EmpresaDAO;
import dao.ImpuestoDAO;
import dao.UsuarioDAO;
import model.Auditoria;
import model.Empresa;
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
import java.util.List;

@WebServlet({"/admin", "/admin/*"})
public class AdminServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;
    private EmpresaDAO empresaDAO;
    private ImpuestoDAO impuestoDAO;
    private AuditoriaDAO auditoriaDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        empresaDAO = new EmpresaDAO();
        impuestoDAO = new ImpuestoDAO();
        auditoriaDAO = new AuditoriaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!verificarAdmin(request, response)) return;
        String path = request.getPathInfo();
        if (path == null) path = "/";
        try {
            switch (path) {
                case "/":
                case "":
                    request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
                    break;
                case "/pendientes":
                    List<Usuario> pendientes = usuarioDAO.listarPendientes();
                    request.setAttribute("pendientes", pendientes);
                    request.getRequestDispatcher("/WEB-INF/admin/usuariosPendientes.jsp").forward(request, response);
                    break;
                case "/usuarios":
                    List<Usuario> usuarios = usuarioDAO.listarTodos();
                    request.setAttribute("usuarios", usuarios);
                    request.getRequestDispatcher("/WEB-INF/admin/usuariosLista.jsp").forward(request, response);
                    break;
                case "/empresa":
                    Empresa emp = empresaDAO.obtener();
                    request.setAttribute("empresa", emp);
                    request.setAttribute("itbis", impuestoDAO.getPorcentajeITBIS());
                    request.getRequestDispatcher("/WEB-INF/admin/configEmpresa.jsp").forward(request, response);
                    break;
                case "/auditoria":
                    List<Auditoria> logs = auditoriaDAO.listar(200);
                    request.setAttribute("auditoria", logs);
                    request.getRequestDispatcher("/WEB-INF/admin/auditoria.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!verificarAdmin(request, response)) return;
        String path = request.getPathInfo();
        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("usuarioId");
        String adminNombre = (String) session.getAttribute("usuario");

        try {
            if ("/aprobar".equals(path)) {
                int id = Integer.parseInt(request.getParameter("id"));
                usuarioDAO.aprobar(id, adminId);
                auditoriaDAO.registrar(adminId, adminNombre, "APROBAR", "usuarios", id, "Usuario aprobado");
                response.sendRedirect(request.getContextPath() + "/admin/pendientes");
                return;
            }
            if ("/empresa".equals(path)) {
                Empresa e = new Empresa();
                e.setId(1);
                e.setRnc(request.getParameter("rnc"));
                e.setNombre(request.getParameter("nombre"));
                e.setDireccion(request.getParameter("direccion"));
                e.setTelefono(request.getParameter("telefono"));
                empresaDAO.guardar(e);
                try {
                    String itbisParam = request.getParameter("itbis");
                    if (itbisParam != null && !itbisParam.trim().isEmpty()) {
                        double itbis = Double.parseDouble(itbisParam.trim().replace(",", "."));
                        impuestoDAO.setPorcentajeITBIS(itbis);
                    }
                } catch (NumberFormatException ignored) {}
                auditoriaDAO.registrar(adminId, adminNombre, "EDITAR", "empresa", 1, "Configuración empresa e ITBIS");
                response.sendRedirect(request.getContextPath() + "/admin/empresa");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private boolean verificarAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Roles.ADMIN.equals(session.getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return false;
        }
        return true;
    }
}
