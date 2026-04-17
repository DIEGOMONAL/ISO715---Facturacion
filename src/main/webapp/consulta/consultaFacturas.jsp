<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Factura" %>
<%@ page import="model.Articulo" %>
<%@ page import="model.Cliente" %>
<%@ page import="model.Vendedor" %>
<%
    List<Factura> lista = (List<Factura>) request.getAttribute("listaFacturas");
    String articuloIdSel = request.getParameter("articuloId");
    String clienteIdSel = request.getParameter("clienteId");
    String vendedorIdSel = request.getParameter("vendedorId");
    String fechaDesdeSel = request.getParameter("fechaDesde");
    String fechaHastaSel = request.getParameter("fechaHasta");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Consulta por Criterios</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="consulta"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="page-header mb-4">
            <h1>Consulta de Facturas por Criterios</h1>
            <p class="subtitle">Filtra facturas por artículo, cliente, vendedor o rango de fechas</p>
        </div>
        <div class="card card-custom filter-card mb-4">
            <div class="card-body">
                <% String error = (String) request.getAttribute("error"); %>
                <% if (error != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= error %>
                </div>
                <% } %>
                <form action="${pageContext.request.contextPath}/consulta" method="get" class="row g-3" id="formConsulta">
                    <div class="col-md-3">
                        <label class="form-label">Artículo</label>
                        <select name="articuloId" class="form-select">
                            <option value="">-- Todos --</option>
                            <% List<Articulo> articulos = (List<Articulo>) request.getAttribute("articulos");
                                if (articulos != null) for (Articulo a : articulos) { %>
                            <option value="<%= a.getId() %>" <%= (articuloIdSel != null && articuloIdSel.equals(String.valueOf(a.getId()))) ? "selected" : "" %>><%= a.getDescripcion() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Cliente</label>
                        <select name="clienteId" class="form-select">
                            <option value="">-- Todos --</option>
                            <% List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
                                if (clientes != null) for (Cliente c : clientes) { %>
                            <option value="<%= c.getId() %>" <%= (clienteIdSel != null && clienteIdSel.equals(String.valueOf(c.getId()))) ? "selected" : "" %>><%= c.getNombreComercial() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Vendedor</label>
                        <select name="vendedorId" class="form-select">
                            <option value="">-- Todos --</option>
                            <% List<Vendedor> vendedores = (List<Vendedor>) request.getAttribute("vendedores");
                                if (vendedores != null) for (Vendedor v : vendedores) { %>
                            <option value="<%= v.getId() %>" <%= (vendedorIdSel != null && vendedorIdSel.equals(String.valueOf(v.getId()))) ? "selected" : "" %>><%= v.getNombre() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Desde</label>
                        <input type="date" name="fechaDesde" id="fechaDesde" class="form-control" value="<%= fechaDesdeSel != null ? fechaDesdeSel : "" %>">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Hasta</label>
                        <input type="date" name="fechaHasta" id="fechaHasta" class="form-control" value="<%= fechaHastaSel != null ? fechaHastaSel : "" %>">
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary">Buscar</button>
                    </div>
                </form>
            </div>
        </div>
        <% if (lista != null) { %>
        <div class="card card-custom">
            <div class="card-body">
                <h5 class="card-title">Resultados</h5>
                <div class="mb-3 d-flex gap-2 flex-wrap">
                    <span class="me-2">Exportar esta consulta:</span>
                    <a href="${pageContext.request.contextPath}/exportConsulta?type=xls&articuloId=<%= articuloIdSel != null ? articuloIdSel : "" %>&clienteId=<%= clienteIdSel != null ? clienteIdSel : "" %>&vendedorId=<%= vendedorIdSel != null ? vendedorIdSel : "" %>&fechaDesde=<%= fechaDesdeSel != null ? fechaDesdeSel : "" %>&fechaHasta=<%= fechaHastaSel != null ? fechaHastaSel : "" %>"
                       class="btn btn-sm btn-outline-primary">Excel</a>
                    <a href="${pageContext.request.contextPath}/exportConsulta?type=csv&articuloId=<%= articuloIdSel != null ? articuloIdSel : "" %>&clienteId=<%= clienteIdSel != null ? clienteIdSel : "" %>&vendedorId=<%= vendedorIdSel != null ? vendedorIdSel : "" %>&fechaDesde=<%= fechaDesdeSel != null ? fechaDesdeSel : "" %>&fechaHasta=<%= fechaHastaSel != null ? fechaHastaSel : "" %>"
                       class="btn btn-sm btn-outline-secondary">CSV</a>
                    <a href="${pageContext.request.contextPath}/exportConsulta?type=pdf&articuloId=<%= articuloIdSel != null ? articuloIdSel : "" %>&clienteId=<%= clienteIdSel != null ? clienteIdSel : "" %>&vendedorId=<%= vendedorIdSel != null ? vendedorIdSel : "" %>&fechaDesde=<%= fechaDesdeSel != null ? fechaDesdeSel : "" %>&fechaHasta=<%= fechaHastaSel != null ? fechaHastaSel : "" %>"
                       class="btn btn-sm btn-outline-danger">PDF</a>
                </div>
                <% if (!lista.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cliente</th>
                            <th>Vendedor</th>
                            <th>Fecha</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Factura f : lista) { %>
                        <tr>
                            <td><%= f.getId() %></td>
                            <td><%= f.getClienteNombre() %></td>
                            <td><%= f.getVendedorNombre() %></td>
                            <td><%= f.getFecha() %></td>
                            <td>RD$ <%= new java.text.DecimalFormat("#,##0.00").format(f.getTotal()) %></td>
                            <td><a href="${pageContext.request.contextPath}/facturas?action=ver&id=<%= f.getId() %>" class="btn btn-sm btn-outline-primary">Ver</a></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <p class="text-muted mb-0">No se encontraron facturas con los criterios indicados.</p>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
(function() {
    const desde = document.getElementById('fechaDesde');
    const hasta = document.getElementById('fechaHasta');
    if (!desde || !hasta) return;

    function syncMinHasta() {
        if (desde.value) {
            hasta.min = desde.value;
            if (hasta.value && hasta.value < desde.value) {
                hasta.value = desde.value;
            }
        } else {
            hasta.removeAttribute('min');
        }
    }

    desde.addEventListener('change', syncMinHasta);
    // Sincronizar al cargar, en caso de que ya vengan valores del servidor
    syncMinHasta();
})();
</script>
</body>
</html>
