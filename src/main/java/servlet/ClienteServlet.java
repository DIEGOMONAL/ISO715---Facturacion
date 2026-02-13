package servlet;

import dao.ClienteDAO;
import model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private ClienteDAO clienteDAO;

    @Override
    public void init() {
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";
        try {
            switch (action) {
                case "nuevo":
                    request.setAttribute("cliente", null);
                    request.getRequestDispatcher("/clientes/formCliente.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Cliente c = clienteDAO.obtenerPorId(idEditar);
                    request.setAttribute("cliente", c);
                    request.getRequestDispatcher("/clientes/formCliente.jsp").forward(request, response);
                    break;
                case "eliminar":
                    clienteDAO.eliminar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/clientes");
                    break;
                default:
                    String buscar = request.getParameter("buscar");
                    String ordenar = request.getParameter("ordenar");
                    String[] ord = parseOrdenar(ordenar, "nombre_comercial", "asc");
                    List<Cliente> lista = clienteDAO.listarConFiltros(buscar, ord[0], ord[1]);
                    request.setAttribute("listaClientes", lista);
                    request.setAttribute("buscar", buscar);
                    request.setAttribute("ordenar", ordenar != null ? ordenar : "nombre_comercial_asc");
                    request.getRequestDispatcher("/clientes/listaClientes.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("insertar".equals(action)) {
                Cliente c = new Cliente(
                        request.getParameter("nombreComercial"),
                        request.getParameter("rncCedula"),
                        request.getParameter("cuentaContable"),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                clienteDAO.insertar(c);
            } else if ("actualizar".equals(action)) {
                Cliente c = new Cliente(
                        Integer.parseInt(request.getParameter("id")),
                        request.getParameter("nombreComercial"),
                        request.getParameter("rncCedula"),
                        request.getParameter("cuentaContable"),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                clienteDAO.actualizar(c);
            }
            response.sendRedirect(request.getContextPath() + "/clientes");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String[] parseOrdenar(String ordenar, String defCol, String defDir) {
        if (ordenar == null || !ordenar.contains("_")) return new String[]{defCol, defDir};
        int i = ordenar.lastIndexOf("_");
        return new String[]{ordenar.substring(0, i), ordenar.substring(i + 1)};
    }
}
