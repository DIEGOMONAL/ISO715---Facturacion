<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Factura" %>
<%@ page import="model.FacturaDetalle" %>
<%@ page import="model.Articulo" %>
<%@ page import="model.Cliente" %>
<%@ page import="model.CondicionPago" %>
<%@ page import="model.Vendedor" %>
<%@ page import="java.util.List" %>
<%
    Factura factura = (Factura) request.getAttribute("factura");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<CondicionPago> condicionesPago = (List<CondicionPago>) request.getAttribute("condicionesPago");
    List<Vendedor> vendedores = (List<Vendedor>) request.getAttribute("vendedores");
    List<Articulo> articulos = (List<Articulo>) request.getAttribute("articulos");
    boolean esEdicion = (factura != null);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title><%= esEdicion ? "Editar" : "Nueva" %> Factura</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="facturas"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="card card-custom">
            <div class="card-body">
                <h1 class="h4 mb-3"><%= esEdicion ? "Editar Factura" : "Nueva Factura de Artículos" %></h1>

                <form action="${pageContext.request.contextPath}/facturas" method="post" id="formFactura">
                    <input type="hidden" name="action" value="<%= esEdicion ? "actualizar" : "insertar" %>"/>
                    <% if (esEdicion) { %><input type="hidden" name="id" value="<%= factura.getId() %>"/><% } %>

                    <div class="row mb-3">
                        <div class="col-md-4">
                            <label for="clienteId" class="form-label">Cliente</label>
                            <select class="form-select" id="clienteId" name="clienteId" required>
                                <option value="">-- Seleccione --</option>
                                <% if (clientes != null) for (Cliente c : clientes) { %>
                                <option value="<%= c.getId() %>" <%= (esEdicion && factura.getClienteId() == c.getId()) ? "selected" : "" %>><%= c.getNombreComercial() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="condicionPagoId" class="form-label">Condición de Pago</label>
                            <select class="form-select" id="condicionPagoId" name="condicionPagoId" required>
                                <option value="">-- Seleccione --</option>
                                <% if (condicionesPago != null) for (CondicionPago cp : condicionesPago) { %>
                                <option value="<%= cp.getId() %>" <%= (esEdicion && factura.getCondicionPagoId() == cp.getId()) ? "selected" : "" %>><%= cp.getDescripcion() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="vendedorId" class="form-label">Vendedor</label>
                            <select class="form-select" id="vendedorId" name="vendedorId" required>
                                <option value="">-- Seleccione --</option>
                                <% if (vendedores != null) for (Vendedor v : vendedores) { %>
                                <option value="<%= v.getId() %>" <%= (esEdicion && factura.getVendedorId() == v.getId()) ? "selected" : "" %>><%= v.getNombre() %></option>
                                <% } %>
                            </select>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-4">
                            <label for="fecha" class="form-label">Fecha</label>
                            <input type="date" class="form-control" id="fecha" name="fecha"
                                   value="<%= esEdicion && factura.getFecha() != null ? factura.getFecha().toString() : "" %>" required>
                        </div>
                        <div class="col-md-4">
                            <label for="hora" class="form-label">Hora</label>
                            <input type="time" class="form-control" id="hora" name="hora"
                                   value="<%= (esEdicion && factura.getHora() != null) ? factura.getHora().toString().substring(0,5) : "" %>">
                        </div>
                    </div>

                    <h5 class="mt-4 mb-2">Artículos a facturar</h5>
                    <div class="table-responsive">
                        <table class="table table-bordered table-custom" id="tablaDetalles">
                            <thead>
                            <tr>
                                <th>Artículo</th>
                                <th style="width:100px">Cantidad</th>
                                <th style="width:120px">Precio Unit.</th>
                                <th style="width:120px">Subtotal</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody id="filasDetalle">
                            <% if (esEdicion && factura.getDetalles() != null && !factura.getDetalles().isEmpty()) {
                                for (FacturaDetalle d : factura.getDetalles()) {
                                    Articulo art = null;
                                    if (articulos != null) for (Articulo a : articulos) { if (a.getId() == d.getArticuloId()) { art = a; break; } }
                            %>
                            <tr>
                                <td>
                                    <select class="form-select articulo-select" name="articuloId" required>
                                        <option value="">-- Artículo --</option>
                                        <% for (Articulo a : articulos) { %>
                                        <option value="<%= a.getId() %>" data-precio="<%= a.getPrecioUnitario() %>" <%= d.getArticuloId() == a.getId() ? "selected" : "" %>><%= a.getDescripcion() %> (RD$ <%= String.format("%.2f", a.getPrecioUnitario()) %>)</option>
                                        <% } %>
                                    </select>
                                </td>
                                <td><input type="number" min="1" class="form-control cantidad-input" name="cantidad" value="<%= d.getCantidad() %>" required></td>
                                <td><input type="number" step="0.01" class="form-control precio-input" name="precioUnitario" value="<%= d.getPrecioUnitario() %>" required></td>
                                <td class="subtotal-cell"><%= String.format("%.2f", d.getSubtotal()) %></td>
                                <td><button type="button" class="btn btn-sm btn-outline-danger btn-eliminar">Quitar</button></td>
                            </tr>
                            <% }
                            } else if (articulos != null && !articulos.isEmpty()) { %>
                            <tr>
                                <td>
                                    <select class="form-select articulo-select" name="articuloId" required>
                                        <option value="">-- Artículo --</option>
                                        <% for (Articulo a : articulos) { %>
                                        <option value="<%= a.getId() %>" data-precio="<%= a.getPrecioUnitario() %>"><%= a.getDescripcion() %> (RD$ <%= String.format("%.2f", a.getPrecioUnitario()) %>)</option>
                                        <% } %>
                                    </select>
                                </td>
                                <td><input type="number" min="1" class="form-control cantidad-input" name="cantidad" value="1" required></td>
                                <td><input type="number" step="0.01" class="form-control precio-input" name="precioUnitario" required></td>
                                <td class="subtotal-cell">0.00</td>
                                <td><button type="button" class="btn btn-sm btn-outline-danger btn-eliminar">Quitar</button></td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                    <% if (articulos != null && !articulos.isEmpty()) { %>
                    <button type="button" class="btn btn-outline-secondary btn-sm mb-3" id="btnAgregar">+ Agregar artículo</button>
                    <% } %>
                    <div class="d-flex justify-content-between align-items-center">
                        <div><strong>Total: RD$ </strong><span id="totalFactura">0.00</span></div>
                        <div>
                            <a href="${pageContext.request.contextPath}/facturas" class="btn btn-outline-secondary">Cancelar</a>
                            <button type="submit" class="btn btn-primary">Guardar factura</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
(function() {
    const tbody = document.getElementById('filasDetalle');
    const btnAgregar = document.getElementById('btnAgregar');
    const totalSpan = document.getElementById('totalFactura');

    function calcularSubtotal(row) {
        const cant = parseFloat(row.querySelector('.cantidad-input').value) || 0;
        const prec = parseFloat(row.querySelector('.precio-input').value) || 0;
        const sub = cant * prec;
        row.querySelector('.subtotal-cell').textContent = sub.toFixed(2);
        return sub;
    }
    function actualizarTotal() {
        let t = 0;
        tbody.querySelectorAll('tr').forEach(function(r) {
            t += calcularSubtotal(r);
        });
        totalSpan.textContent = t.toFixed(2);
    }
    function initFila(row) {
        const sel = row.querySelector('.articulo-select');
        const precioInp = row.querySelector('.precio-input');
        if (sel && sel.value && sel.options[sel.selectedIndex]) {
            const dataP = sel.options[sel.selectedIndex].dataset.precio;
            if (dataP && !precioInp.value) precioInp.value = dataP;
        }
        sel && sel.addEventListener('change', function() {
            const opt = this.options[this.selectedIndex];
            if (opt && opt.dataset.precio) precioInp.value = opt.dataset.precio;
            actualizarTotal();
        });
        row.querySelector('.cantidad-input').addEventListener('input', actualizarTotal);
        row.querySelector('.precio-input').addEventListener('input', actualizarTotal);
        row.querySelector('.btn-eliminar').addEventListener('click', function() {
            if (tbody.querySelectorAll('tr').length > 1) { row.remove(); actualizarTotal(); }
        });
        actualizarTotal();
    }
    tbody.querySelectorAll('tr').forEach(initFila);
    if (btnAgregar) {
        btnAgregar.addEventListener('click', function() {
            const primera = tbody.querySelector('tr');
            if (!primera) return;
            const clon = primera.cloneNode(true);
            clon.querySelectorAll('input, select').forEach(function(el) { el.value = el.tagName === 'SELECT' ? '' : (el.type === 'number' && el.name === 'cantidad' ? 1 : ''); });
            clon.querySelector('.subtotal-cell').textContent = '0.00';
            tbody.appendChild(clon);
            initFila(clon);
        });
    }
    actualizarTotal();
})();
</script>
</body>
</html>
