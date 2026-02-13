package servlet;

import dao.VendedorDAO;
import model.Vendedor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/vendedores")
public class VendedorServlet extends HttpServlet {

    private VendedorDAO vendedorDAO;

    @Override
    public void init() {
        vendedorDAO = new VendedorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";
        try {
            switch (action) {
                case "nuevo":
                    request.setAttribute("vendedor", null);
                    request.getRequestDispatcher("/vendedores/formVendedor.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Vendedor v = vendedorDAO.obtenerPorId(idEditar);
                    request.setAttribute("vendedor", v);
                    request.getRequestDispatcher("/vendedores/formVendedor.jsp").forward(request, response);
                    break;
                case "eliminar":
                    vendedorDAO.eliminar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/vendedores");
                    break;
                default:
                    String buscar = request.getParameter("buscar");
                    String ordenar = request.getParameter("ordenar");
                    String[] ord = parseOrdenar(ordenar, "nombre", "asc");
                    List<Vendedor> lista = vendedorDAO.listarConFiltros(buscar, ord[0], ord[1]);
                    request.setAttribute("listaVendedores", lista);
                    request.setAttribute("buscar", buscar);
                    request.setAttribute("ordenar", ordenar != null ? ordenar : "nombre_asc");
                    request.getRequestDispatcher("/vendedores/listaVendedores.jsp").forward(request, response);
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
                Vendedor v = new Vendedor(
                        request.getParameter("nombre"),
                        parseDoubleSafe(request.getParameter("porcientoComision")),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                vendedorDAO.insertar(v);
            } else if ("actualizar".equals(action)) {
                Vendedor v = new Vendedor(
                        Integer.parseInt(request.getParameter("id")),
                        request.getParameter("nombre"),
                        parseDoubleSafe(request.getParameter("porcientoComision")),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                vendedorDAO.actualizar(v);
            }
            response.sendRedirect(request.getContextPath() + "/vendedores");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String[] parseOrdenar(String ordenar, String defCol, String defDir) {
        if (ordenar == null || !ordenar.contains("_")) return new String[]{defCol, defDir};
        int i = ordenar.lastIndexOf("_");
        return new String[]{ordenar.substring(0, i), ordenar.substring(i + 1)};
    }
    private double parseDoubleSafe(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        try { return Double.parseDouble(s.trim().replace(",", "")); } catch (NumberFormatException e) { return 0; }
    }
}
