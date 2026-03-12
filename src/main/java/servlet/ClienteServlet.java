package servlet;

import dao.ClienteDAO;
import model.Cliente;
import util.DocumentosRD;

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
        String nombreComercial = request.getParameter("nombreComercial");
        String rncCedula = request.getParameter("rncCedula");
        String cuentaContable = request.getParameter("cuentaContable");
        String estado = request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO";

        // Validaciones básicas de servidor para evitar datos inválidos
        String error = null;
        if (nombreComercial == null || nombreComercial.trim().isEmpty()) {
            error = "El nombre comercial es obligatorio.";
        } else if (nombreComercial.trim().length() > 255) {
            error = "El nombre comercial no puede tener más de 255 caracteres.";
        } else if (rncCedula == null || rncCedula.trim().isEmpty()) {
            error = "El RNC o Cédula es obligatorio.";
        } else if (!rncCedula.trim().matches("^[0-9\\-]{9,20}$")) {
            error = "El RNC o Cédula debe contener solo números y guiones, entre 9 y 20 caracteres.";
        } else if (!DocumentosRD.esRncOCedulaValido(rncCedula.trim())) {
            error = "El RNC o Cédula no es válido según las reglas de documentos de República Dominicana.";
        } else if (cuentaContable != null && cuentaContable.trim().length() > 50) {
            error = "La cuenta contable no puede tener más de 50 caracteres.";
        }

        if (error != null) {
            // Volver al formulario con el mensaje de error y los datos ingresados
            request.setAttribute("error", error);
            Cliente cForm = null;
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    cForm = new Cliente(id, nombreComercial, rncCedula, cuentaContable, estado);
                } catch (NumberFormatException ignored) {
                    cForm = new Cliente(nombreComercial, rncCedula, cuentaContable, estado);
                }
            } else {
                cForm = new Cliente(nombreComercial, rncCedula, cuentaContable, estado);
            }
            request.setAttribute("cliente", cForm);
            request.getRequestDispatcher("/clientes/formCliente.jsp").forward(request, response);
            return;
        }

        try {
            if ("insertar".equals(action)) {
                Cliente c = new Cliente(
                        nombreComercial,
                        rncCedula,
                        cuentaContable,
                        estado
                );
                clienteDAO.insertar(c);
            } else if ("actualizar".equals(action)) {
                Cliente c = new Cliente(
                        Integer.parseInt(request.getParameter("id")),
                        nombreComercial,
                        rncCedula,
                        cuentaContable,
                        estado
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
