<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Factura" %>
<%@ page import="model.FacturaDetalle" %>
<%@ page import="model.Cliente" %>
<%
    List<Factura> lista = (List<Factura>) request.getAttribute("listaFacturas");
    Integer clienteIdSel = (Integer) request.getAttribute("clienteIdSel");
    String fechaDesdeSel = (String) request.getAttribute("fechaDesdeSel");
    String fechaHastaSel = (String) request.getAttribute("fechaHastaSel");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Reporte Facturación por Cliente</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="reporte"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="page-header mb-4">
            <h1>Reporte de Facturación por Cliente</h1>
            <p class="subtitle">Genera un informe de facturación para un cliente en un rango de fechas</p>
        </div>
        <div class="card card-custom filter-card mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/reporte" method="get" class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">Cliente</label>
                        <select name="clienteId" class="form-select" required>
                            <option value="">-- Seleccione cliente --</option>
                            <% if (clientes != null) for (Cliente c : clientes) { %>
                            <option value="<%= c.getId() %>" <%= (clienteIdSel != null && clienteIdSel == c.getId()) ? "selected" : "" %>><%= c.getNombreComercial() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Fecha desde</label>
                        <input type="date" name="fechaDesde" class="form-control" value="<%= fechaDesdeSel != null ? fechaDesdeSel : "" %>" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Fecha hasta</label>
                        <input type="date" name="fechaHasta" class="form-control" value="<%= fechaHastaSel != null ? fechaHastaSel : "" %>" required>
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">Generar reporte</button>
                    </div>
                </form>
            </div>
        </div>
        <% if (lista != null) { %>
        <div class="card card-custom">
            <div class="card-body">
                <h5 class="card-title">Facturas del cliente en el rango de fechas</h5>
                <% if (!lista.isEmpty()) {
                    double totalGeneral = 0;
                    for (Factura f : lista) totalGeneral += f.getTotal();
                %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>ID Factura</th>
                            <th>Fecha</th>
                            <th>Cond. Pago</th>
                            <th>Vendedor</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Factura f : lista) { %>
                        <tr>
                            <td><%= f.getId() %></td>
                            <td><%= f.getFecha() %></td>
                            <td><%= f.getCondicionPagoDescripcion() %></td>
                            <td><%= f.getVendedorNombre() %></td>
                            <td>RD$ <%= new java.text.DecimalFormat("#,##0.00").format(f.getTotal()) %></td>
                            <td><a href="${pageContext.request.contextPath}/facturas?action=ver&id=<%= f.getId() %>" class="btn btn-sm btn-outline-primary">Ver</a></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <div class="mt-3"><strong>Total facturado: RD$ <%= new java.text.DecimalFormat("#,##0.00").format(totalGeneral) %></strong></div>
                <% } else { %>
                <p class="text-muted mb-0">No hay facturas para este cliente en el rango de fechas seleccionado.</p>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
