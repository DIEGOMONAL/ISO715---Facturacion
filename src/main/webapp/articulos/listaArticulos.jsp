<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Articulo" %>
<%
    List<Articulo> lista = (List<Articulo>) request.getAttribute("listaArticulos");
    String buscar = (String) request.getAttribute("buscar");
    String ordenar = (String) request.getAttribute("ordenar");
    if (ordenar == null) ordenar = "descripcion_asc";
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
        <form method="get" class="filtros-bar d-flex flex-wrap gap-2 align-items-end">
            <div class="input-group" style="max-width: 280px;">
                <span class="input-group-text">Buscar</span>
                <input type="text" class="form-control" name="buscar" placeholder="ID o descripción" value="<%= buscar != null ? buscar : "" %>">
                <button type="submit" class="btn btn-outline-primary">Buscar</button>
            </div>
            <div class="d-flex gap-2 align-items-center flex-wrap">
                <label class="mb-0">Ordenar:</label>
                <select name="ordenar" class="form-select form-select-sm" style="width: auto; min-width: 200px;">
                    <option value="id_asc" <%= "id_asc".equals(ordenar) ? "selected" : "" %>>ID (Ascendente)</option>
                    <option value="id_desc" <%= "id_desc".equals(ordenar) ? "selected" : "" %>>ID (Descendente)</option>
                    <option value="descripcion_asc" <%= "descripcion_asc".equals(ordenar) ? "selected" : "" %>>Descripción (Ascendente)</option>
                    <option value="descripcion_desc" <%= "descripcion_desc".equals(ordenar) ? "selected" : "" %>>Descripción (Descendente)</option>
                    <option value="precio_asc" <%= "precio_asc".equals(ordenar) ? "selected" : "" %>>Precio (Ascendente)</option>
                    <option value="precio_desc" <%= "precio_desc".equals(ordenar) ? "selected" : "" %>>Precio (Descendente)</option>
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
                            <th>Descripción</th>
                            <th>Precio Unitario</th>
                            <th>Disponible</th>
                            <th>Estado</th>
                            <th class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Articulo a : lista) { %>
                        <tr>
                            <td><%= a.getId() %></td>
                            <td><%= a.getDescripcion() %></td>
                            <td>RD$ <%= new java.text.DecimalFormat("#,##0.00").format(a.getPrecioUnitario()) %></td>
                            <td><%= a.getCantidadDisponible() %></td>
                            <td><span class="badge badge-status <%= "ACTIVO".equals(a.getEstado()) ? "bg-success" : "bg-secondary" %>"><%= a.getEstado() %></span></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/articulos?action=editar&id=<%= a.getId() %>" class="btn btn-sm btn-outline-secondary me-1">Editar</a>
                                <% if ("ACTIVO".equals(a.getEstado())) { %>
                                <a href="${pageContext.request.contextPath}/articulos?action=eliminar&id=<%= a.getId() %>" class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Desactivar este artículo? Ya no saldrá para nuevas facturas.');">Desactivar</a>
                                <% } else { %>
                                <a href="${pageContext.request.contextPath}/articulos?action=recuperar&id=<%= a.getId() %>" class="btn btn-sm btn-outline-success"
                                   onclick="return confirm('¿Reactivar este artículo?');">Recuperar</a>
                                <% } %>
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
