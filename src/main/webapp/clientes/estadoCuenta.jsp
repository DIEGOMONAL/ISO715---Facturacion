<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Cliente" %>
<%@ page import="model.Factura" %>
<%@ page import="model.Abono" %>
<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    Cliente clienteSel = (Cliente) request.getAttribute("clienteSel");
    Integer clienteIdSel = (Integer) request.getAttribute("clienteIdSel");
    List<Factura> facturas = (List<Factura>) request.getAttribute("facturasCliente");
    List<Factura> facturasCredito = (List<Factura>) request.getAttribute("facturasCredito");
    List<Abono> abonosCliente = (List<Abono>) request.getAttribute("abonosCliente");
    Double balanceActual = (Double) request.getAttribute("balanceActual");
    String ok = request.getParameter("ok");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Estado de Cuenta</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="estadoCuenta"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="page-header mb-4">
            <h1>Estado de cuenta</h1>
            <p class="subtitle">Revisa balance, facturas y registra abonos para clientes a credito/cuotas.</p>
        </div>

        <% if ("abono".equals(ok)) { %>
        <div class="alert alert-success">Abono registrado correctamente.</div>
        <% } %>
        <% if ("monto".equals(error)) { %>
        <div class="alert alert-danger">El monto del abono debe ser mayor que cero.</div>
        <% } else if ("monto_mayor".equals(error)) { %>
        <div class="alert alert-danger">El monto no puede ser mayor que la deuda actual del cliente.</div>
        <% } else if ("sin_deuda".equals(error)) { %>
        <div class="alert alert-warning">El cliente no tiene deuda pendiente.</div>
        <% } else if ("factura".equals(error)) { %>
        <div class="alert alert-danger">La factura seleccionada no pertenece al cliente.</div>
        <% } else if ("cliente".equals(error) || "datos".equals(error)) { %>
        <div class="alert alert-danger">No se pudo registrar el abono. Verifica los datos.</div>
        <% } %>

        <div class="card card-custom mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/estadoCuenta" method="get" class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Cliente</label>
                        <select name="clienteId" class="form-select" required>
                            <option value="">-- Seleccione --</option>
                            <% if (clientes != null) for (Cliente c : clientes) { %>
                            <option value="<%= c.getId() %>" <%= (clienteIdSel != null && clienteIdSel == c.getId()) ? "selected" : "" %>><%= c.getNombreComercial() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-3 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">Consultar</button>
                    </div>
                </form>
            </div>
        </div>

        <% if (clienteSel != null) { %>
        <div class="card card-custom mb-4">
            <div class="card-body">
                <h5 class="card-title">Cliente seleccionado</h5>
                <p><strong>Nombre:</strong> <%= clienteSel.getNombreComercial() %></p>
                <p><strong>RNC/Cédula:</strong> <%= clienteSel.getRncCedula() %></p>
                <p><strong>Balance actual:</strong> RD$ <%= new java.text.DecimalFormat("#,##0.00").format(balanceActual != null ? balanceActual : 0.0) %></p>
            </div>
        </div>

        <div class="card card-custom mb-4">
            <div class="card-body">
                <h5 class="card-title">Registrar abono</h5>
                <form action="${pageContext.request.contextPath}/estadoCuenta" method="post" class="row g-3">
                    <input type="hidden" name="action" value="abonar"/>
                    <input type="hidden" name="clienteId" value="<%= clienteSel.getId() %>"/>
                    <div class="col-md-4">
                        <label class="form-label">Monto (RD$)</label>
                        <input type="number" name="monto" min="0.01" step="0.01" class="form-control" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Factura (opcional)</label>
                        <select name="facturaId" class="form-select">
                            <option value="">Aplicar al saldo general</option>
                            <% if (facturasCredito != null) for (Factura fc : facturasCredito) { %>
                            <option value="<%= fc.getId() %>">
                                #<%= fc.getId() %> - <%= fc.getFecha() %> - RD$ <%= new java.text.DecimalFormat("#,##0.00").format(fc.getTotal()) %>
                            </option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Observación</label>
                        <input type="text" name="observacion" maxlength="255" class="form-control" placeholder="Ej: pago parcial de deuda">
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-success">Registrar abono</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card card-custom">
            <div class="card-body">
                <h5 class="card-title">Facturas del cliente</h5>
                <% if (facturas != null && !facturas.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Fecha</th>
                            <th>Vendedor</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Factura f : facturas) { %>
                        <tr>
                            <td><%= f.getId() %></td>
                            <td><%= f.getFecha() %></td>
                            <td><%= f.getVendedorNombre() %></td>
                            <td>RD$ <%= new java.text.DecimalFormat("#,##0.00").format(f.getTotal()) %></td>
                            <td><a href="${pageContext.request.contextPath}/facturas?action=ver&id=<%= f.getId() %>" class="btn btn-sm btn-outline-primary">Ver</a></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <p class="text-muted mb-0">Este cliente no tiene facturas registradas.</p>
                <% } %>
            </div>
        </div>

        <div class="card card-custom mt-4">
            <div class="card-body">
                <h5 class="card-title">Historial de abonos</h5>
                <% if (abonosCliente != null && !abonosCliente.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-custom">
                        <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Referencia</th>
                            <th>Monto</th>
                            <th>Observación</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Abono ab : abonosCliente) { %>
                        <tr>
                            <td><%= ab.getFechaRegistro() %></td>
                            <td><%= ab.getFacturaId() != null ? ab.getReferenciaFactura() : "Saldo general" %></td>
                            <td>RD$ <%= new java.text.DecimalFormat("#,##0.00").format(ab.getMonto()) %></td>
                            <td><%= ab.getObservacion() != null && !ab.getObservacion().trim().isEmpty() ? ab.getObservacion() : "-" %></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <p class="text-muted mb-0">Aun no hay abonos registrados para este cliente.</p>
                <% } %>
            </div>
        </div>
        <% } %>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

