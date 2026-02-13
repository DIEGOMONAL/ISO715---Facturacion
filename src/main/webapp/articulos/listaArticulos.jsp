<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Articulo" %>
<%
    List<Articulo> lista = (List<Articulo>) request.getAttribute("listaArticulos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Artículos Facturables</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="articulos"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
            <div class="page-header mb-0">
                <h1>Artículos Facturables</h1>
                <p class="subtitle mb-0">Administra los artículos que pueden ser facturados</p>
            </div>
            <a href="${pageContext.request.contextPath}/articulos?action=nuevo" class="btn btn-primary flex-shrink-0">Nuevo artículo</a>
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
                            <th>Precio Unitario</th>
                            <th>Estado</th>
                            <th class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Articulo a : lista) { %>
                        <tr>
                            <td><%= a.getId() %></td>
                            <td><%= a.getDescripcion() %></td>
                            <td>RD$ <%= String.format("%.2f", a.getPrecioUnitario()) %></td>
                            <td><span class="badge badge-status <%= "ACTIVO".equals(a.getEstado()) ? "bg-success" : "bg-secondary" %>"><%= a.getEstado() %></span></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/articulos?action=editar&id=<%= a.getId() %>" class="btn btn-sm btn-outline-secondary me-1">Editar</a>
                                <a href="${pageContext.request.contextPath}/articulos?action=eliminar&id=<%= a.getId() %>" class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Eliminar este artículo?');">Eliminar</a>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">📦</div>
                    <p class="mb-2">No hay artículos registrados.</p>
                    <a href="${pageContext.request.contextPath}/articulos?action=nuevo" class="btn btn-primary">Crear el primero</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
