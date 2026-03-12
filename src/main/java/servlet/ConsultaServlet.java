package servlet;

import dao.*;
import model.Factura;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/consulta")
public class ConsultaServlet extends HttpServlet {

    private FacturaDAO facturaDAO;
    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private VendedorDAO vendedorDAO;

    @Override
    public void init() {
        facturaDAO = new FacturaDAO();
        articuloDAO = new ArticuloDAO();
        clienteDAO = new ClienteDAO();
        vendedorDAO = new VendedorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("articulos", articuloDAO.listarActivos());
            request.setAttribute("clientes", clienteDAO.listarActivos());
            request.setAttribute("vendedores", vendedorDAO.listarActivos());

            String articuloId = request.getParameter("articuloId");
            String vendedorId = request.getParameter("vendedorId");
            String clienteId = request.getParameter("clienteId");
            String fechaDesde = request.getParameter("fechaDesde");
            String fechaHasta = request.getParameter("fechaHasta");

            if (articuloId != null || vendedorId != null || clienteId != null || (fechaDesde != null && !fechaDesde.isEmpty()) || (fechaHasta != null && !fechaHasta.isEmpty())) {
                Integer artId = parseInteger(articuloId);
                Integer vendId = parseInteger(vendedorId);
                Integer cliId = parseInteger(clienteId);
                Date fd = parseDate(fechaDesde);
                Date fh = parseDate(fechaHasta);
                List<Factura> lista = facturaDAO.consultarPorCriterios(artId, vendId, cliId, fd, fh);
                request.setAttribute("listaFacturas", lista);
            }

            request.getRequestDispatcher("/consulta/consultaFacturas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Integer parseInteger(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return null; }
    }
    private Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return new SimpleDateFormat("yyyy-MM-dd").parse(s.trim()); } catch (Exception e) { return null; }
    }
}
