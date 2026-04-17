package servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.FacturaDAO;
import model.Factura;
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
import java.util.List;
import java.util.Locale;

public class ExportFacturasServlet extends HttpServlet {

    private FacturaDAO facturaDAO;

    @Override
    public void init() {
        facturaDAO = new FacturaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        if (type == null || type.trim().isEmpty()) type = "xls";

        String buscar = request.getParameter("buscar");
        String ordenar = request.getParameter("ordenar");
        String[] ord = parseOrdenar(ordenar, "fecha", "desc");

        try {
            List<Factura> lista = facturaDAO.listarConFiltros(buscar, ord[0], ord[1]);
            switch (type.toLowerCase(Locale.ROOT)) {
                case "csv":
                    exportCsv(lista, response);
                    break;
                case "pdf":
                    exportPdf(lista, response);
                    break;
                case "xls":
                case "xlsx":
                default:
                    exportExcel(lista, response);
                    break;
            }
        } catch (SQLException | DocumentException e) {
            throw new ServletException("Error exportando facturas", e);
        }
    }

    private void exportCsv(List<Factura> lista, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=facturas.csv");
        StringBuilder sb = new StringBuilder();
        sb.append("ID;Cliente;Condicion Pago;Vendedor;Fecha;Total\n");
        for (Factura f : lista) {
            sb.append(f.getId()).append(";")
                    .append(safe(f.getClienteNombre())).append(";")
                    .append(safe(f.getCondicionPagoDescripcion())).append(";")
                    .append(safe(f.getVendedorNombre())).append(";")
                    .append(f.getFecha() != null ? f.getFecha() : "").append(";")
                    .append(String.format(Locale.US, "%.2f", f.getTotal()))
                    .append("\n");
        }
        response.getWriter().write(sb.toString());
    }

    private void exportExcel(List<Factura> lista, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=facturas.xlsx");

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sh = wb.createSheet("Facturas");
            int rowNum = 0;
            Row header = sh.createRow(rowNum++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Cliente");
            header.createCell(2).setCellValue("Condición Pago");
            header.createCell(3).setCellValue("Vendedor");
            header.createCell(4).setCellValue("Fecha");
            header.createCell(5).setCellValue("Total");

            for (Factura f : lista) {
                Row r = sh.createRow(rowNum++);
                r.createCell(0).setCellValue(f.getId());
                r.createCell(1).setCellValue(safe(f.getClienteNombre()));
                r.createCell(2).setCellValue(safe(f.getCondicionPagoDescripcion()));
                r.createCell(3).setCellValue(safe(f.getVendedorNombre()));
                r.createCell(4).setCellValue(f.getFecha() != null ? f.getFecha().toString() : "");
                r.createCell(5).setCellValue(f.getTotal());
            }

            for (int i = 0; i <= 5; i++) sh.autoSizeColumn(i);

            try (OutputStream os = response.getOutputStream()) {
                wb.write(os);
            }
        }
    }

    private void exportPdf(List<Factura> lista, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=facturas.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("Reporte de Facturas"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        addHeader(table, "ID");
        addHeader(table, "Cliente");
        addHeader(table, "Condición Pago");
        addHeader(table, "Vendedor");
        addHeader(table, "Fecha");
        addHeader(table, "Total");

        for (Factura f : lista) {
            table.addCell(String.valueOf(f.getId()));
            table.addCell(safe(f.getClienteNombre()));
            table.addCell(safe(f.getCondicionPagoDescripcion()));
            table.addCell(safe(f.getVendedorNombre()));
            table.addCell(f.getFecha() != null ? f.getFecha().toString() : "");
            table.addCell(String.format(Locale.US, "%.2f", f.getTotal()));
        }

        document.add(table);
        document.close();
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        table.addCell(cell);
    }

    private String safe(String s) {
        return s != null ? s : "";
    }

    private String[] parseOrdenar(String ordenar, String defCol, String defDir) {
        if (ordenar == null || !ordenar.contains("_")) return new String[]{defCol, defDir};
        int i = ordenar.lastIndexOf("_");
        return new String[]{ordenar.substring(0, i), ordenar.substring(i + 1)};
    }
}

