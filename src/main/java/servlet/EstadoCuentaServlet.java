package servlet;

import dao.AbonoDAO;
import dao.ClienteDAO;
import dao.FacturaDAO;
import model.Abono;
import model.Cliente;
import model.Factura;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EstadoCuentaServlet extends HttpServlet {

    private ClienteDAO clienteDAO;
    private FacturaDAO facturaDAO;
    private AbonoDAO abonoDAO;

    @Override
    public void init() {
        clienteDAO = new ClienteDAO();
        facturaDAO = new FacturaDAO();
        abonoDAO = new AbonoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            cargarVista(request);
            request.getRequestDispatcher("/clientes/estadoCuenta.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (!"abonar".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/estadoCuenta");
            return;
        }
        String clienteIdParam = request.getParameter("clienteId");
        try {
            int clienteId = Integer.parseInt(clienteIdParam);
            double monto = parseMonto(request.getParameter("monto"));
            Integer facturaId = parseFacturaId(request.getParameter("facturaId"));
            String observacion = request.getParameter("observacion");
            Cliente cliente = clienteDAO.obtenerPorId(clienteId);
            if (cliente == null) {
                response.sendRedirect(request.getContextPath() + "/estadoCuenta?error=cliente");
                return;
            }
            if (monto <= 0) {
                response.sendRedirect(request.getContextPath() + "/estadoCuenta?clienteId=" + clienteId + "&error=monto");
                return;
            }
            if (cliente.getBalance() <= 0) {
                response.sendRedirect(request.getContextPath() + "/estadoCuenta?clienteId=" + clienteId + "&error=sin_deuda");
                return;
            }
            if (monto > cliente.getBalance()) {
                response.sendRedirect(request.getContextPath() + "/estadoCuenta?clienteId=" + clienteId + "&error=monto_mayor");
                return;
            }
            if (facturaId != null) {
                Factura factura = facturaDAO.obtenerPorId(facturaId);
                if (factura == null || factura.getClienteId() != clienteId) {
                    response.sendRedirect(request.getContextPath() + "/estadoCuenta?clienteId=" + clienteId + "&error=factura");
                    return;
                }
            }

            Abono abono = new Abono();
            abono.setClienteId(clienteId);
            abono.setFacturaId(facturaId);
            abono.setMonto(monto);
            abono.setObservacion(observacion != null ? observacion.trim() : null);
            abonoDAO.insertar(abono);
            response.sendRedirect(request.getContextPath() + "/estadoCuenta?clienteId=" + clienteId + "&ok=abono");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/estadoCuenta?error=datos");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void cargarVista(HttpServletRequest request) throws SQLException {
        List<Cliente> clientes = clienteDAO.listarActivos();
        request.setAttribute("clientes", clientes);

        String clienteId = request.getParameter("clienteId");
        if (clienteId != null && !clienteId.trim().isEmpty()) {
            int id = Integer.parseInt(clienteId.trim());
            Cliente c = clienteDAO.obtenerPorId(id);
            List<Factura> facturas = facturaDAO.consultarPorCriterios(null, null, id, null, null);
            List<Factura> facturasCredito = facturaDAO.listarCreditoPorCliente(id);
            List<Abono> abonos = abonoDAO.listarPorCliente(id);
            request.setAttribute("clienteSel", c);
            request.setAttribute("facturasCliente", facturas);
            request.setAttribute("facturasCredito", facturasCredito);
            request.setAttribute("abonosCliente", abonos);
            request.setAttribute("clienteIdSel", id);
            request.setAttribute("balanceActual", c != null ? c.getBalance() : 0.0);
        }
    }

    private double parseMonto(String montoParam) {
        if (montoParam == null || montoParam.trim().isEmpty()) return 0;
        return Double.parseDouble(montoParam.trim().replace(",", ""));
    }

    private Integer parseFacturaId(String facturaIdParam) {
        if (facturaIdParam == null || facturaIdParam.trim().isEmpty()) return null;
        int id = Integer.parseInt(facturaIdParam.trim());
        return id > 0 ? id : null;
    }
}

