<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Factura" %>
<%
    List<Factura> lista = (List<Factura>) request.getAttribute("listaFacturas");
    String buscar = (String) request.getAttribute("buscar");
    String ordenar = (String) request.getAttribute("ordenar");
    if (ordenar == null) ordenar = "fecha_desc";
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
        <form method="get" class="filtros-bar d-flex flex-wrap gap-2 align-items-end">
            <div class="input-group" style="max-width: 280px;">
                <span class="input-group-text">Buscar</span>
                <input type="text" class="form-control" name="buscar" placeholder="ID factura" value="<%= buscar != null ? buscar : "" %>">
                <button type="submit" class="btn btn-outline-primary">Buscar</button>
            </div>
            <div class="d-flex gap-2 align-items-center flex-wrap">
                <label class="mb-0">Ordenar:</label>
                <select name="ordenar" class="form-select form-select-sm" style="width: auto; min-width: 220px;">
                    <option value="id_asc" <%= "id_asc".equals(ordenar) ? "selected" : "" %>>ID (Ascendente)</option>
                    <option value="id_desc" <%= "id_desc".equals(ordenar) ? "selected" : "" %>>ID (Descendente)</option>
                    <option value="fecha_asc" <%= "fecha_asc".equals(ordenar) ? "selected" : "" %>>Fecha (Ascendente)</option>
                    <option value="fecha_desc" <%= "fecha_desc".equals(ordenar) ? "selected" : "" %>>Fecha (Descendente)</option>
                    <option value="total_asc" <%= "total_asc".equals(ordenar) ? "selected" : "" %>>Total (Ascendente)</option>
                    <option value="total_desc" <%= "total_desc".equals(ordenar) ? "selected" : "" %>>Total (Descendente)</option>
                    <option value="cliente_asc" <%= "cliente_asc".equals(ordenar) ? "selected" : "" %>>Cliente (Ascendente)</option>
                    <option value="cliente_desc" <%= "cliente_desc".equals(ordenar) ? "selected" : "" %>>Cliente (Descendente)</option>
                </select>
                <button type="submit" class="btn btn-sm btn-outline-secondary">Aplicar</button>
            </div>
        </form>
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
                            <td>RD$ <%= new java.text.DecimalFormat("#,##0.00").format(f.getTotal()) %></td>
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
