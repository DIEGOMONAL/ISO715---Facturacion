<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Vendedor" %>
<%
    List<Vendedor> lista = (List<Vendedor>) request.getAttribute("listaVendedores");
    String buscar = (String) request.getAttribute("buscar");
    String ordenar = (String) request.getAttribute("ordenar");
    if (ordenar == null) ordenar = "nombre_asc";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Vendedores</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="vendedores"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
            <div class="page-header mb-0">
                <h1>Vendedores</h1>
                <p class="subtitle mb-0">Empleados de la empresa y sus comisiones</p>
            </div>
            <a href="${pageContext.request.contextPath}/vendedores?action=nuevo" class="btn btn-primary flex-shrink-0">Nuevo vendedor</a>
        </div>
        <form method="get" class="filtros-bar d-flex flex-wrap gap-2 align-items-end">
            <div class="input-group" style="max-width: 280px;">
                <span class="input-group-text">Buscar</span>
                <input type="text" class="form-control" name="buscar" placeholder="ID o nombre" value="<%= buscar != null ? buscar : "" %>">
                <button type="submit" class="btn btn-outline-primary">Buscar</button>
            </div>
            <div class="d-flex gap-2 align-items-center flex-wrap">
                <label class="mb-0">Ordenar:</label>
                <select name="ordenar" class="form-select form-select-sm" style="width: auto; min-width: 220px;">
                    <option value="id_asc" <%= "id_asc".equals(ordenar) ? "selected" : "" %>>ID (Ascendente)</option>
                    <option value="id_desc" <%= "id_desc".equals(ordenar) ? "selected" : "" %>>ID (Descendente)</option>
                    <option value="nombre_asc" <%= "nombre_asc".equals(ordenar) ? "selected" : "" %>>Nombre (Ascendente)</option>
                    <option value="nombre_desc" <%= "nombre_desc".equals(ordenar) ? "selected" : "" %>>Nombre (Descendente)</option>
                    <option value="comision_asc" <%= "comision_asc".equals(ordenar) ? "selected" : "" %>>% Comisión (Ascendente)</option>
                    <option value="comision_desc" <%= "comision_desc".equals(ordenar) ? "selected" : "" %>>% Comisión (Descendente)</option>
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
                            <th>Nombre</th>
                            <th>% Comisión</th>
                            <th>Estado</th>
                            <th class="text-end">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Vendedor v : lista) { %>
                        <tr>
                            <td><%= v.getId() %></td>
                            <td><%= v.getNombre() %></td>
                            <td><%= String.format("%.2f", v.getPorcientoComision()) %>%</td>
                            <td><span class="badge badge-status <%= "ACTIVO".equals(v.getEstado()) ? "bg-success" : "bg-secondary" %>"><%= v.getEstado() %></span></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/vendedores?action=editar&id=<%= v.getId() %>" class="btn btn-sm btn-outline-secondary me-1">Editar</a>
                                <a href="${pageContext.request.contextPath}/vendedores?action=eliminar&id=<%= v.getId() %>" class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('¿Eliminar este vendedor?');">Eliminar</a>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">👤</div>
                    <p class="mb-2">No hay vendedores registrados.</p>
                    <a href="${pageContext.request.contextPath}/vendedores?action=nuevo" class="btn btn-primary">Crear el primero</a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
