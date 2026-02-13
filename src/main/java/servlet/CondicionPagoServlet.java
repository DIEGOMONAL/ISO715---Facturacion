package servlet;

import dao.CondicionPagoDAO;
import model.CondicionPago;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/condicionesPago")
public class CondicionPagoServlet extends HttpServlet {

    private CondicionPagoDAO condicionPagoDAO;

    @Override
    public void init() {
        condicionPagoDAO = new CondicionPagoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";
        try {
            switch (action) {
                case "nuevo":
                    request.setAttribute("condicionPago", null);
                    request.getRequestDispatcher("/condicionesPago/formCondicionPago.jsp").forward(request, response);
                    break;
                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    CondicionPago cp = condicionPagoDAO.obtenerPorId(idEditar);
                    request.setAttribute("condicionPago", cp);
                    request.getRequestDispatcher("/condicionesPago/formCondicionPago.jsp").forward(request, response);
                    break;
                case "eliminar":
                    condicionPagoDAO.eliminar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/condicionesPago");
                    break;
                default:
                    String buscar = request.getParameter("buscar");
                    String ordenar = request.getParameter("ordenar");
                    String[] ord = parseOrdenar(ordenar, "descripcion", "asc");
                    List<CondicionPago> lista = condicionPagoDAO.listarConFiltros(buscar, ord[0], ord[1]);
                    request.setAttribute("listaCondicionesPago", lista);
                    request.setAttribute("buscar", buscar);
                    request.setAttribute("ordenar", ordenar != null ? ordenar : "descripcion_asc");
                    request.getRequestDispatcher("/condicionesPago/listaCondicionesPago.jsp").forward(request, response);
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
                CondicionPago cp = new CondicionPago(
                        request.getParameter("descripcion"),
                        parseIntSafe(request.getParameter("cantidadDias"), 0),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                condicionPagoDAO.insertar(cp);
            } else if ("actualizar".equals(action)) {
                CondicionPago cp = new CondicionPago(
                        Integer.parseInt(request.getParameter("id")),
                        request.getParameter("descripcion"),
                        parseIntSafe(request.getParameter("cantidadDias"), 0),
                        request.getParameter("estado") != null ? request.getParameter("estado") : "ACTIVO"
                );
                condicionPagoDAO.actualizar(cp);
            }
            response.sendRedirect(request.getContextPath() + "/condicionesPago");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String[] parseOrdenar(String ordenar, String defCol, String defDir) {
        if (ordenar == null || !ordenar.contains("_")) return new String[]{defCol, defDir};
        int i = ordenar.lastIndexOf("_");
        return new String[]{ordenar.substring(0, i), ordenar.substring(i + 1)};
    }
    private int parseIntSafe(String s, int def) {
        if (s == null || s.trim().isEmpty()) return def;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
