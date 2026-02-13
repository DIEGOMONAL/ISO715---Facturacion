package servlet;

import dao.*;
import model.Factura;
import model.FacturaDetalle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

@WebServlet("/facturas")
public class FacturaServlet extends HttpServlet {

    private FacturaDAO facturaDAO;
    private ClienteDAO clienteDAO;
    private CondicionPagoDAO condicionPagoDAO;
    private VendedorDAO vendedorDAO;
    private ArticuloDAO articuloDAO;

    @Override
    public void init() {
        facturaDAO = new FacturaDAO();
        clienteDAO = new ClienteDAO();
        condicionPagoDAO = new CondicionPagoDAO();
        vendedorDAO = new VendedorDAO();
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
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "ver":
                    verFactura(request, response);
                    break;
                case "eliminar":
                    facturaDAO.eliminar(Integer.parseInt(request.getParameter("id")));
                    response.sendRedirect(request.getContextPath() + "/facturas");
                    break;
                default:
                    String buscar = request.getParameter("buscar");
                    String ordenar = request.getParameter("ordenar");
                    String[] ord = parseOrdenar(ordenar, "fecha", "desc");
                    List<Factura> lista = facturaDAO.listarConFiltros(buscar, ord[0], ord[1]);
                    request.setAttribute("listaFacturas", lista);
                    request.setAttribute("buscar", buscar);
                    request.setAttribute("ordenar", ordenar != null ? ordenar : "fecha_desc");
                    request.getRequestDispatcher("/facturas/listaFacturas.jsp").forward(request, response);
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
                insertar(request, response);
            } else if ("actualizar".equals(action)) {
                actualizar(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/facturas");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("factura", null);
        request.setAttribute("clientes", clienteDAO.listarActivos());
        request.setAttribute("condicionesPago", condicionPagoDAO.listarActivos());
        request.setAttribute("vendedores", vendedorDAO.listarActivos());
        request.setAttribute("articulos", articuloDAO.listarActivos());
        request.getRequestDispatcher("/facturas/formFactura.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(id);
        request.setAttribute("factura", factura);
        request.setAttribute("clientes", clienteDAO.listarActivos());
        request.setAttribute("condicionesPago", condicionPagoDAO.listarActivos());
        request.setAttribute("vendedores", vendedorDAO.listarActivos());
        request.setAttribute("articulos", articuloDAO.listarActivos());
        request.getRequestDispatcher("/facturas/formFactura.jsp").forward(request, response);
    }

    private void verFactura(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(id);
        request.setAttribute("factura", factura);
        request.getRequestDispatcher("/facturas/verFactura.jsp").forward(request, response);
    }

    private void insertar(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Factura f = construirFacturaDesdeRequest(request);
        facturaDAO.insertar(f);
        response.sendRedirect(request.getContextPath() + "/facturas");
    }

    private void actualizar(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Factura f = construirFacturaDesdeRequest(request);
        f.setId(Integer.parseInt(request.getParameter("id")));
        facturaDAO.actualizar(f);
        response.sendRedirect(request.getContextPath() + "/facturas");
    }

    private Factura construirFacturaDesdeRequest(HttpServletRequest request) {
        Factura f = new Factura();
        f.setClienteId(Integer.parseInt(request.getParameter("clienteId")));
        f.setCondicionPagoId(Integer.parseInt(request.getParameter("condicionPagoId")));
        f.setVendedorId(Integer.parseInt(request.getParameter("vendedorId")));
        f.setFecha(Date.valueOf(request.getParameter("fecha")));
        String horaParam = request.getParameter("hora");
        if (horaParam != null && !horaParam.isEmpty()) {
            f.setHora(Time.valueOf(horaParam.length() == 5 ? horaParam + ":00" : horaParam));
        }
        String[] articuloIds = request.getParameterValues("articuloId");
        if (articuloIds != null) {
            double total = 0;
            for (int i = 0; i < articuloIds.length; i++) {
                int articuloId = Integer.parseInt(articuloIds[i]);
                int cantidad = parseIntSafe(request.getParameterValues("cantidad")[i], 1);
                double precio = parseDoubleSafe(request.getParameterValues("precioUnitario")[i], 0);
                FacturaDetalle d = new FacturaDetalle(0, articuloId, cantidad, precio);
                d.setSubtotal(cantidad * precio);
                f.addDetalle(d);
                total += d.getSubtotal();
            }
            f.setTotal(total);
        }
        return f;
    }

    private int parseIntSafe(String s, int def) {
        if (s == null || s.trim().isEmpty()) return def;
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return def; }
    }
    private String[] parseOrdenar(String ordenar, String defCol, String defDir) {
        if (ordenar == null || !ordenar.contains("_")) return new String[]{defCol, defDir};
        int i = ordenar.lastIndexOf("_");
        return new String[]{ordenar.substring(0, i), ordenar.substring(i + 1)};
    }
    private double parseDoubleSafe(String s, double def) {
        if (s == null || s.trim().isEmpty()) return def;
        try { return Double.parseDouble(s.trim().replace(",", "")); } catch (NumberFormatException e) { return def; }
    }
}
