<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="util.Roles" %>
<%
    String rol = (String) session.getAttribute("rol");
    boolean isAdmin = Roles.ADMIN.equals(rol);
    boolean isCajero = Roles.CAJERO.equals(rol);
    boolean isInventario = Roles.INVENTARIO.equals(rol);
    boolean isContador = Roles.CONTADOR.equals(rol);
    boolean isSupervisor = Roles.SUPERVISOR.equals(rol);
    boolean showCatalogos = isAdmin || isInventario || isCajero || isSupervisor;
    boolean showFacturacion = isAdmin || isCajero || isSupervisor || isContador;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Inicio - Sistema de Facturación</title>
    <jsp:include page="/includes/head.jsp"/>
</head>
<body class="bg-light">
<jsp:include page="/includes/navbar.jsp"><jsp:param name="page" value="inicio"/></jsp:include>

<main class="py-5">
    <div class="container">
        <div class="page-header mb-4">
            <h1>Sistema de Facturación</h1>
            <p class="subtitle">Bienvenido, <%= session.getAttribute("nombreCompleto") != null ? session.getAttribute("nombreCompleto") : session.getAttribute("usuario") %></p>
        </div>
        <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-warning"><%= request.getParameter("error") %></div>
        <% } %>
        <div class="row g-4">
            <% if (showCatalogos) { %>
            <div class="col-md-6">
                <div class="quick-link-card">
                    <h5>Catálogos</h5>
                    <ul class="list-unstyled mb-0">
                        <li class="mb-2"><a href="${pageContext.request.contextPath}/articulos">Artículos facturables</a></li>
                        <li class="mb-2"><a href="${pageContext.request.contextPath}/clientes">Clientes</a></li>
                        <li class="mb-2"><a href="${pageContext.request.contextPath}/condicionesPago">Condiciones de pago</a></li>
                        <li><a href="${pageContext.request.contextPath}/vendedores">Vendedores</a></li>
                    </ul>
                </div>
            </div>
            <% } %>
            <% if (showFacturacion) { %>
            <div class="col-md-6">
                <div class="quick-link-card">
                    <h5>Facturación y reportes</h5>
                    <ul class="list-unstyled mb-0">
                        <% if (!isContador) { %>
                        <li class="mb-2"><a href="${pageContext.request.contextPath}/facturas">Facturación de artículos</a></li>
                        <% } else { %>
                        <li class="mb-2"><a href="${pageContext.request.contextPath}/facturas">Ver facturas</a></li>
                        <% } %>
                        <li class="mb-2"><a href="${pageContext.request.contextPath}/consulta">Consulta por criterios</a></li>
                        <li><a href="${pageContext.request.contextPath}/reporte">Reporte por cliente entre fechas</a></li>
                    </ul>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
