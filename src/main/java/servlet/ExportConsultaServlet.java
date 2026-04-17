package servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ArticuloDAO;
import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.VendedorDAO;
import model.Articulo;
import model.Cliente;
import model.Factura;
import model.Vendedor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportConsultaServlet extends HttpServlet {

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
        String type = request.getParameter("type");
        if (type == null || type.trim().isEmpty()) type = "xls";

        Integer artId = parseInteger(request.getParameter("articuloId"));
        Integer cliId = parseInteger(request.getParameter("clienteId"));
        Integer vendId = parseInteger(request.getParameter("vendedorId"));
        Date fechaDesde = parseDate(request.getParameter("fechaDesde"));
        Date fechaHasta = parseDate(request.getParameter("fechaHasta"));

        try {
            List<Factura> lista = facturaDAO.consultarPorCriterios(artId, vendId, cliId, fechaDesde, fechaHasta);
            String descripcion = construirDescripcion(artId, cliId, vendId, request.getParameter("fechaDesde"), request.getParameter("fechaHasta"));
            switch (type.toLowerCase(Locale.ROOT)) {
                case "csv":
                    exportCsv(lista, descripcion, response);
                    break;
                case "pdf":
                    exportPdf(lista, descripcion, response);
                    break;
                case "xls":
                case "xlsx":
                default:
                    exportExcel(lista, descripcion, response);
                    break;
            }
        } catch (SQLException | DocumentException e) {
            throw new ServletException("Error exportando consulta", e);
        }
    }

    private void exportCsv(List<Factura> lista, String descripcion, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=consulta_facturas.csv");
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(descripcion).append("\n");
        sb.append("ID;Cliente;Vendedor;Fecha;Total\n");
        for (Factura f : lista) {
            sb.append(f.getId()).append(";")
                    .append(safe(f.getClienteNombre())).append(";")
                    .append(safe(f.getVendedorNombre())).append(";")
                    .append(f.getFecha() != null ? f.getFecha() : "").append(";")
                    .append(String.format(Locale.US, "%.2f", f.getTotal()))
                    .append("\n");
        }
        response.getWriter().write(sb.toString());
    }

    private void exportExcel(List<Factura> lista, String descripcion, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=consulta_facturas.xlsx");

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sh = wb.createSheet("Consulta Facturas");
            int rowNum = 0;
            sh.createRow(rowNum++).createCell(0).setCellValue(descripcion);
            rowNum++;
            Row header = sh.createRow(rowNum++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Cliente");
            header.createCell(2).setCellValue("Vendedor");
            header.createCell(3).setCellValue("Fecha");
            header.createCell(4).setCellValue("Total");

            for (Factura f : lista) {
                Row r = sh.createRow(rowNum++);
                r.createCell(0).setCellValue(f.getId());
                r.createCell(1).setCellValue(safe(f.getClienteNombre()));
                r.createCell(2).setCellValue(safe(f.getVendedorNombre()));
                r.createCell(3).setCellValue(f.getFecha() != null ? f.getFecha().toString() : "");
                r.createCell(4).setCellValue(f.getTotal());
            }
            for (int i = 0; i <= 4; i++) sh.autoSizeColumn(i);

            try (OutputStream os = response.getOutputStream()) {
                wb.write(os);
            }
        }
    }

    private void exportPdf(List<Factura> lista, String descripcion, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=consulta_facturas.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("Reporte de Consulta de Facturas"));
        document.add(new Paragraph(descripcion));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        addHeader(table, "ID");
        addHeader(table, "Cliente");
        addHeader(table, "Vendedor");
        addHeader(table, "Fecha");
        addHeader(table, "Total");

        for (Factura f : lista) {
            table.addCell(String.valueOf(f.getId()));
            table.addCell(safe(f.getClienteNombre()));
            table.addCell(safe(f.getVendedorNombre()));
            table.addCell(f.getFecha() != null ? f.getFecha().toString() : "");
            table.addCell(String.format(Locale.US, "%.2f", f.getTotal()));
        }
        document.add(table);
        document.close();
    }

    private String construirDescripcion(Integer artId, Integer cliId, Integer vendId, String desde, String hasta) throws SQLException {
        StringBuilder sb = new StringBuilder("Facturas");
        if (artId != null && artId > 0) {
            Articulo a = articuloDAO.obtenerPorId(artId);
            sb.append(" del artículo ").append(a != null ? a.getDescripcion() : "#" + artId);
        }
        if (cliId != null && cliId > 0) {
            Cliente c = clienteDAO.obtenerPorId(cliId);
            sb.append(" del cliente ").append(c != null ? c.getNombreComercial() : "#" + cliId);
        }
        if (vendId != null && vendId > 0) {
            Vendedor v = vendedorDAO.obtenerPorId(vendId);
            sb.append(" del vendedor ").append(v != null ? v.getNombre() : "#" + vendId);
        }
        if (desde != null && !desde.trim().isEmpty()) {
            sb.append(" desde ").append(desde);
        }
        if (hasta != null && !hasta.trim().isEmpty()) {
            sb.append(" hasta ").append(hasta);
        }
        return sb.toString();
    }

    private void addHeader(PdfPTable table, String text) {
        table.addCell(new PdfPCell(new Phrase(text)));
    }

    private Integer parseInteger(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(String s) {
        return s != null ? s : "";
    }
}

