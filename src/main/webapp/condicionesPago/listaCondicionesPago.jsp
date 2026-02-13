<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.CondicionPago" %>
<%
    List<CondicionPago> lista = (List<CondicionPago>) request.getAttribute("listaCondicionesPago");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Condiciones de Pago</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="condiciones"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
            <div class="page-header mb-0">
                <h1>Condiciones de Pago</h1>
                <p class="subtitle mb-0">Al contado, crédito, cheque y más</p>
            </div>
            <a href="${pageContext.request.contextPath}/condicionesPago?action=nuevo" class="btn btn-primary flex-shrink-0">Nueva condición</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <% if (lista != null && !lista.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover align-middle table-custom">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Descripción</th>
                            <th>Cantidad de Días</th>
                            <th>Estado</th>
                            <th class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (CondicionPago cp : lista) { %>
                        <tr>
                            <td><%= cp.getId() %></td>
                            <td><%= cp.getDescripcion() %></td>
                            <td><%= cp.getCantidadDias() %> días</td>
                            <td><span class="badge badge-status <%= "ACTIVO".equals(cp.getEstado()) ? "bg-success" : "bg-secondary" %>"><%= cp.getEstado() %></span></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/condicionesPago?action=editar&id=<%= cp.getId() %>" class="btn btn-sm btn-outline-secondary me-1">Editar</a>
                                <a href="${pageContext.request.contextPath}/condicionesPago?action=eliminar&id=<%= cp.getId() %>" class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Eliminar esta condición?');">Eliminar</a>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">💳</div>
                    <p class="mb-2">No hay condiciones de pago.</p>
                    <a href="${pageContext.request.contextPath}/condicionesPago?action=nuevo" class="btn btn-primary">Crear la primera</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
