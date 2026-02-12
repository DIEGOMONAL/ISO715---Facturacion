package servlet;

import dao.FacturaDAO;
import model.Factura;

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

    @Override
    public void init() {
        facturaDAO = new FacturaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
                case "nuevo":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "eliminar":
                    eliminar(request, response);
                    break;
                default:
                    listar(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "insertar":
                    insertar(request, response);
                    break;
                case "actualizar":
                    actualizar(request, response);
                    break;
                default:
                    response.sendRedirect("facturas");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Factura> lista = facturaDAO.listarTodas();
        request.setAttribute("listaFacturas", lista);
        request.getRequestDispatcher("/facturas/listaFacturas.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("factura", null);
        request.getRequestDispatcher("/facturas/formFactura.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Factura factura = facturaDAO.obtenerPorId(id);
        request.setAttribute("factura", factura);
        request.getRequestDispatcher("/facturas/formFactura.jsp").forward(request, response);
    }

    private void insertar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String cliente = request.getParameter("cliente");
        Date fecha = Date.valueOf(request.getParameter("fecha"));
        double total = Double.parseDouble(request.getParameter("total"));

        String horaParam = request.getParameter("hora");
        Time hora = null;
        if (horaParam != null && !horaParam.isEmpty()) {
            // el input type="time" devuelve HH:mm
            if (horaParam.length() == 5) {
                horaParam = horaParam + ":00";
            }
            hora = Time.valueOf(horaParam);
        }

        Factura f = new Factura(cliente, fecha, hora, total);
        facturaDAO.insertar(f);
        response.sendRedirect("facturas");
    }

    private void actualizar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String cliente = request.getParameter("cliente");
        Date fecha = Date.valueOf(request.getParameter("fecha"));
        double total = Double.parseDouble(request.getParameter("total"));

        String horaParam = request.getParameter("hora");
        Time hora = null;
        if (horaParam != null && !horaParam.isEmpty()) {
            if (horaParam.length() == 5) {
                horaParam = horaParam + ":00";
            }
            hora = Time.valueOf(horaParam);
        }

        Factura f = new Factura(id, cliente, fecha, hora, total);
        facturaDAO.actualizar(f);
        response.sendRedirect("facturas");
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        facturaDAO.eliminar(id);
        response.sendRedirect("facturas");
    }
}

