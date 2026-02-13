<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Factura" %>
<%@ page import="model.FacturaDetalle" %>
<%
    Factura factura = (Factura) request.getAttribute("factura");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Factura #<%= factura != null ? factura.getId() : "" %></title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="facturas"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="card card-custom">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1 class="h4 mb-0">Factura #<%= factura.getId() %></h1>
                    <a href="${pageContext.request.contextPath}/facturas" class="btn btn-outline-secondary">Volver</a>
                </div>
                <% if (factura != null) { %>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><strong>Cliente:</strong> <%= factura.getClienteNombre() %></p>
                        <p><strong>Condición de pago:</strong> <%= factura.getCondicionPagoDescripcion() %></p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Vendedor:</strong> <%= factura.getVendedorNombre() %></p>
                        <p><strong>Fecha:</strong> <%= factura.getFecha() %> <%= factura.getHora() != null ? factura.getHora().toString().substring(0,5) : "" %></p>
                    </div>
                </div>
                <table class="table table-bordered table-custom">
                    <thead>
                    <tr>
                        <th>Artículo</th>
                        <th>Cantidad</th>
                        <th>Precio Unit.</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (FacturaDetalle d : factura.getDetalles()) { %>
                    <tr>
                        <td><%= d.getArticuloDescripcion() != null ? d.getArticuloDescripcion() : "-" %></td>
                        <td><%= d.getCantidad() %></td>
                        <td>RD$ <%= String.format("%.2f", d.getPrecioUnitario()) %></td>
                        <td>RD$ <%= String.format("%.2f", d.getSubtotal()) %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
                <div class="text-end"><strong>Total: RD$ <%= String.format("%.2f", factura.getTotal()) %></strong></div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
