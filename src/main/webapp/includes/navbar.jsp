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
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Sistema de Facturación</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link ${param.page eq 'inicio' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Inicio</a>
                </li>
                <% if (showCatalogos) { %>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page eq 'articulos' || param.page eq 'clientes' || param.page eq 'condiciones' || param.page eq 'vendedores' ? 'active' : ''}" href="#" id="catDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">Catálogos</a>
                    <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="catDropdown">
                        <li><a class="dropdown-item ${param.page eq 'articulos' ? 'active' : ''}" href="${pageContext.request.contextPath}/articulos">Artículos</a></li>
                        <li><a class="dropdown-item ${param.page eq 'clientes' ? 'active' : ''}" href="${pageContext.request.contextPath}/clientes">Clientes</a></li>
                        <li><a class="dropdown-item ${param.page eq 'condiciones' ? 'active' : ''}" href="${pageContext.request.contextPath}/condicionesPago">Condiciones de Pago</a></li>
                        <li><a class="dropdown-item ${param.page eq 'vendedores' ? 'active' : ''}" href="${pageContext.request.contextPath}/vendedores">Vendedores</a></li>
                    </ul>
                </li>
                <% } %>
                <% if (showFacturacion) { %>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page eq 'facturas' || param.page eq 'consulta' || param.page eq 'reporte' ? 'active' : ''}" href="#" id="factDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">Facturación</a>
                    <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="factDropdown">
                        <% if (!isContador) { %>
                        <li><a class="dropdown-item ${param.page eq 'facturas' ? 'active' : ''}" href="${pageContext.request.contextPath}/facturas">Facturas</a></li>
                        <% } else { %>
                        <li><a class="dropdown-item ${param.page eq 'facturas' ? 'active' : ''}" href="${pageContext.request.contextPath}/facturas">Ver Facturas</a></li>
                        <% } %>
                        <li><a class="dropdown-item ${param.page eq 'consulta' ? 'active' : ''}" href="${pageContext.request.contextPath}/consulta">Consulta</a></li>
                        <li><a class="dropdown-item ${param.page eq 'reporte' ? 'active' : ''}" href="${pageContext.request.contextPath}/reporte">Reporte Cliente</a></li>
                    </ul>
                </li>
                <% } %>
                <% if (isAdmin) { %>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle ${param.page eq 'admin' ? 'active' : ''}" href="#" id="adminDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">Administración</a>
                    <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="adminDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin">Panel</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/pendientes">Usuarios pendientes</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/usuarios">Usuarios</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/empresa">Config. Empresa</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/auditoria">Auditoría</a></li>
                    </ul>
                </li>
                <% } %>
            </ul>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <span class="nav-link usuario-label"><%= session.getAttribute("nombreCompleto") != null ? session.getAttribute("nombreCompleto") : session.getAttribute("usuario") %> (<%= rol != null ? Roles.getLabel(rol) : "" %>)</span>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/logout">Salir</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
