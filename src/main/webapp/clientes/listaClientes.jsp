<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Cliente" %>
<%
    List<Cliente> lista = (List<Cliente>) request.getAttribute("listaClientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Clientes</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="clientes"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
            <div class="page-header mb-0">
                <h1>Clientes</h1>
                <p class="subtitle mb-0">Gestiona la información de tus clientes</p>
            </div>
            <a href="${pageContext.request.contextPath}/clientes?action=nuevo" class="btn btn-primary flex-shrink-0">Nuevo cliente</a>
        </div>
        <div class="card card-custom">
            <div class="card-body">
                <% if (lista != null && !lista.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="table table-striped table-hover align-middle table-custom">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre Comercial / Razón Social</th>
                            <th>RNC / Cédula</th>
                            <th>Cuenta Contable</th>
                            <th>Estado</th>
                            <th class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Cliente c : lista) { %>
                        <tr>
                            <td><%= c.getId() %></td>
                            <td><%= c.getNombreComercial() %></td>
                            <td><%= c.getRncCedula() %></td>
                            <td><%= c.getCuentaContable() != null ? c.getCuentaContable() : "-" %></td>
                            <td><span class="badge badge-status <%= "ACTIVO".equals(c.getEstado()) ? "bg-success" : "bg-secondary" %>"><%= c.getEstado() %></span></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/clientes?action=editar&id=<%= c.getId() %>" class="btn btn-sm btn-outline-secondary me-1">Editar</a>
                                <a href="${pageContext.request.contextPath}/clientes?action=eliminar&id=<%= c.getId() %>" class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Eliminar este cliente?');">Eliminar</a>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">👥</div>
                    <p class="mb-2">No hay clientes registrados.</p>
                    <a href="${pageContext.request.contextPath}/clientes?action=nuevo" class="btn btn-primary">Crear el primero</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
