<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Factura" %>
<%
    List<Factura> lista = (List<Factura>) request.getAttribute("listaFacturas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Listado de Facturas</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="facturas"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
            <div class="page-header mb-0">
                <h1>Facturación de Artículos</h1>
                <p class="subtitle mb-0">Crea y gestiona facturas de venta</p>
            </div>
            <a href="${pageContext.request.contextPath}/facturas?action=nuevo" class="btn btn-primary flex-shrink-0">Nueva factura</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <% if (lista != null && !lista.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover align-middle table-custom">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cliente</th>
                            <th>Cond. Pago</th>
                            <th>Vendedor</th>
                            <th>Fecha</th>
                            <th>Total</th>
                            <th class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Factura f : lista) { %>
                        <tr>
                            <td><%= f.getId() %></td>
                            <td><%= f.getClienteNombre() != null ? f.getClienteNombre() : "-" %></td>
                            <td><%= f.getCondicionPagoDescripcion() != null ? f.getCondicionPagoDescripcion() : "-" %></td>
                            <td><%= f.getVendedorNombre() != null ? f.getVendedorNombre() : "-" %></td>
                            <td><%= f.getFecha() != null ? f.getFecha() : "" %></td>
                            <td>RD$ <%= String.format("%.2f", f.getTotal()) %></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/facturas?action=ver&id=<%= f.getId() %>" class="btn btn-sm btn-outline-primary me-1">Ver</a>
                                <a href="${pageContext.request.contextPath}/facturas?action=editar&id=<%= f.getId() %>" class="btn btn-sm btn-outline-secondary me-1">Editar</a>
                                <a href="${pageContext.request.contextPath}/facturas?action=eliminar&id=<%= f.getId() %>" class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Eliminar esta factura?');">Eliminar</a>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">📄</div>
                    <p class="mb-2">No hay facturas registradas.</p>
                    <a href="${pageContext.request.contextPath}/facturas?action=nuevo" class="btn btn-primary">Crear la primera</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
