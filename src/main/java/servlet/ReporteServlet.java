package servlet;

import dao.ClienteDAO;
import dao.FacturaDAO;
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

@WebServlet("/reporte")
public class ReporteServlet extends HttpServlet {

    private FacturaDAO facturaDAO;
    private ClienteDAO clienteDAO;

    @Override
    public void init() {
        facturaDAO = new FacturaDAO();
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("clientes", clienteDAO.listarActivos());

            String clienteId = request.getParameter("clienteId");
            String fechaDesde = request.getParameter("fechaDesde");
            String fechaHasta = request.getParameter("fechaHasta");

            if (clienteId != null && !clienteId.trim().isEmpty() && fechaDesde != null && !fechaDesde.isEmpty() && fechaHasta != null && !fechaHasta.isEmpty()) {
                int cliId = Integer.parseInt(clienteId.trim());
                Date fd = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDesde.trim());
                Date fh = new SimpleDateFormat("yyyy-MM-dd").parse(fechaHasta.trim());
                List<Factura> lista = facturaDAO.reportePorClienteEntreFechas(cliId, fd, fh);
                request.setAttribute("listaFacturas", lista);
                request.setAttribute("clienteIdSel", cliId);
                request.setAttribute("fechaDesdeSel", fechaDesde);
                request.setAttribute("fechaHastaSel", fechaHasta);
            }

            request.getRequestDispatcher("/reporte/reporteCliente.jsp").forward(request, response);
        } catch (SQLException | java.text.ParseException e) {
            throw new ServletException(e);
        }
    }
}
