package servlet;

import dao.ArticuloDAO;
import model.Articulo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/articulos")
public class ArticuloServlet extends HttpServlet {

    private ArticuloDAO articuloDAO;

    @Override
    public void init() {
        articuloDAO = new ArticuloDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";
        try {
            switch (action) {
                case "nuevo":
                    request.setAttribute("articulo", null);
                    request.getRequestDispatcher("/articulos/formArticulo.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Articulo a = articuloDAO.obtenerPorId(idEditar);
                    request.setAttribute("articulo", a);
                    request.getRequestDispatcher("/articulos/formArticulo.jsp").forward(request, response);
                    break;
                case "eliminar":
                    articuloDAO.eliminar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/articulos");
                    break;
                default:
                    String buscar = request.getParameter("buscar");
                    String ordenar = request.getParameter("ordenar");
                    String[] ord = parseOrdenar(ordenar, "descripcion", "asc");
                    List<Articulo> lista = articuloDAO.listarConFiltros(buscar, ord[0], ord[1]);
                    request.setAttribute("listaArticulos", lista);
                    request.setAttribute("buscar", buscar);
                    request.setAttribute("ordenar", ordenar != null ? ordenar : "descripcion_asc");
                    request.getRequestDispatcher("/articulos/listaArticulos.jsp").forward(request, response);
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
                Articulo a = new Articulo(
                        request.getParameter("descripcion"),
                        parseDoubleSafe(request.getParameter("precioUnitario")),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                articuloDAO.insertar(a);
            } else if ("actualizar".equals(action)) {
                Articulo a = new Articulo(
                        Integer.parseInt(request.getParameter("id")),
                        request.getParameter("descripcion"),
                        parseDoubleSafe(request.getParameter("precioUnitario")),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                articuloDAO.actualizar(a);
            }
            response.sendRedirect(request.getContextPath() + "/articulos");
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
        String v = s.trim().replace(",", "");
        try { return Double.parseDouble(v); } catch (NumberFormatException e) { return 0; }
    }
}
